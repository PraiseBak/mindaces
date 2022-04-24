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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserObjController
{
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
    public String userObjAddPost(UserObjDto userObjDto, Authentication authentication, RedirectAttributes re)
    {

        if(new ValidCheck().isValidObjInput(userObjDto))
        {
            objService.addObj(authentication,userObjDto);
        }
        else
        {
            re.addAttribute("error","입력 양식이 맞지 않습니다");
            return "redirect:";
        }

        return "redirect:/user/userObjPage";
    }


}
