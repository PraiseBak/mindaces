package com.mindaces.mindaces.api;


import com.mindaces.mindaces.dto.UserObjDto;

import java.util.regex.Pattern;

public class ValidCheck
{

    public boolean isSignupValid(String inputID, String userPassword, String userEmail)
    {
        //@한개만 나왔는지 @.한개만 나왔는지
        if (!isValidEmail(userEmail))
        {
            return false;
        }
        if (!isValidID(inputID))
        {
            return false;
        }
        if(!isValidPassword(userPassword))
        {
            return false;
        }
        return true;
    }

    public boolean isValidEmail(String email)
    {
        if(email.length() < 4 || email.length() > 40)
        {
            return false;
        }
        //이메일 체크
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        return Pattern.matches(regex,email);
    }

    public boolean isValidID(String id)
    {
        if(id.length() < 2 || id.length() > 20)
        {
            return false;
        }
        //닉네임은 한글, 영문, 숫자만 가능하며 2-20자리 가능.
        String regex = "^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣])+$";
        return Pattern.matches(regex,id);
    }

    public boolean isValidPassword(String password)
    {
        //'숫자', '문자' 무조건 1개 이상, '최소 8자에서 최대 20자' 허용 \
        // (특수문자는 정의된 특수문자만 사용 가능)

        if(password.length() < 8 || password.length() > 20)
        {
            return false;
        }

        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]+$";
        return Pattern.matches(regex,password);
    }

    public boolean isValidObjInput(UserObjDto userObjDto)
    {
        String title = userObjDto.getObjTitle();
        String content = userObjDto.getObjContent();
        Integer day = userObjDto.getObjDay();
        if(title.length() < 2 || title.length() > 20){
            return false;
        }else if(content.length() < 2 || content.length() > 40) {
            return false;
        }else if(day < 1 || day > 3650) {
            return false;
        }
        return true;
    }


}
