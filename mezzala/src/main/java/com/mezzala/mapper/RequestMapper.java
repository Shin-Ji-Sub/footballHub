package com.mezzala.mapper;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardSmallCategoryDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.RequestBoardDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface RequestMapper {

    void insertBoard(RequestBoardDto board);

    int selectAllRequestBoardCount(@Param("searchValue") String searchValue, @Param("category") String category);

    List<BoardDto> selectRequestBoardWithPaging(@Param("start") int start, @Param("category") String category,
                                                @Param("searchValue") String searchValue, @Param("sortValue") String sortValue);

    List<RequestBoardDto> selectRequestBoard(@Param("boardNo") int boardNo, @Param("sortValue") String sortValue,
                                      @Param("category") String category, @Param("searchValue") String searchValue);

    void insertComment(@Param("content") String content, @Param("boardId") int boardId, @Param("userId") String userId);

    void insertRecomment(@Param("content") String content, @Param("boardId") int boardId,
                         @Param("userId") String userId, @Param("parentId") Integer parentId);

    List<CommentDto> selectCommentsWithBoardId(@Param("boardId") int boardId, @Param("start") int start);

    void deleteCommentWithCommentIdAndBoardId(@Param("commentId") int commentId, @Param("boardId") int boardId);

    void updateCommentWithCommentIdAndBoardId(@Param("commentId") int commentId, @Param("boardId") int boardId,
                                              @Param("content") String content, @Param("modifyDate") Timestamp modifyDate);

    int selectAllRequestCommentCount(int boardId);
}
