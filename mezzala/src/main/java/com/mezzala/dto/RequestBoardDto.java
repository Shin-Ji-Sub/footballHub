package com.mezzala.dto;

import com.mezzala.common.DateParsing;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class RequestBoardDto {

    private int boardId;
    private String subject;
    private String content;
    private String category;
    // default(now())
    private Timestamp regDate;
    // default(true)
    private boolean boardStatus;
    private int commentCount;

    // user
    private String userId;

    UserDto user;
    List<CommentDto> comments;

    // regDate를 변환하여 반환하는 getter
    public String getRegDateFormatted() {
        if (regDate == null) {
            return null;
        }
        DateParsing dateParsing = new DateParsing();
        return dateParsing.calculateTime(regDate);
    }

}
