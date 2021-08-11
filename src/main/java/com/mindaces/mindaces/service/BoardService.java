package com.mindaces.mindaces.service;


import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.repository.BoardRepository;
import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.dto.GalleryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService
{
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository)
    {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Long savePost(BoardDto boardDto)
    {
        return boardRepository.save(boardDto.toEntity()).getContentIdx();
    }

    public Page<BoardDto> paging(@PageableDefault(sort="createdDate") Pageable pageRequest)
    {
        Page<Board> boardList = boardRepository.findAll(pageRequest);

        Page<BoardDto> pagingList = boardList.map(
                post -> new BoardDto(
                            post.getGallery(),post.getUser(),
                            post.getContentIdx(),post.getContent(),
                            post.getTitle(),post.getCreatedDate(),post.getModifiedDate()
                        ));

        for (BoardDto paging : pagingList)
        {
            System.out.println(paging.getTitle());
            System.out.println(paging.getContent());
            System.out.println(paging.getCreatedDate());
        }

        return pagingList;
    }



}
