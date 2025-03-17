package com.mezzala.dto;

import com.mezzala.common.DateParsing;
import lombok.Data;

import java.sql.Timestamp;
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

    // user
    UserDto user;
    // comment_action
    List<CommentActionDto> commentActions;

    // 자기참조
    List<CommentDto> recomments;

    // regDate를 변환하여 반환하는 getter
    public String getRegDateFormatted() {
        if (regDate == null) {
            return null;
        }
        DateParsing dateParsing = new DateParsing();
        return dateParsing.calculateTime(regDate);
    }

}
