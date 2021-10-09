package com.mindaces.mindaces.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MailDto
{
    private String title;
    private String receiveAddress;
    private String sendAddress;
    private String mailContent;


    @Builder
    public MailDto(String title, String receiveAddress, String sendAddress, String mailContent)
    {
        this.title = title;
        this.receiveAddress = receiveAddress;
        this.sendAddress = sendAddress;
        this.mailContent = mailContent;
    }


}
