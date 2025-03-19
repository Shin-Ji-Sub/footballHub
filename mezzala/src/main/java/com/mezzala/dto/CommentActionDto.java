package com.mezzala.dto;

import lombok.Data;

@Data
public class CommentActionDto {

    private Integer actionId;
    // User
    private String userId;
    // Comment
    private int commentId;

    // comment
    CommentDto comment;

}
