package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.entity.BoardInfo;
import org.springframework.stereotype.Service;

@Service
public class BoardInfoService
{
    CommentService commentService;

    public BoardInfoService(CommentService commentService)
    {
        this.commentService = commentService;

    }

    public void updateCommentCount(Board board)
    {
        Long commentCount;
        commentCount = commentService.countByBoardIdx(board.getContentIdx());
        board.getBoardInfo().updateCommentCount(commentCount);
    }




}
