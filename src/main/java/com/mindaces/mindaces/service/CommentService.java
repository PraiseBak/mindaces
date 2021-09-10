package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.Comment;
import com.mindaces.mindaces.domain.repository.CommentRepository;
import com.mindaces.mindaces.dto.CommentDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService
{
    private CommentRepository commentRepository;
    private RoleService roleService;

    public CommentService(CommentRepository commentRepository,RoleService roleService)
    {
        this.roleService = roleService;
        this.commentRepository = commentRepository;
    }

    public List<CommentDto> getCommentByContentIdxAndGalleryName(String galleryName, Long contentIdx)
    {
        List<Comment> commentList = commentRepository.getCommentByBoardIdxAndGallery(contentIdx,galleryName);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for(Comment comment: commentList)
        {
            commentDtoList.add(convertEntityToDto(comment));
        }

        return commentDtoList;
    }

    private CommentDto convertEntityToDto(Comment comment)
    {
        return CommentDto.builder()
                .commentIdx(comment.getCommentIdx())
                .gallery(comment.getGallery())
                .user(comment.getUser())
                .content(comment.getContent())
                .isLogged(comment.getIsLogged())
                .likes(comment.getLikes())
                .dislikes(comment.getDislikes())
                .build();


    }


    public Boolean addCommentValidCheck(CommentDto commentDto)
    {
        int commentPasswordLen = commentDto.getCommentPassword().length();
        int userLen = commentDto.getUser().length();
        String content = commentDto.getContent();

        if(content.length() < 2 || content.getBytes().length > 65535)
        {
            return false;
        }

        if(commentPasswordLen < 4 || commentPasswordLen > 20)
        {
            return false;
        }

        if(userLen < 2 || userLen > 20)
        {
            return false;
        }

        return true;
    }

    public Boolean modifyCommendValidCheck(CommentDto commentDto)
    {
        String content = commentDto.getContent();
        if(content.length() < 2 || content.getBytes().length > 65535)
        {
            return false;
        }
        return true;

    }


    public Boolean addComment(String galleryName, Long contentIdx, Authentication authentication, CommentDto commentDto)
    {
        Boolean isValid = addCommentValidCheck(commentDto);
        if(isValid)
        {
            if(roleService.isUser(authentication))
            {
                commentDto.setIsLogged(1L);
                commentDto.setCommentPassword("****");
                commentDto.setUser(authentication.getName());
            }
            commentDto.setGallery(galleryName);
            commentDto.setBoardIdx(contentIdx);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            commentDto.setCommentPassword(passwordEncoder.encode(commentDto.getCommentPassword()));
            Comment comment = commentRepository.save(commentDto.toEntity());
            return true;
        }
        return false;
    }


    public Boolean deleteComment(CommentDto commentDto,Authentication authentication)
    {
        String userName = roleService.getUserName(authentication);
        //비로그인한 유저인경우 비밀번호 체크
        if(userName.equals("-"))
        {
            Comment matchComment = getMatchPasswordComment(commentDto);
            if(matchComment != null)
            {
                commentRepository.deleteById(commentDto.getCommentIdx());
                return true;
            }
        }
        //로그인한 유저인 경우 유저 체크
        else
        {
            if(isSameUser(commentDto,authentication))
            {
                commentRepository.deleteById(commentDto.getCommentIdx());
                return true;
            }
        }
        return false;
    }

    public Comment getMatchPasswordComment(CommentDto commentDto)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Long commentIdx = commentDto.getCommentIdx();
        String inputPassword = commentDto.getCommentPassword();
        Comment objComment = this.commentRepository.getById(commentIdx);
        if(objComment == null)
        {
            return null;
        }
        if(passwordEncoder.matches(inputPassword,objComment.getCommentPassword()))
        {
            return objComment;
        }
        return null;
    }

    public void modifyComment(CommentDto commentDto, Authentication authentication)
    {
        if(!modifyCommendValidCheck(commentDto))
        {
            return;
        }

        String userName = roleService.getUserName(authentication);
        //비로그인한 유저인경우 비밀번호 체크
        if(userName.equals("-"))
        {
            Comment matchComment = getMatchPasswordComment(commentDto);
            if(matchComment != null)
            {
                matchComment.setContent(commentDto.getContent());
                this.commentRepository.save(matchComment);
            }
        }
        else
        {
            if(isSameUser(commentDto,authentication))
            {
                Comment comment = commentRepository.getById(commentDto.getCommentIdx());
                comment.setContent(commentDto.getContent());
                this.commentRepository.save(comment);
            }
        }

    }

    public Boolean isSameUser(CommentDto commentDto,Authentication authentication)
    {
        String userName = roleService.getUserName(authentication);
        Long commentIdx = commentDto.getCommentIdx();
        if(userName.equals("-"))
        {
            return false;
        }
        Comment comment = commentRepository.getById(commentIdx);
        if(comment.getIsLogged() == 1L)
        {
            if(comment.getUser().equals(userName))
            {
                return true;
            }
        }
        return false;
    }
}
