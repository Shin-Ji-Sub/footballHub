package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.UserDto;
import com.mezzala.mapper.BoardMapper;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BoardServiceImpl implements BoardService {
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
    public List<BoardDto> findBoardWithPaging(int start, String category, String searchValue) {
        List<BoardDto> boards = new ArrayList<>();

        if (searchValue.isEmpty()) {
            boards = boardMapper.selectBoardWithPaging(start, category);
        } else {
            boards = boardMapper.selectBoardWithPagingAndSearch(start, category, searchValue);
        }

        for (BoardDto b : boards) {
            System.out.println("* :" + b);
        }
        return boards;
    }

    @Override
    public int findAllBoardCount() {
        return boardMapper.selectAllBoardCount();
    }

    @Override
    public List<BoardDto> findBoardWithBoardId(int boardId) {
        return boardMapper.selectBoardWithBoardId(boardId);
    }

    @Override
    public void incrementVisitedBoard(int boardId) {
        boardMapper.updateVisitedBoard(boardId);
    }

    @Override
    public List<BoardDto> findBoardWithBoardNo(int boardNo) {
        return boardMapper.selectBoardWithBoardNo(boardNo);
    }

    @Override
    public void addUserAction(UserDto user, int boardId, String actionCategory) {
        boardMapper.insertUserAction(user.getUserId(), boardId, actionCategory);
    }

    @Override
    public void removeUserAction(UserDto user, int boardId, String actionCategory) {
        boardMapper.deleteUserAction(user.getUserId(), boardId, actionCategory);
    }

    @Override
    public void addComment(String content, int boardId, String userId) {
        boardMapper.insertComment(content, boardId, userId);
    }

    @Override
    public void addRecomment(String content, int boardId, String userId, Integer parentId) {
        boardMapper.insertRecomment(content, boardId, userId, parentId);
    }

    @Override
    public List<CommentDto> findCommentsWithBoardId(int boardId) {
        return boardMapper.selectCommentsWithBoardId(boardId);
    }

    @Override
    public void deleteCommentWithCommentIdAndBoardId(int commentId, int boardId) {
        boardMapper.deleteCommentWithCommentIdAndBoardId(commentId, boardId);
    }

    @Override
    public void modifyCommentWithCommentIdAndBoardId(int commentId, int boardId, String content) {
        Date now = new Date();
        Timestamp modifyDate = new Timestamp(now.getTime());
        System.out.println("modifyDate : " + modifyDate);
        boardMapper.updateCommentWithCommentIdAndBoardId(commentId, boardId, content, modifyDate);
    }

    @Override
    public void addCommentAction(int commentId, String userId) {
        boardMapper.insertCommentAction(commentId, userId);
    }

    @Override
    public List<CommentDto> findCommentActions(int boardId, String userId) {
        return boardMapper.selectCommentActions(boardId, userId);
    }

    @Override
    public void deleteCommentRecommendation(int commentId, String userId) {
        boardMapper.deleteCommentRecommendation(commentId, userId);
    }

}
