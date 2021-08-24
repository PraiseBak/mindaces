package com.mindaces.mindaces.controller;


import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.entity.Gallery;
import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.service.BoardService;
import com.mindaces.mindaces.service.GalleryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/gallery")
//Board를 직접적으로 수정하게 되는 페이지에 대한 컨트롤러
public class BoardController
{
    BoardService boardService;
    GalleryService galleryService;
    String errorURL = "redirect:/error/galleryMiss";

    BoardController(BoardService boardService, GalleryService galleryService)
    {
        this.boardService = boardService;
        this.galleryService = galleryService;
    }

    @GetMapping(value = "/{galleryName}/postWrite")
    public String postWrite(
            @PathVariable(name = "galleryName") String galleryName,
            Model model,
            Authentication authentication
    )
    {
        if(!galleryService.isGallery(galleryName))
        {
            return errorURL;
        }

        Board board = new Board();
        if(boardService.isUser(authentication))
        {
            board.setUser(authentication.getName());
            board.setPassword("****");
        }

        model.addAttribute("inputPassword",null);
        model.addAttribute("board",board);
        return "gallery/postWrite";
    }

    @PostMapping(value = "/{galleryName}/postWrite")
    public String postSubmit(@PathVariable(name = "galleryName") String galleryName, BoardDto boardDto, Authentication authentication)
    {
        //로그인한 유저가 작성한 글의 비밀번호는 ****로 저장됨 어차피 나중에 확인할때 비밀번호 없이 확인할것이라 상관 X
        //TODO checkValid()
        //존재하지 않는 갤러리라던가 등
        if(!galleryService.isGallery(galleryName))
        {
            return errorURL;
        }

        if(boardService.isUser(authentication))
        {
            boardDto.setUser(authentication.getName());
            boardDto.setIsLoggedUser(1L);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boardDto.setGallery(galleryName);
        boardDto.setPassword(passwordEncoder.encode(boardDto.getPassword()));
        boardService.savePost(boardDto);
        return "redirect:";
    }

    @PostMapping(value = "/{galleryName}/modify/{index}")
    public String modify(
            @PathVariable(name = "galleryName") String galleryName,
            @PathVariable(name = "index") Long contentIdx,
            Model model,
            String hiddenPassword
    )
    {
        if(!galleryService.isGallery(galleryName))
        {
            return errorURL;
        }

        BoardDto boardDto = boardService.getBoardInfoByGalleryAndIdx(galleryName,contentIdx);

        boardDto.setPassword("****");
        model.addAttribute("inputPassword",hiddenPassword);
        model.addAttribute("board",boardDto);
        return "gallery/postWrite";
    }

    @PostMapping(value = "/{galleryName}/modify/postModify/{index}")
    public String postModify(
            @PathVariable(name = "galleryName") String galleryName,
            @PathVariable(name = "index") Long contentIdx,
            String hiddenPassword,
            BoardDto boardDto,
            Authentication authentication
    )
    {
        if(!galleryService.isGallery(galleryName))
        {
            return errorURL;
        }

        boardDto.setContentIdx(contentIdx);
        Boolean result;
        if(boardService.isUser(authentication))
        {
            result = boardService.isBoardModifyAuthValidLoggedUser(authentication,contentIdx,galleryName);
        }
        else
        {
            result = boardService.isBoardModifyAuthValidUser(contentIdx,hiddenPassword,galleryName);
        }
        if(result)
        {
            boardService.updatePost(boardDto);

        }
        return "redirect:/gallery/" + galleryName;
    }

    @PostMapping(value = "/{galleryName}/delete/{index}")
    public String deletePost(
            @PathVariable(name = "galleryName") String galleryName,
            @PathVariable(name = "index") Long contentIdx,
            String hiddenPassword,
            Authentication authentication
    )
    {
        if(!galleryService.isGallery(galleryName))
        {
            return errorURL;
        }
        Boolean result;

        result = boardService.isBoardModifyAuthValidLoggedUser(authentication,contentIdx,galleryName);
        if(!result)
        {
            result = boardService.isBoardModifyAuthValidUser(contentIdx,hiddenPassword,galleryName);
        }

        if(result)
        {
            boardService.deletePost(contentIdx);
        }

        return "redirect:/gallery/" + galleryName;
    }


}
