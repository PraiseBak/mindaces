package com.mindaces.mindaces.service;

import com.mindaces.mindaces.dto.MailDto;
import com.mindaces.mindaces.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Random;


@Service
@AllArgsConstructor
public class SendEmailService
{
    private UtilService utilService;
    private JavaMailSender mailSender;
    private UserService userService;


    private static final String FROM_ADDRESS = "praisebak@naver.com";

    private MailDto getChangePWMailDto(UserDto userDto,String randomPassword)
    {
        MailDto mailDto;
        mailDto = MailDto.builder()
                .title("mindaces 임시 비밀번호 메일입니다")
                .sendAddress(FROM_ADDRESS)
                .receiveAddress(userDto.getUserEmail())
                .build();

        mailDto.setMailContent(
                "mindaces 유저 " + userDto.getUserID() +"님의 임시 비밀번호 입니다\n" +
                "임시 비밀번호:" + randomPassword +
                "\n임시 비밀번호로 로그인 후 비밀번호를 변경해주세요"
        );
        return mailDto;
    }

    private MailDto getSignupMailDto(UserDto userDto,String randomKey)
    {
        MailDto mailDto;
        mailDto = MailDto.builder()
                .title("mindaces 회원가입 메일입니다")
                .sendAddress(FROM_ADDRESS)
                .receiveAddress(userDto.getUserEmail())
                .build();

        mailDto.setMailContent(
                //TODO 링크 수정
                "mindaces 유저 " + userDto.getUserID() +"님의 회원가입 링크입니다 아래 링크를 클릭해주세요\n" +
                        "https://localhost:8080/signup/" + randomKey
        );
        return mailDto;
    }

    private String getRandomPassword()
    {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++)
        {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }

        return str;
    }


    public Boolean sendEmail(UserDto userDto, Boolean isChangeMode)
    {
        try
        {
            String randomPassword;
            MailDto mailDto;
            randomPassword = getRandomPassword();
            if(isChangeMode)
            {
                mailDto = this.getChangePWMailDto(userDto, randomPassword);
            }
            else
            {
                String randomKey = this.utilService.getRandomStr();
                mailDto = getSignupMailDto(userDto,randomKey);
            }
            SimpleMailMessage message;
            message = new SimpleMailMessage();
            message.setTo(mailDto.getReceiveAddress());
            message.setFrom(mailDto.getSendAddress());
            message.setSubject(mailDto.getTitle());
            message.setText(mailDto.getMailContent());
            mailSender.send(message);
            userService.changeAsRandomPassword(userDto, randomPassword);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

}
