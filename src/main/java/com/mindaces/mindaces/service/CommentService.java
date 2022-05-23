package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.Comment;
import com.mindaces.mindaces.domain.entity.CommentLikedUserInfo;
import com.mindaces.mindaces.domain.entity.Likes;
import com.mindaces.mindaces.domain.repository.CommentRepository;
import com.mindaces.mindaces.domain.repository.LikesRepository;
import com.mindaces.mindaces.dto.CommentDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService
{
    private CommentRepository commentRepository;
    private RoleService roleService;
    private LikesRepository likesRepository;
    private UtilService utilService;
    private BoardSearchService boardSearchService;

    final static int COMMENT_PER_PAGE = 10;
    final static int MAX_PAGE_BUTTON_NUM = 10;

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

        String commentPassword = commentDto.getCommentPassword();
        String user = commentDto.getUser();
        String content =commentDto.getContent();

        if(utilService.isLRWhiteSpace(commentPassword) || utilService.isLRWhiteSpace(user))
        {
            return false;
        }
        int commentPasswordLen = commentPassword.length();
        int userLen = user.length();


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
    /*
    public Boolean modifyCommentValidCheck(CommentDto commentDto)
    {
        String content = commentDto.getContent();
        if(content.length() < 2 || content.getBytes().length > 65535)
        {
            return false;
        }
        return true;

    }

     */


    void updateParentComment(Long parentCommentIdx,Comment nestedComment)
    {
        Comment parentComment;
        parentComment = this.getCommentByID(parentCommentIdx);
        parentComment.addNestedComment(nestedComment);

    }



    @Transactional
    public Boolean addComment(String galleryName, Long contentIdx, Authentication authentication, CommentDto commentDto)
    {
        Boolean isValid = addCommentValidCheck(commentDto);
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
            commentDto.setContent(commentDto.getContent());

            Comment savedComment = commentRepository.save(commentDto.toEntity());

            Likes likes = Likes.builder()
                    .contentIdx(savedComment.getContentIdx())
                    .isComment(true)
                    .build();

            savedComment.setLikes(likes);
            commentRepository.save(savedComment);
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean deleteComment(CommentDto commentDto,Authentication authentication)
    {
        String username = roleService.getUsername(authentication);
        //비로그인한 유저인경우 비밀번호 체크
        if(username.equals("-"))
        {
            Comment matchComment = getMatchPasswordComment(commentDto);
            if(matchComment != null)
            {
                likesRepository.deleteByContentIdxAndIsComment(commentDto.getContentIdx(),true);
                commentRepository.deleteById(commentDto.getContentIdx());
                return true;
            }
        }
        //로그인한 유저인 경우 유저 체크
        else
        {
            if(isSameUser(commentDto,authentication))
            {
                likesRepository.deleteByContentIdxAndIsComment(commentDto.getContentIdx(),true);
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
        if(objComment != null)
        {
            if(passwordEncoder.matches(inputPassword,objComment.getCommentPassword()))
            {
                return objComment;
            }
        }
        return null;
    }
    /*
    public void modifyComment(CommentDto commentDto, Authentication authentication)
    {
        if(!modifyCommendValidCheck(commentDto))
        {
            return;
        }

        String username = roleService.getUsername(authentication);
        //비로그인한 유저인경우 비밀번호 체크
        if(username.equals("-"))
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
    */

    public Boolean isSameUser(CommentDto commentDto,Authentication authentication)
    {
        String username = roleService.getUsername(authentication);
        Long commentIdx = commentDto.getContentIdx();
        if(username.equals("-"))
        {
            return false;
        }
        Comment comment = commentRepository.getById(commentIdx);
        if(comment.getIsLogged() == 1L)
        {
            if(comment.getUser().equals(username))
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

    public List<CommentDto> getPagedCommentList(String galleryName, Long boardIdx, String username, int page)
    {
        Page<Comment> pageEntity;

        if(galleryName != null)
        {
            pageEntity = commentRepository.findByGalleryAndBoardIdx(galleryName,boardIdx,
                    PageRequest.of(page - 1, COMMENT_PER_PAGE, Sort.by(Sort.Direction.DESC, "createdDate")));
        }
        else
        {
            pageEntity = commentRepository.findByUserAndIsLogged(username,1L,
                    PageRequest.of(page - 1, COMMENT_PER_PAGE, Sort.by(Sort.Direction.DESC, "createdDate")));
        }

        List<CommentDto>
                commentDtoList = new ArrayList<>();
        for(Comment perComment  : pageEntity)
        {
            commentDtoList.add(0,entityToDto(perComment));
        }
        return commentDtoList;
    }

    private CommentDto entityToDto(Comment comment)
    {
        return CommentDto.builder()
                .contentIdx(comment.getContentIdx())
                .content(comment.getContent())
                .gallery(comment.getGallery())
                .isLogged(comment.getIsLogged())
                .createdDate(comment.getCreatedDate())
                .modifiedDate(comment.getModifiedDate())
                .likes(comment.getLikes())
                .user(comment.getUser())
                .nestedCommentList(comment.getNestedCommentList())
                .parentCommentIdx(comment.getParentCommentIdx())
                .boardIdx(comment.getBoardIdx())
                .build();
    }

    public Integer[] getPageList(int curPage,Long count)
    {
        Integer[] pageList = new Integer[MAX_PAGE_BUTTON_NUM];
        Double postsTotalCount = Double.valueOf(count);
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/COMMENT_PER_PAGE)));
        Integer blockStartPageNum =
                (curPage <= MAX_PAGE_BUTTON_NUM / 2)
                        ? 1
                        : curPage - MAX_PAGE_BUTTON_NUM / 2;
        Integer blockLastPageNum =
                (totalLastPageNum > blockStartPageNum + MAX_PAGE_BUTTON_NUM - 1 )
                        ? blockStartPageNum + MAX_PAGE_BUTTON_NUM - 1
                        : totalLastPageNum;

        for (int val = blockStartPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
            if(totalLastPageNum == 1)
            {
                break;
            }
        }
        return pageList;
    }


    public void addingPagedCommentToModel(Model model, String galleryName, Long boardIdx, int page)
    {
        List<CommentDto> commentDtoList;
        Long count;
        Integer[] pageList;

        commentDtoList = getPagedCommentList(galleryName,boardIdx,null,page);
        count = this.commentRepository.countByGalleryAndBoardIdx(galleryName,boardIdx);
        pageList = getPageList(page,count);

        model.addAttribute("commentList",commentDtoList);
        model.addAttribute("commentPageList",pageList);
    }

    public void addingPagedCommentToModelByWritedUser(Model model, int page, String username)
    {
        List<CommentDto> commentDtoList;
        Long count;
        Integer[] pageList;
        String title = "";
        List<String> contentTitleList = new ArrayList<>();

        commentDtoList = getPagedCommentList(null,null,username,page);
        count = this.commentRepository.countByUserAndIsLogged(username,1L);
        pageList = getPageList(page,count);

        for(CommentDto comment : commentDtoList)
        {
            title = boardSearchService.getBoardTitleByBoardIdxAndGallery(comment.getBoardIdx(),comment.getGallery());
            contentTitleList.add(title);
        }

        model.addAttribute("contentTitleList",contentTitleList);
        model.addAttribute("commentList",commentDtoList);
        model.addAttribute("commentPageList",pageList);
    }

    public void saveComment(Comment comment)
    {
        this.commentRepository.save(comment);
    }

    @Transactional
    public void deleteCommentByBoardIdxAndGalleryName(Long contentIdx,String galleryName)
    {
        List<Comment> commentList = commentRepository.getCommentByBoardIdxAndGalleryOrderByCreatedDateAsc(contentIdx,galleryName);
        for(Comment comment : commentList)
        {
            comment.getCommentLikedUserInfoList().clear();
        }

        this.commentRepository.deleteByBoardIdxAndGallery(contentIdx,galleryName);
    }
}
