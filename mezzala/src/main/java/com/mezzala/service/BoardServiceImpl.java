package com.mezzala.service;

import com.mezzala.dto.*;
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
    public void modifyBoard(BoardDto board, List<Map<String, String>> imageFiles) {
        Date now = new Date();
        Timestamp modifyDate = new Timestamp(now.getTime());
        board.setRegDate(modifyDate);
        boardMapper.modifyBoard(board);

        int boardId = board.getBoardId();
        boardMapper.deleteBoardAttachAll(imageFiles, boardId);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            boardMapper.insertBoardAttach(imageFiles, boardId);
        }
    }

    @Override
    public List<BoardDto> findBoardWithPaging(int start, String sortValue, String searchValue, String userId) {
        List<BoardDto> boards = new ArrayList<>();

        if (searchValue.isEmpty()) {
            boards = boardMapper.selectBoardWithPaging(start, sortValue, userId);
        } else {
            boards = boardMapper.selectBoardWithPagingAndSearch(start, sortValue, searchValue, userId);
        }

//        for (BoardDto b : boards) {
//            System.out.println("* :" + b);
//        }
        return boards;
    }

    @Override
    public int findAllBoardCount(String userId, String searchValue) {
        return boardMapper.selectAllBoardCount(userId, searchValue);
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
    public List<BoardDto> findBoardWithBoardNo(int boardNo, String userId, String sortValue, String searchValue) {
        return boardMapper.selectBoardWithBoardNo(boardNo, userId, sortValue, searchValue);
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
    public List<CommentDto> findCommentsWithBoardId(int boardId, int start) {
        return boardMapper.selectCommentsWithBoardId(boardId, start);
    }

    @Override
    public void deleteCommentWithCommentIdAndBoardId(int commentId, int boardId) {
        boardMapper.deleteCommentWithCommentIdAndBoardId(commentId, boardId);
    }

    @Override
    public void modifyCommentWithCommentIdAndBoardId(int commentId, int boardId, String content) {
        Date now = new Date();
        Timestamp modifyDate = new Timestamp(now.getTime());
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

    @Override
    public void deleteContent(int boardId) {
        boardMapper.deleteContent(boardId);
    }

    @Override
    public List<BoardDto> findHubBoard(int boardNo, String sortValue, String category, int largeCategory, String userId, String searchValue) {
        return boardMapper.selectHubBoard(boardNo, sortValue, category, largeCategory, userId, searchValue);
    }

    @Override
    public int findAllCommentCount(int boardId) {
        return boardMapper.selectAllCommentCount(boardId);
    }

    @Override
    public int findAllNoticeBoardCount() {
        return boardMapper.selectAllNoticeBoardCount();
    }

    @Override
    public List<BoardDto> findNoticeBoardWithPaging(int start) {
        return boardMapper.selectNoticeBoardWithPaging(start);
    }

    @Override
    public List<BoardDto> findNoticeBoard(int boardNo, String fromPage) {
        return boardMapper.selectNoticeBoard(boardNo, fromPage);
    }

    @Override
    public YoutubeDto findYoutube() {
        return boardMapper.selectYoutube();
    }

    @Override
    public List<BoardDto> findMypageBoard(int boardNo, String userId, int tabNo) {
        List<BoardDto> boards = null;
        if (tabNo == 1 || tabNo == 3 || tabNo == 5) {
            // 작성한 글, 좋아요 한 글, 스크랩 한 글
            boards = boardMapper.selectMypageBoard(boardNo, userId, tabNo);
        } else {
            // 작성한 댓글, 좋아요 한 댓글
            boards = boardMapper.selectMypageCommentBoard(boardNo, userId, tabNo);
        }
        return boards;
    }

}
