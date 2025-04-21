package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface AdminService {
    void addBoard(BoardDto board, List<Map<String, String>> imageFiles);

    int findAllNoticeBoardCount(String searchValue, boolean state);

    List<BoardDto> findNoticeBoardWithPaging(int start, String searchValue, boolean state);

    void modifyBoardState(int boardId, boolean state);

    void incrementVisitedBoard(int boardId);

    List<BoardDto> findBoardWithBoardId(int boardId);

    void modifyBoardsState(List<Integer> contentIds);

    List<BoardLargeCategoryDto> findCategories();

    int findAllBoardCount(String totalSelectValue, String smallSelectValue);

    List<BoardDto> findBoardWithPaging(int start, String sortValue, String totalSelectValue, String smallSelectValue);
}
