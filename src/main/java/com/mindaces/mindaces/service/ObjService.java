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
import java.util.List;

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
        if(user != null)
        {
            userID = user.getUserIdx();
            userObj.setUserIdx(userID);
            objRepository.save(userObj);

        }
    }

    public List<UserObj> getObjListOrderByDate(Authentication authentication)
    {
        Long userID;
        User user = userRepository.findByUserID(roleService.getUsername(authentication));
        List<UserObj> userObjs = null;
        if(user != null)
        {
             userID = user.getUserIdx();
             userObjs = objRepository.findAllByUserIdx(userID);
        }

        return userObjs;
    }
}
