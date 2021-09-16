package com.mindaces.mindaces.dto;

import java.util.Comparator;

public class LikeComparator<C> implements Comparator<CommentDto>
{
    @Override
    public int compare(CommentDto a, CommentDto b)
    {
        return a.getLikes().getLikes() < b.getLikes().getLikes() ? 1 : a.getLikes().getLikes() == b.getLikes().getLikes() ? 0 : -1;
    }

}
