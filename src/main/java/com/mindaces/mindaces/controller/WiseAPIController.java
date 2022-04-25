package com.mindaces.mindaces.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequestMapping(value = "/API")
@Controller()
public class WiseAPIController
{
    @ResponseBody
    @PostMapping(value = "/getRandWise")
    String getRandWise()
    {
        ClassPathResource resource = new ClassPathResource("static/text/wises.txt");
        String result = "";
        try
        {
            Path path = Paths.get(resource.getURI());
            List<String> list = Files.readAllLines(path);
            int rand = ((int) (Math.random() * 10000)) % list.size();
            result = list.get(rand);
        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("fileWise");
        }
        return result;
    }

}
