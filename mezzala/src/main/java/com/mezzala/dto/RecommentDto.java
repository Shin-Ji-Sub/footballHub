package com.mezzala.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RecommentDto {

    private int commentId;
    private int parentId;
    private Date regDate;
    private String content;
    // user
    private String userId;
    // board
    private int boardId;

}
