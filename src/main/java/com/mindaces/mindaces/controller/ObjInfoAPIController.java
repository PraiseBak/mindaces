package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.domain.entity.UserObj;
import com.mindaces.mindaces.dto.UserObjDto;
import com.mindaces.mindaces.service.ObjService;
import com.mindaces.mindaces.service.RoleService;
import com.mindaces.mindaces.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/API")
@AllArgsConstructor
@Controller
public class ObjInfoAPIController
{
    private RoleService roleService;
    private UserService userService;
    private ObjService objService;

    @PostMapping(value = "/setRepresentObj")
    @ResponseBody
    public Boolean setRepresentObj(Authentication authentication, UserObjDto userObjDto)
    {
        Long curUserIdx = userService.findUserID(roleService.getUsername(authentication));
        Long objUserIdx = objService.getUserIdxByObjIdx(userObjDto.getObjIdx());
        boolean isValid = curUserIdx == objUserIdx;
        if(isValid)
        {
            objService.setRepresentObjByObjIdxAndUserIdx(objUserIdx, userObjDto.getObjIdx());
        }

        return isValid;
    }

    @GetMapping(value = "/getRepresentObj")
    @ResponseBody
    public String getRepresentObj(Authentication authentication)
    {
        Long curUserIdx = userService.findUserID(roleService.getUsername(authentication));
        String result = "주목표가 없습니다";
        UserObj userObj = objService.getRepresentObj(curUserIdx);
        return result;
    }





}
