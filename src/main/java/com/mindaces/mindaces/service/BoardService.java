package com.mindaces.mindaces.service;


import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.repository.BoardRepository;
import com.mindaces.mindaces.dto.BoardDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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


}
