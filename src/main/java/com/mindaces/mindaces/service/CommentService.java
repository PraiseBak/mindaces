package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.entity.Comment;
import com.mindaces.mindaces.domain.entity.Likes;
import com.mindaces.mindaces.domain.repository.CommentRepository;
import com.mindaces.mindaces.domain.repository.LikesRepository;
import com.mindaces.mindaces.dto.CommentDto;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService
{
    private CommentRepository commentRepository;
    private RoleService roleService;
    private LikesRepository likesRepository;

    public CommentService(CommentRepository commentRepository,RoleService roleService,LikesRepository likesRepository)
    {
        this.roleService = roleService;
        this.commentRepository = commentRepository;
        this.likesRepository = likesRepository;
    }

    Sort getSortByCreateDate()
    {
        return Sort.by(Sort.Direction.ASC,"createdDate");

    }

    public List<CommentDto> getCommentByContentIdxAndGalleryName(String galleryName, Long contentIdx)
    {
        List<Comment> commentList = commentRepository.getCommentByBoardIdxAndGalleryOrderByCreatedDateAsc(contentIdx,galleryName);
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
                .contentIdx(comment.getContentIdx())
                .gallery(comment.getGallery())
                .user(comment.getUser())
                .content(comment.getContent())
                .isLogged(comment.getIsLogged())
                .likes(comment.getLikes())
                .createdDate(comment.getCreatedDate())
                .modifiedDate(comment.getModifiedDate())
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

    @Transactional
    public Boolean addComment(String galleryName, Long contentIdx, Authentication authentication, CommentDto commentDto)
    {
        Boolean isValid = addCommentValidCheck(commentDto);
        Board board;
        if(isValid)
        {
            commentDto.setIsLogged(0L);
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

            Comment savedComment = commentRepository.save(commentDto.toEntity());

            Likes likes = Likes.builder()
                .contentIdx(savedComment.getContentIdx())
                .isComment(true)
                .build();

            savedComment.setLikes(likes);
            likesRepository.save(likes);
            commentRepository.save(savedComment);

            return true;
        }
        return false;
    }

    @Transactional
    public Boolean deleteComment(CommentDto commentDto,Authentication authentication)
    {
        String userName = roleService.getUserName(authentication);
        //비로그인한 유저인경우 비밀번호 체크
        if(userName.equals("-"))
        {
            Comment matchComment = getMatchPasswordComment(commentDto);
            if(matchComment != null)
            {
                likesRepository.deleteByIsCommentAndContentIdx(true,commentDto.getContentIdx());
                commentRepository.deleteById(commentDto.getContentIdx());
                return true;
            }
        }
        //로그인한 유저인 경우 유저 체크
        else
        {
            if(isSameUser(commentDto,authentication))
            {
                likesRepository.deleteByIsCommentAndContentIdx(true,commentDto.getContentIdx());
                commentRepository.deleteById(commentDto.getContentIdx());
                return true;
            }
        }
        return false;
    }

    public Comment getMatchPasswordComment(CommentDto commentDto)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Long commentIdx = commentDto.getContentIdx();
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
                matchComment.modifyContent(commentDto.getContent());
                this.commentRepository.save(matchComment);
            }
        }
        else
        {
            if(isSameUser(commentDto,authentication))
            {
                Comment comment = commentRepository.getById(commentDto.getContentIdx());
                comment.modifyContent(commentDto.getContent());
                this.commentRepository.save(comment);
            }
        }

    }

    public Boolean isSameUser(CommentDto commentDto,Authentication authentication)
    {
        String userName = roleService.getUserName(authentication);
        Long commentIdx = commentDto.getContentIdx();
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

    public boolean updateLikes(Long commentIdx)
    {
        try
        {
            Likes likes = likesRepository.findByContentIdxAndIsComment(commentIdx,true);
            likes.updateLike();
            likesRepository.save(likes);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }


    public Comment getCommentByID(Long commentIdx)
    {
        return commentRepository.getById(commentIdx);
    }

    public Long countByBoardIdx(Long boardIdx)
    {
        return this.commentRepository.countByBoardIdx(boardIdx);
    }

}
