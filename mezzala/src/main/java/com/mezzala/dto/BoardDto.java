package com.mezzala.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BoardDto {

    private int boardId;
    private String subject;
    private String content;
    private Date regDate;
    private int visitCount;
    private int recommendationCount;
    private boolean boardStatus;

    // user
    private String userId;
    // board_large_category
    private int largeCategoryId;
    // board_small_category
    private int smallCategoryIndex;

    List<CommentDto> comments;

}
