package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.mapper.BoardMapper;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class BoardServiceImpl implements BoardService{
    @Setter
    BoardMapper boardMapper;

    @Override
    public List<BoardLargeCategoryDto> findAllCategory() {
        return boardMapper.selectAllCategory();
    }

    @Override
    public void addBoard(BoardDto board, List<Map<String, String>> imageFiles) {
        boardMapper.insertBoard(board);
        if (imageFiles != null && !imageFiles.isEmpty()) {
            int boardId = board.getBoardId();
            boardMapper.insertBoardAttach(imageFiles, boardId);
        }
    }

    @Override
    public List<BoardDto> findBoardWithPaging(int start) {
        List<BoardDto> boards = boardMapper.selectBoardWithPaging(start);
        for (BoardDto b : boards) {
            System.out.println("* :" + b);
        }
        return boards;
    }

    @Override
    public int findAllBoardCount() {
        return boardMapper.selectAllBoardCount();
    }

}
