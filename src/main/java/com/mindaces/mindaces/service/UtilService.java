package com.mindaces.mindaces.service;


import org.springframework.stereotype.Service;

@Service
public class UtilService
{
    String getTrimedStr(String origin)
    {
        return origin.trim();
    }

    Boolean isLRWhiteSpace(String origin)
    {
        try
        {
            if(origin.charAt(origin.length() -1) == ' ' || origin.charAt(0) == ' ')
            {
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    Boolean isThereWhiteSpace(String origin)
    {
        return origin.contains(" ");
    }


}
