package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.api.ValidCheck;
import com.mindaces.mindaces.domain.entity.UserObj;
import com.mindaces.mindaces.dto.UserObjDto;
import com.mindaces.mindaces.service.ObjService;
import com.mindaces.mindaces.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserObjController
{
    private RoleService roleService;
    private ObjService objService;

    @GetMapping("/user/userObjPage")
    public String UserObjPage(Authentication authentication, Model model)
    {
        List<UserObj> userObjs = objService.getObjListOrderByDate(authentication);
        model.addAttribute("userObjList",userObjs);
        return "userObjPage/userObjInfo";
    }

    @GetMapping("/user/userObjAdd")
    public String userObjAdd()
    {
        return "userObjPage/userObjAdd";

    }

    @PostMapping("/user/userObjAdd")
    public String userObjAddPost(UserObjDto userObjDto,Authentication authentication)
    {
        if(new ValidCheck().isValidObjInput(userObjDto))
        {
            objService.addObj(authentication,userObjDto);
        }

        return "redirect:/user/userObjPage";
    }




}
