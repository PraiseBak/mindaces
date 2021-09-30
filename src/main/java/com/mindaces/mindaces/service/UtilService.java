package com.mindaces.mindaces.service;


import org.springframework.stereotype.Service;

@Service
public class UtilService
{
    String getTrimedStr(String origin)
    {
        return origin.trim();
    }

}
