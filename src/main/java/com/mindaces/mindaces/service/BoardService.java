package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.repository.BoardRepository;
import com.mindaces.mindaces.dto.BoardDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BoardService
{
    private BoardRepository boardRepository;
    public BoardService(BoardRepository boardRepository)
    {
        this.boardRepository = boardRepository;
    }

    @Transactional
    //트랜잭션임 끝나면 commit됨
    //여기선 save할 것이기 떄문에 Transaction으로 선언해줌
    public Long savePost(BoardDto boardDto)
    {
        return boardRepository.save(boardDto.toEntity()).getGalleryIdx();
    }




}
