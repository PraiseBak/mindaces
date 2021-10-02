package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.service.BoardService;
import com.mindaces.mindaces.service.LikesService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/API")
public class BoardAPIController
{
    LikesService likesService;
    BoardService boardService;

    BoardAPIController(BoardService boardService, LikesService likesService)
    {
        this.boardService = boardService;
        this.likesService = likesService;
    }


    @PostMapping("/checkBoardPasswordAPI")
    @ResponseBody
    public Boolean checkBoardPassword(BoardDto boardDto)
    {
        String galleryName = boardDto.getGallery();
        Long contentIdx = boardDto.getContentIdx();
        String inputPassword=  boardDto.getPassword();
        Boolean result = boardService.isBoardModifyAuthValidUser(contentIdx,inputPassword,galleryName);
        return result;
    }

    @PostMapping("/checkBoardUserAPI")
    @ResponseBody
    public Boolean checkUser(BoardDto boardDto, Authentication authentication)
    {
        return boardService.isBoardModifyAuthValidLoggedUser(authentication,boardDto.getContentIdx(),boardDto.getGallery());
    }

    @PostMapping("/checkBoardValidWriteAPI")
    @ResponseBody
    public String checkBoardValidWrite(BoardDto boardDto, Authentication authentication)
    {
        String mode = "write";
        if(boardDto.getPassword().length() == 0)
        {
            mode = "modify";
        }
        return boardService.isBoardWriteValid(boardDto,authentication,mode);
    }

    @PostMapping("/singleUploadImgAPI")
    @ResponseBody
    public HashMap singleUploadImgAPI(@RequestParam("Filedata") MultipartFile multipartFile, HttpSession httpSession)
    {
        HashMap fileInfo = new HashMap();

        // 업로드 파일이 존재하면
        if(multipartFile != null && !(multipartFile.getOriginalFilename().equals("")))
        {

            // 확장자 제한
            String originalName = multipartFile.getOriginalFilename(); // 실제 파일명
            String originalNameExtension = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase(); // 실제파일 확장자 (소문자변경)
            if( !( (originalNameExtension.equals("jpg")) || (originalNameExtension.equals("gif")) || (originalNameExtension.equals("png")) || (originalNameExtension.equals("bmp")) ) ){
                fileInfo.put("result", -1); // 허용 확장자가 아닐 경우
                return fileInfo;
            }

            // 파일크기제한 (1MB)
            long filesize = multipartFile.getSize(); // 파일크기
            long limitFileSize = 5*1024*1024; // 5MB
            if(limitFileSize < filesize){ // 제한보다 파일크기가 클 경우
                fileInfo.put("result", -2);
                return fileInfo;
            }

            // 저장경로
            String defaultPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources";
            String path = defaultPath + File.separator + "upload" + File.separator + "board" + File.separator + "images" + File.separator + "";

            // 저장경로 처리
            File file = new File(path);
            if(!file.exists()) { // 디렉토리 존재하지 않을경우 디렉토리 생성
                file.mkdirs();
            }

            // 파일 저장명 처리 (20150702091941-fd8-db619e6040d5.확장자)
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String today= formatter.format(new Date());
            String modifyName = today + "-" + UUID.randomUUID().toString().substring(20) + "." + originalNameExtension;

            // Multipart 처리
            try {
                // 서버에 파일 저장 (쓰기)
                multipartFile.transferTo(new File(path + modifyName));

                // 로그
                System.out.println("** upload 정보 **");
                System.out.println("** path : " + path + " **");
                System.out.println("** originalName : " + originalName + " **");
                System.out.println("** modifyName : " + modifyName + " **");
            } catch (Exception e) {
                return fileInfo;
            }

            // CallBack - Map에 담기
            String imageurl = httpSession.getServletContext().getContextPath() + "/upload/board/images/" + modifyName;    // separator와는 다름!
            fileInfo.put("imageurl", imageurl);     // 상대파일경로(사이즈변환이나 변형된 파일)
            fileInfo.put("filename", modifyName);   // 파일명
            fileInfo.put("filesize", filesize);     // 파일사이즈
            fileInfo.put("imagealign", "C");        // 이미지정렬(C:center)
            fileInfo.put("originalurl", imageurl);  // 실제파일경로
            fileInfo.put("thumburl", imageurl);     // 썸네일파일경로(사이즈변환이나 변형된 파일)

            fileInfo.put("result", 1);                // -1, -2를 제외한 아무거나 싣어도 됨
        }

        return fileInfo;    // @ResponseBody 어노테이션을 사용하여 Map을 JSON형태로 반환
    }

    @PostMapping("/requestBoardRecommendAPI")
    @ResponseBody
    public String recommendRequestAPI(@RequestParam Map<String, Object> map, HttpServletRequest request,Authentication authentication)
    {
        String result = this.likesService.recommand(map, request,authentication);
        return result;
    }

    @PostMapping("/getRenewBoardLikesAPI")
    @ResponseBody
    public Map<String,Long> getRecentLikesAPI(@RequestParam(value = "gallery") String gallery, @RequestParam(value = "boardIdx") Long boardIdx)
    {
        Map<String,Long> map = new HashMap<String,Long>();
        map = likesService.getRecentCommentLikes(gallery,boardIdx);
        return map;
    }




}
