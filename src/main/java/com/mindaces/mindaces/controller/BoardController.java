package com.mindaces.mindaces.controller;


import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "/gallery")
@AllArgsConstructor
//Board를 직접적으로 수정하게 되는 페이지에 대한 컨트롤러
public class BoardController
{
    BoardService boardService;
    CommentService commentService;
    GalleryService galleryService;
    RoleService roleService;
    LikesService likesService;
    BoardSearchService boardSearchService;
    final String errorGalleryURL = "redirect:/error/galleryMiss";
    final String errorWriteURL = "redirect:/error/writeError";
    final String galleryError = "존재하지 않는 갤러리입니다";


    @GetMapping(value = "/{galleryURL}/postWrite")
    public String postWrite(
            @PathVariable(name = "galleryURL") String galleryURL,
            Model model,
            Authentication authentication,
            RedirectAttributes attributes)
    {
        if(!galleryService.isGallery(galleryURL))
        {
            attributes.addAttribute("errorMsg",this.galleryError);
            return errorGalleryURL;
        }

        BoardDto boardDto = new BoardDto();
        if(roleService.isUser(authentication))
        {
            boardDto.setUser(authentication.getName());
            boardDto.setPassword("****");
        }

        model.addAttribute("inputPassword",null);
        model.addAttribute("board",boardDto);
        return "gallery/postWrite";
    }

    @PostMapping(value = "/{galleryURL}/postWrite")
    public Object postSubmit(@PathVariable(name = "galleryURL") String galleryURL,
                             BoardDto boardDto,
                             Authentication authentication,
                             RedirectAttributes attributes,
                             @RequestParam(required = false,defaultValue = "board",value = "pagingMode") String pagingMode,
                             @RequestParam(required = false,defaultValue = "1") Integer page)
    {
        //로그인한 유저가 작성한 글의 비밀번호는 ****로 저장됨 어차피 나중에 확인할때 비밀번호 없이 확인할것이라 상관 X
        //존재하지 않는 갤러리라던가 등
        if(!galleryService.isGallery(galleryURL))
        {
            return errorGalleryURL;
        }

        String checkBoardWriteValid = boardService.isBoardWriteValid(boardDto,authentication,"write");
        if(!checkBoardWriteValid.equals("통과"))
        {
            attributes.addAttribute("errorMsg",checkBoardWriteValid);
            return errorWriteURL;
        }
        boardDto.setIsLoggedUser(0L);
        if(roleService.isUser(authentication))
        {
            boardDto.setUser(authentication.getName());
            boardDto.setIsLoggedUser(1L);
        }

        String galleryName = galleryService.getGalleryNameByURL(galleryURL);
        boardDto.setGallery(galleryName);
        boardService.savePost(boardDto);

        attributes.addAttribute("pagingMode",pagingMode);
        attributes.addAttribute("page",page);

        return "redirect:";
    }

    @PostMapping(value = "/{galleryURL}/modify/{index}")
    public String modify(
            @PathVariable(name = "galleryURL") String galleryURL,
            @PathVariable(name = "index") Long contentIdx,
            Model model,
            String hiddenPassword,
            @RequestParam(required = false,defaultValue = "board",value = "pagingMode") String pagingMode,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            RedirectAttributes attributes
    )
    {
        if(!galleryService.isGallery(galleryURL))
        {
            return errorGalleryURL;
        }
        String galleryName = galleryService.getGalleryNameByURL(galleryURL);
        BoardDto boardDto = boardSearchService.getBoardDtoByGalleryAndIdx(galleryName,contentIdx);
        boardDto.setPassword("****");
        model.addAttribute("inputPassword",hiddenPassword);
        model.addAttribute("board",boardDto);
        model.addAttribute("pagingMode",pagingMode);
        model.addAttribute("page",page);
        attributes.addAttribute("pagingMode",pagingMode);
        attributes.addAttribute("page",page);

        return "gallery/postWrite";
    }

    @PostMapping(value = "/{galleryURL}/modify/postModify/{index}")
    public String postModify(
            @PathVariable(name = "galleryURL") String galleryURL,
            @PathVariable(name = "index") Long contentIdx,
            String hiddenPassword,
            BoardDto boardDto,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            @RequestParam(required = false,defaultValue = "board",value = "pagingMode") String pagingMode,
            @RequestParam(required = false,defaultValue = "1") Integer page

    )
    {
        String galleryName = galleryService.getGalleryNameByURL(galleryURL);
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
        redirectAttributes.addAttribute("pagingMode",pagingMode);
        redirectAttributes.addAttribute("page",page);
        return "redirect:/gallery/" + galleryName;
    }

    @PostMapping(value = "/{galleryURL}/delete/{index}")
    public String deletePost(
            @PathVariable(name = "galleryURL") String galleryURL,
            @PathVariable(name = "index") Long contentIdx,
            String hiddenPassword,
            Authentication authentication,
            @RequestParam(required = false,defaultValue = "board",value = "pagingMode") String pagingMode,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            RedirectAttributes attributes
    )
    {
        String galleryName = galleryService.getGalleryNameByURL(galleryURL);
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
            commentService.deleteCommentByBoardIdxAndGalleryName(contentIdx,galleryName);
            boardService.deletePost(contentIdx);
        }
        attributes.addAttribute("pagingMode",pagingMode);
        attributes.addAttribute("page",page);
        return "redirect:/gallery/" + galleryURL;
    }

}
