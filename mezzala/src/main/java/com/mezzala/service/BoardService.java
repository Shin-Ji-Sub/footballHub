package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface BoardService {
    List<BoardLargeCategoryDto> findAllCategory();

    void addBoard(BoardDto board, List<Map<String, String>> imageFiles);

    List<BoardDto> findBoardWithPaging(int start, String category, String searchValue);

    int findAllBoardCount();

    List<BoardDto> findBoardWithBoardId(int boardId);

    void incrementVisitedBoard(int boardId);

    List<BoardDto> findBoardWithBoardNo(int boardNo);

    void addUserAction(UserDto user, int boardId, String actionCategory);

    void removeUserAction(UserDto user, int boardId, String actionCategory);

    void addComment(String content, int boardId, String userId);

    List<CommentDto> findCommentsWithBoardId(int boardId);
}
