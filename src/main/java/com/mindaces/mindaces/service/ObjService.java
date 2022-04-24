package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.User;
import com.mindaces.mindaces.domain.entity.UserObj;
import com.mindaces.mindaces.domain.repository.ObjRepository;
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
    RoleService roleService;
    UserService userService;

    @Transactional
    public void addObj(Authentication authentication, UserObjDto userObjDto)
    {
        long userID = -1;

        UserObj userObj;
        User user;

        userObj = userObjDto.toEntity();

        userID = userService.findUserID(roleService.getUsername(authentication));
        if(userID != -1)
        {
            userObj.setUserIdx(userID);
            objRepository.save(userObj);
        }
    }

    public List<UserObj> getObjListOrderByDate(Authentication authentication)
    {
        Long userID = -1L;
        userID = userService.findUserID(roleService.getUsername(authentication));
        List<UserObj> userObjs = null;
        if(userID != -1)
        {
             userObjs = objRepository.findAllByUserIdx(userID);
        }
        return userObjs;
    }

    public Long getUserIdxByObjIdx(Long objIdx)
    {
        return objRepository.getById(objIdx).getUserIdx();
    }

    @Transactional
    public void setRepresentObjByObjIdxAndUserIdx(Long objUserIdx, Long objIdx)
    {
        List<UserObj> objList;
        objList = getRepreUserObjListByUserIdx(objUserIdx);
        for(UserObj obj : objList)
        {
            obj.setIsRepresentObj(false);
        }
        UserObj obj = objRepository.findById(objIdx).get();
        obj.setIsRepresentObj(true);
    }

    List<UserObj> getRepreUserObjListByUserIdx(Long objUserIdx)
    {
        List<UserObj> objList;
        objList = objRepository.findAllByIsRepresentObjAndUserIdx(true,objUserIdx);
        return objList;
    }


    public UserObj getRepresentObj(Long curUserIdx)
    {
        List<UserObj> objList;
        objList = getRepreUserObjListByUserIdx(curUserIdx);
        if(objList != null && objList.size() != 1)
        {
            return null;
        }
        return objList.get(0);
    }

    public boolean delObjs(List<Long> selectedList, Authentication authentication)
    {
        for(Long idx : selectedList)
        {
            if(delObj(idx,authentication) == false)
            {
                return false;
            }
        }
        return true;
    }

    public Boolean delObj(Long objIdx, Authentication authentication)
    {
        Long userIdx = -1L;
        userIdx = userService.findUserID(authentication);
        Boolean isObjFromUserIdx;
        isObjFromUserIdx = isObjPresentByUserIdx(objIdx,userIdx);
        if(isObjFromUserIdx)
        {
            objRepository.deleteById(objIdx);
            return true;
        }
        return false;
        
    }

    private Boolean isObjPresentByUserIdx(Long objIdx, Long userIdx)
    {
        return this.objRepository.existsByUserIdxAndObjIdx(userIdx,objIdx);
    }
}
