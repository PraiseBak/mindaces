package com.mindaces.mindaces;


import com.mindaces.mindaces.domain.entity.Comment;
import com.mindaces.mindaces.domain.entity.Likes;
import com.mindaces.mindaces.domain.repository.CommentRepository;
import com.mindaces.mindaces.domain.repository.LikesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MindacesApplicationTests
{
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	LikesRepository likesRepository;

	@Test
	public void save_comment()
	{
		Likes likes = new Likes(104L,1L,1L,false);
		likesRepository.save(likes);



		Comment originComment = commentRepository.getById(104L);
		Comment comment = Comment.builder()
						.likes(likes)
						.build();

		assertEquals("gd", "gd");
	}

}

