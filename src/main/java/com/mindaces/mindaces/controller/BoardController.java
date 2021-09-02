package com.mindaces.mindaces.controller;


import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.service.BoardService;
import com.mindaces.mindaces.service.GalleryService;
import com.mindaces.mindaces.service.RoleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "/gallery")
//Board를 직접적으로 수정하게 되는 페이지에 대한 컨트롤러
public class BoardController
{
    BoardService boardService;
    GalleryService galleryService;
    RoleService roleService;
    String errorGalleryURL = "redirect:/error/galleryMiss";
    String errorWriteURL = "redirect:/error/writeError";
    final String galleryError = "존재하지 않는 갤러리입니다";

    BoardController(BoardService boardService, GalleryService galleryService, RoleService roleService)
    {
        this.boardService = boardService;
        this.galleryService = galleryService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/test")
    public String postWrite(
    )
    {
        return "gallery/editor";
    }

    @GetMapping(value = "/{galleryName}/postWrite")
    public String postWrite(
            @PathVariable(name = "galleryName") String galleryName,
            Model model,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    )
    {
        if(!galleryService.isGallery(galleryName))
        {
            redirectAttributes.addAttribute("errorMsg",this.galleryError);
            return errorGalleryURL;
        }

        Board board = new Board();
        if(roleService.isUser(authentication))
        {
            board.setUser(authentication.getName());
            board.setPassword("****");
        }

        model.addAttribute("inputPassword",null);
        model.addAttribute("board",board);
        return "gallery/postWrite";
    }

    @PostMapping(value = "/{galleryName}/postWrite")
    public Object postSubmit(@PathVariable(name = "galleryName") String galleryName,
                             BoardDto boardDto,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes)
    {
        //로그인한 유저가 작성한 글의 비밀번호는 ****로 저장됨 어차피 나중에 확인할때 비밀번호 없이 확인할것이라 상관 X
        //TODO checkValidSignup()
        //존재하지 않는 갤러리라던가 등
        if(!galleryService.isGallery(galleryName))
        {
            return errorGalleryURL;
        }

        String checkBoardWriteValid = boardService.isBoardWriteValid(boardDto,authentication,"write");
        if(!checkBoardWriteValid.equals("통과"))
        {
            redirectAttributes.addAttribute("errorMsg",checkBoardWriteValid);
            return errorWriteURL;
        }

        if(roleService.isUser(authentication))
        {
            boardDto.setUser(authentication.getName());
            boardDto.setIsLoggedUser(1L);
        }

        boardDto.setGallery(galleryName);
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
            return errorGalleryURL;
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
            Authentication authentication,
            RedirectAttributes redirectAttributes
    )
    {
        if(!galleryService.isGallery(galleryName))
        {
            return errorGalleryURL;
        }

        String checkBoardWriteValid = boardService.isBoardWriteValid(boardDto,authentication,"modify");
        if(!checkBoardWriteValid.equals("통과"))
        {
            redirectAttributes.addAttribute("errorMsg",checkBoardWriteValid);
            return errorWriteURL;
        }

        boardDto.setContentIdx(contentIdx);
        Boolean result;
        if(roleService.isUser(authentication))
        {
            result = boardService.isBoardModifyAuthValidLoggedUser(authentication,contentIdx,galleryName);
        }
        else
        {
            result = boardService.isBoardModifyAuthValidUser(contentIdx,hiddenPassword,galleryName);
        }
        if(result)
        {
            boardService.updatePost(boardDto,galleryName);
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
            return errorGalleryURL;
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
