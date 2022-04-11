package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.User;
import com.mindaces.mindaces.domain.entity.UserObj;
import com.mindaces.mindaces.domain.repository.ObjRepository;
import com.mindaces.mindaces.domain.repository.UserRepository;
import com.mindaces.mindaces.dto.UserObjDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class ObjService
{
    ObjRepository objRepository;
    UserRepository userRepository;
    RoleService roleService;

    @Transactional
    public void addObj(Authentication authentication, UserObjDto userObjDto)
    {
        long userID = -1;

        UserObj userObj;
        User user;
        userObj = userObjDto.toEntity();

        user = userRepository.findByUserID(roleService.getUsername(authentication));
        System.out.println(user == null);
        if(user != null)
        {
            userID = user.getUserIdx();
            System.out.println(userID);
            if(userID == -1){
                return;
            }

            userObj.setUserIdx(userID);
            objRepository.save(userObj);

        }
    }

}
