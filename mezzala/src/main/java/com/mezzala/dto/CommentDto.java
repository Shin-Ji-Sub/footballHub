package com.mezzala.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {

    private int commentId;
    private int parentId;
    private Date regDate;
    private String content;
    private boolean commentStatus;

    // user
    private String user;

}
