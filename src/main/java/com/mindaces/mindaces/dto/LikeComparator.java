package com.mindaces.mindaces.dto;

import java.util.Comparator;

public class LikeComparator<C> implements Comparator<CommentDto>
{
    @Override
    public int compare(CommentDto a, CommentDto b)
    {
        return a.getLikes().getLike() < b.getLikes().getLike() ? 1 : a.getLikes().getLike() == b.getLikes().getLike() ? 0 : -1;
    }

}
