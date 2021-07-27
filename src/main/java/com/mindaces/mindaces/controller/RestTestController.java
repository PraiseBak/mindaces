package com.mindaces.mindaces.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class RestTestController
{

    @RequestMapping(value="/apitest", method= {RequestMethod.GET, RequestMethod.POST})
    public String callapihttp(){
        System.out.println("쉬새발끼야");
        StringBuffer result = new StringBuffer();
        try{
            String urlstr = "http://apis.data.go.kr/1741000/EarthquakeIndoors/getEarthquakeIndoorsList?" +
                    "ServiceKey=DQYqPdYO87JMLpXGo08vRxxJiINqQuS4u%2BVgvCjFeq53ox0z3kxHs%2BfLdE6VElzrSO0nT25zGtVtwlSMYh6YOg%3D%3D" +
                    "&type=json" +
                    "&pageNo=1" +
                    "&numOfRows=10" +
                    "&flag=Y";
            URL url = new URL(urlstr);
            HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8"));

            String returnLine;

            while((returnLine = br.readLine()) != null) {
                result.append(returnLine);
                System.out.println(br.readLine());
            }
            urlconnection.disconnect();

        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
}