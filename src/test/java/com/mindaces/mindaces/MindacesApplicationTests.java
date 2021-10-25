package com.mindaces.mindaces;


import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.entity.BoardLikedUserInfo;
import com.mindaces.mindaces.domain.entity.Comment;
import com.mindaces.mindaces.domain.entity.Likes;
import com.mindaces.mindaces.domain.repository.BoardLikedUserInfoRepository;
import com.mindaces.mindaces.domain.repository.BoardRepository;
import com.mindaces.mindaces.domain.repository.CommentRepository;
import com.mindaces.mindaces.domain.repository.LikesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MindacesApplicationTests
{
	@Autowired
	BoardLikedUserInfoRepository boardLikedUserInfoRepository;
	@Autowired
	BoardRepository boardRepository;

	@Transactional
	@Test
	public void 일대다_관계_추가_테스트()
	{
		String test = "test";
		//Board 초기화
		Board board = boardRepository.findById(71L).get();
		//BoardLikedUserInfo 초기화
		BoardLikedUserInfo boardLikedUserInfo = BoardLikedUserInfo.builder()
						.likedIP(test)
						.disLikedIP(test)
						.contentIdx(board.getContentIdx())
						.gallery(test)
						.username(test)
						.build();
		//many Entity 저장
		boardLikedUserInfoRepository.save(boardLikedUserInfo);
		//one Entity에 many Entity 저장
		board.getBoardLikedUserInfoList().add(boardLikedUserInfo);
		//one Entity 저장
		System.out.println(board.getBoardLikedUserInfoList().size());
		//제대로 추가 됐는가
		assertEquals(board.getBoardLikedUserInfoList().size(), 1);
	}

	@Transactional
	@Test
	public void 일대다_관계_삭제_테스트()
	{
		Board board = boardRepository.findById(71L).get();
		System.out.println(board.getBoardLikedUserInfoList().size());
		assertEquals(board.getBoardLikedUserInfoList().size(), 0);
	}

	@Test
	public void 빌더_테스트()
	{
		String test = "test";
		Board board = Board.builder()
				.user(test)
				.gallery(test)
				.content(test)
				.password(test)
				.title(test)
				.build();

		Board board2 = Board.builder()
				.user(test)
				.gallery(test)
				.content(test)
				.password(test)
				.title(test)
				.isLoggedUser(null)
				.build();


		boardRepository.save(board);
		test = "test2";
		Board board1;
		board1 = new Board(test,test,1L,test,test,null,test,null);
		boardRepository.save(board1);
		test = "test3";
		Board board2 = Board.builder()
				.user(test)
				.gallery(test)
				.content(test)
				.password(test)
				.title(test)
				.isLoggedUser(null)
				.build();
		boardRepository.save(board2);


	}


}

