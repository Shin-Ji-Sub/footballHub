package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardSmallCategoryDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.RequestBoardDto;

import java.util.List;

public interface RequestService {

    void addBoard(RequestBoardDto board);

    int findAllRequestBoardCount(String searchValue, String category);

    List<BoardDto> findRequestBoardWithPaging(int start, String category, String searchValue, String sortValue);

    List<RequestBoardDto> findRequestBoard(int boardNo, String sortValue, String category, String searchValue);

    void addComment(String content, int boardId, String userId);

    void addRecomment(String content, int boardId, String userId, Integer parentId);

    List<CommentDto> findCommentsWithBoardId(int boardId);

    void deleteCommentWithCommentIdAndBoardId(int commentId, int boardId);

    void modifyCommentWithCommentIdAndBoardId(int commentId, int boardId, String content);
}
