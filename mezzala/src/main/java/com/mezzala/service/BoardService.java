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

    List<BoardDto> findBoardWithPaging(int start, String sortValue, String searchValue, String userId);

    int findAllBoardCount(String userId, String searchValue);

    List<BoardDto> findBoardWithBoardId(int boardId);

    void incrementVisitedBoard(int boardId);

    List<BoardDto> findBoardWithBoardNo(int boardNo, String userId, String sortValue, String searchValue);

    void addUserAction(UserDto user, int boardId, String actionCategory);

    void removeUserAction(UserDto user, int boardId, String actionCategory);

    void addComment(String content, int boardId, String userId);

    List<CommentDto> findCommentsWithBoardId(int boardId, int start);

    void addRecomment(String content, int boardId, String userId, Integer parentId);

    void deleteCommentWithCommentIdAndBoardId(int commentId, int boardId);

    void modifyCommentWithCommentIdAndBoardId(int commentId, int boardId, String content);

    void addCommentAction(int commentId, String userId);

    List<CommentDto> findCommentActions(int boardId, String userId);

    void deleteCommentRecommendation(int commentId, String userId);

    void deleteContent(int boardId);

    void modifyBoard(BoardDto board, List<Map<String, String>> imageFiles);

    List<BoardDto> findHubBoard(int boardNo, String sortValue, String category, int largeCategory, String userId, String searchValue);

    int findAllCommentCount(int boardId);

    int findAllNoticeBoardCount();

    List<BoardDto> findNoticeBoardWithPaging(int start);

    List<BoardDto> findNoticeBoard(int boardNo, String fromPage);
}
