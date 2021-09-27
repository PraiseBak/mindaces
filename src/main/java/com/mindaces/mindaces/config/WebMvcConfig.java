package com.mindaces.mindaces.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //web root가 아닌 외부 경로에 있는 리소스를 url로 불러올 수 있도록 설정
    //현재 localhost:8090/summernoteImage/1234.jpg
    //로 접속하면 C:/summernote_image/1234.jpg 파일을 불러온다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String defaultPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        String path = defaultPath + File.separator + "upload" + File.separator + "board" + File.separator + "images" + File.separator + "";
        registry.addResourceHandler("/upload/board/images/**")
                .addResourceLocations("file:///" + path );
    }


}