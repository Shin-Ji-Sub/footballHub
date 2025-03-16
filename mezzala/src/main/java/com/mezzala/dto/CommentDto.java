package com.mezzala.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentDto {

    private int commentId;
    private int parentId;
    private Date regDate;
    private String content;
    // default(true)
    private boolean commentState;
    // user
    private String userId;
    // board
    private int boardId;

    UserDto user;

    // 자기참조
    List<CommentDto> recomments;

}
