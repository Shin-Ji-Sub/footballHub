package com.mezzala.service;

import com.mezzala.dto.*;

import java.util.List;
import java.util.Map;

public interface AdminService {
    void addBoard(BoardDto board, List<Map<String, String>> imageFiles);

    int findAllNoticeBoardCount(String searchValue, boolean state);

    List<BoardDto> findNoticeBoardWithPaging(int start, String searchValue, boolean state);

    void modifyBoardState(int boardId, boolean state);

    void incrementVisitedBoard(int boardId);

    List<BoardDto> findNoticeBoardWithBoardId(int boardId);

    void modifyBoardsState(List<Integer> contentIds);

    List<BoardLargeCategoryDto> findCategories();

    int findAllBoardCount(String totalSelectValue, String smallSelectValue, String searchValue);

    List<BoardDto> findBoardWithPaging(int start, String sortValue, String totalSelectValue, String smallSelectValue, String searchValue);

    List<BoardDto> findBoardWithBoardId(int boardId);

    int findAllReportBoardCount(String searchValue);

    List<BoardDto> findReportBoardWithPaging(int start, String sortValue, String searchValue);

    int findAllReportBoardCommentCount(String searchValue);

    List<CommentDto> findReportBoardCommentWithPaging(int start, String sortValue, String searchValue);

    int findAllCategoryCount(String categoryPart, int largeCategoryValue);

    List<BoardLargeCategoryDto> findLargeCategory(int start);

    List<BoardSmallCategoryDto> findSmallCategory(int start, int largeCategoryValue);

    List<BoardLargeCategoryDto> findAllLargeCategory();
}
