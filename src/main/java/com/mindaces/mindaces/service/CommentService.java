package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.Comment;
import com.mindaces.mindaces.domain.repository.CommentRepository;
import com.mindaces.mindaces.dto.CommentDto;
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
        List<Comment> commentlist = commentRepository.getCommentByBoardIdxAndGallery(contentIdx,galleryName);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for(Comment comment: commentList)
        {
            commentDtoList.add(comment.toEntity());
        }

        return commentDtoList;
    }

    private CommentDto converEntityToDto(Comment comment)
    {
        return CommentDto.builder()
                .
                .build();


    }


}


