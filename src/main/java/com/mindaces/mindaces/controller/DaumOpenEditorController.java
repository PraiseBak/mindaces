package com.mindaces.mindaces.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/daumOpenEditor")
public class DaumOpenEditorController
{
    @RequestMapping(value = "/imagePopup")
    public String imagePopup()
    {
        return "gallery/image";
    }

}
