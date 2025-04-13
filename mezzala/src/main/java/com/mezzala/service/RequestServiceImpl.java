package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardSmallCategoryDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.RequestBoardDto;
import com.mezzala.mapper.NormalhubMapper;
import com.mezzala.mapper.RequestMapper;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestServiceImpl implements RequestService {
    @Setter
    private RequestMapper requestMapper;


    @Override
    public void addBoard(RequestBoardDto board) {
        requestMapper.insertBoard(board);
    }

    @Override
    public int findAllRequestBoardCount(String searchValue, String category) {
        return requestMapper.selectAllRequestBoardCount(searchValue, category);
    }

    @Override
    public List<BoardDto> findRequestBoardWithPaging(int start, String category, String searchValue, String sortValue) {
        return requestMapper.selectRequestBoardWithPaging(start, category, searchValue, sortValue);
    }

    @Override
    public List<RequestBoardDto> findRequestBoard(int boardNo, String sortValue, String category, String searchValue) {
        return requestMapper.selectRequestBoard(boardNo, sortValue, category, searchValue);
    }

    @Override
    public void addComment(String content, int boardId, String userId) {
        requestMapper.insertComment(content, boardId, userId);
    }

    @Override
    public void addRecomment(String content, int boardId, String userId, Integer parentId) {
        requestMapper.insertRecomment(content, boardId, userId, parentId);
    }

    @Override
    public List<CommentDto> findCommentsWithBoardId(int boardId, int start) {
        return requestMapper.selectCommentsWithBoardId(boardId, start);
    }

    @Override
    public void deleteCommentWithCommentIdAndBoardId(int commentId, int boardId) {
        requestMapper.deleteCommentWithCommentIdAndBoardId(commentId, boardId);
    }

    @Override
    public void modifyCommentWithCommentIdAndBoardId(int commentId, int boardId, String content) {
        Date now = new Date();
        Timestamp modifyDate = new Timestamp(now.getTime());
        requestMapper.updateCommentWithCommentIdAndBoardId(commentId, boardId, content, modifyDate);
    }

    @Override
    public int findAllRequestCommentCount(int boardId) {
        return requestMapper.selectAllRequestCommentCount(boardId);
    }
}
