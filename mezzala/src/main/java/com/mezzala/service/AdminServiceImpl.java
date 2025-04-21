package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.UserDto;
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
    public List<BoardDto> findBoardWithBoardId(int boardId) {
        return adminMapper.selectBoardWithBoardId(boardId);
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
    public int findAllBoardCount(String totalSelectValue, String smallSelectValue) {
        return adminMapper.selectAllBoardCount(totalSelectValue, smallSelectValue);
    }

    @Override
    public List<BoardDto> findBoardWithPaging(int start, String sortValue, String totalSelectValue, String smallSelectValue) {
        return adminMapper.selectBoardWithPaging(start, sortValue, totalSelectValue, smallSelectValue);
    }
}
