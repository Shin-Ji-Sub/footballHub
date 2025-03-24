package com.mezzala.dto;

import com.mezzala.common.DateParsing;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class BoardDto {

    private int boardId;
    private String subject;
    private String content;
    // default(now())
    private Timestamp regDate;
    // default(0)
    private int visitCount;
    // default(0)
    private int recommendationCount;
    private int commentCount;
    // default(true)
    private boolean boardState;

    // user
    private String userId;
    // board_large_category
    private int largeCategoryId;
    // board_small_category
    private int smallCategoryIndex;

    UserDto user;
    List<CommentDto> comments;
    List<BoardAttachDto> boardAttaches;
    BoardLargeCategoryDto largeCategory;
    BoardSmallCategoryDto smallCategory;
    List<UserActionDto> userActions;

    // regDate를 변환하여 반환하는 getter
    public String getRegDateFormatted() {
        if (regDate == null) {
            return null;
        }
        DateParsing dateParsing = new DateParsing();
        return dateParsing.calculateTime(regDate);
    }

}
