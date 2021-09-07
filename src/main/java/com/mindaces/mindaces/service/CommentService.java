package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.Comment;
import com.mindaces.mindaces.domain.repository.CommentRepository;
import com.mindaces.mindaces.dto.CommentDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService
{
    private CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository)
    {
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
                .gallery(comment.getGallery())
                .user(comment.getUser())
                .content(comment.getContent())
                .isLogged(comment.getIsLogged())
                .likes(comment.getLikes())
                .dislikes(comment.getDislikes())
                .build();


    }


    public Boolean commentValidCheck(CommentDto commentDto)
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

    private Boolean isUser(Authentication authentication)
    {
        if(authentication != null)
        {
            if(authentication.getPrincipal() instanceof User)
            {
                return true;
            }
        }
        return false;
    }


    public Boolean addComment(String galleryName, Long contentIdx, Authentication authentication, CommentDto commentDto)
    {
        Boolean isValid = commentValidCheck(commentDto);
        if(isValid)
        {
            if(isUser(authentication))
            {
                commentDto.setIsLogged(1L);
                commentDto.setCommentPassword("****");
                commentDto.setUser(authentication.getName());
            }
            commentDto.setGallery(galleryName);
            commentDto.setBoardIdx(contentIdx);
            Comment comment = commentRepository.save(commentDto.toEntity());
            return true;
        }
        return false;
    }


    public Boolean deleteComment(String galleryName, Long contentIdx,CommentDto commentDto)
    {


        return false;
    }
}
