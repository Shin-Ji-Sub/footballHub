package com.mezzala.service;

import com.mezzala.dto.*;
import com.mezzala.mapper.AdminMapper;
import com.mezzala.mapper.BoardMapper;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AdminServiceImpl implements AdminService {
    @Setter
    AdminMapper adminMapper;

    @Override
    public void addBoard(BoardDto board, List<Map<String, String>> imageFiles) {
        adminMapper.insertBoard(board);
        if (imageFiles != null && !imageFiles.isEmpty()) {
            int boardId = board.getBoardId();
            adminMapper.insertBoardAttach(imageFiles, boardId);
        }
    }

    @Override
    public int findAllNoticeBoardCount(String searchValue, boolean state) {
        return adminMapper.selectAllNoticeBoardCount(searchValue, state);
    }

    @Override
    public List<BoardDto> findNoticeBoardWithPaging(int start, String searchValue, boolean state) {
        int limit = state ? 5 : 10;
        return adminMapper.selectNoticeBoardWithPaging(start, searchValue, state);
    }

    @Override
    public void modifyBoardState(int boardId, boolean state) {
        adminMapper.updateBoardState(boardId, state);
    }

    @Override
    public void incrementVisitedBoard(int boardId) {
        adminMapper.updateVisitedBoard(boardId);
    }

    @Override
    public List<BoardDto> findNoticeBoardWithBoardId(int boardId) {
        return adminMapper.selectNoticeBoardWithBoardId(boardId);
    }

    @Override
    public void modifyBoardsState(List<Integer> contentIds) {
        adminMapper.updateBoardsState(contentIds);
    }

    @Override
    public List<BoardLargeCategoryDto> findCategories() {
        return adminMapper.selectCategories();
    }

    @Override
    public int findAllBoardCount(String totalSelectValue, String smallSelectValue, String searchValue) {
        return adminMapper.selectAllBoardCount(totalSelectValue, smallSelectValue, searchValue);
    }

    @Override
    public List<BoardDto> findBoardWithPaging(int start, String sortValue, String totalSelectValue, String smallSelectValue, String searchValue) {
        return adminMapper.selectBoardWithPaging(start, sortValue, totalSelectValue, smallSelectValue, searchValue);
    }

    @Override
    public List<BoardDto> findBoardWithBoardId(int boardId) {
        return adminMapper.selectBoardWithBoardId(boardId);
    }

    @Override
    public int findAllReportBoardCount(String searchValue) {
        return adminMapper.selectAllReportBoardCount(searchValue);
    }

    @Override
    public List<BoardDto> findReportBoardWithPaging(int start, String sortValue, String searchValue) {
        return adminMapper.selectReportBoardWithPaging(start, sortValue, searchValue);
    }

    @Override
    public int findAllReportBoardCommentCount(String searchValue) {
        return adminMapper.selectAllReportBoardCommentCount(searchValue);
    }

    @Override
    public List<CommentDto> findReportBoardCommentWithPaging(int start, String sortValue, String searchValue) {
        return adminMapper.selectReportBoardCommentWithPaging(start, sortValue, searchValue);
    }

    @Override
    public int findAllCategoryCount(String categoryPart, int largeCategoryValue) {
        int count = 0;
        if (categoryPart.equals("large")) {
            count = adminMapper.selectAllLargeCategoryCount();
        }
        if (categoryPart.equals("small")) {
            count = adminMapper.selectAllSmallCategoryCount(largeCategoryValue);
        }

        return count;
    }

    @Override
    public List<BoardLargeCategoryDto> findLargeCategory(int start) {
        return adminMapper.selectLargeCategory(start);
    }

    @Override
    public List<BoardSmallCategoryDto> findSmallCategory(int start, int largeCategoryValue) {
        return adminMapper.selectSmallCategory(start, largeCategoryValue);
    }

    @Override
    public List<BoardLargeCategoryDto> findAllLargeCategory() {
        return adminMapper.selectAllLargeCategory();
    }

    @Override
    public void addCategory(String part, String categoryValue, String largeCategoryValue) {
        if (part.equals("large")) {
            adminMapper.insertLargeCategory(categoryValue);
        }
        if (part.equals("small")) {
            adminMapper.insertSmallCategory(categoryValue, largeCategoryValue);
        }
    }

    @Override
    public void modifyCategoryName(String categoryName, int categoryId, String categoryPart) {
        if (categoryPart.equals("large")) {
            adminMapper.updateLargeCategory(categoryName, categoryId);
        }
        if (categoryPart.equals("small")) {
            adminMapper.updateSmallCategory(categoryName, categoryId);
        }
    }

    @Override
    public void deleteCategory(String categoryPart, int categoryId) {
        if (categoryPart.equals("large")) {
            adminMapper.deleteLargeCategory(categoryId);
        }
        if (categoryPart.equals("small")) {
            adminMapper.deleteSmallCategory(categoryId);
        }
    }

}
