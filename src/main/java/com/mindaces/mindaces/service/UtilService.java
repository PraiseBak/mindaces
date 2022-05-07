package com.mindaces.mindaces.service;


import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UtilService
{
    String getTrimedStr(String origin)
    {
        return origin.trim();
    }

    public String getRandomStr()
    {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
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
