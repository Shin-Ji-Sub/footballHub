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
    public int findAllBoardCount(String searchValue, boolean state) {
        return adminMapper.selectAllBoardCount(searchValue, state);
    }

    @Override
    public List<BoardDto> findBoardWithPaging(int start, String searchValue, boolean state) {
        int limit = state ? 5 : 10;
        return adminMapper.selectBoardWithPaging(start, searchValue, state);
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
}
