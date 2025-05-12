package com.mezzala.mapper;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.YoutubeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    List<BoardLargeCategoryDto> selectAllCategory();

    void insertBoard(BoardDto board);

    List<BoardDto> selectBoardWithPaging(@Param("start") int start, @Param("sortValue") String sortValue, @Param("userId") String userId);

    void insertBoardAttach(@Param("imageFiles") List<Map<String, String>> imageFiles, @Param("boardId") int boardId);

    int selectAllBoardCount(String userId, String searchValue);

    List<BoardDto> selectBoardWithPagingAndSearch(@Param("start") int start, @Param("sortValue") String sortValue, @Param("searchValue") String searchValue, @Param("userId") String userId);

    List<BoardDto> selectBoardWithBoardId(int boardId);

    void updateVisitedBoard(int boardId);

    List<BoardDto> selectBoardWithBoardNo(@Param("boardNo") int boardNo, @Param("userId") String userId,
                                          @Param("sortValue") String sortValue, @Param("searchValue") String searchValue);

    void insertUserAction(@Param("userId") String userId, @Param("boardId") int boardId, @Param("actionCategory") String actionCategory);

    void deleteUserAction(@Param("userId") String userId, @Param("boardId") int boardId, @Param("actionCategory") String actionCategory);

    void insertComment(@Param("content") String content, @Param("boardId") int boardId, @Param("userId") String userId);

    void insertRecomment(@Param("content") String content, @Param("boardId") int boardId, @Param("userId") String userId, @Param("parentId") int parentId);

    List<CommentDto> selectCommentsWithBoardId(@Param("boardId") int boardId, @Param("start") int start);

    void deleteCommentWithCommentIdAndBoardId(@Param("commentId") int commentId, @Param("boardId") int boardId);

    void updateCommentWithCommentIdAndBoardId(@Param("commentId") int commentId, @Param("boardId") int boardId, @Param("content") String content, @Param("modifyDate") Timestamp modifyDate);

    void insertCommentAction(@Param("commentId") int commentId, @Param("userId") String userId);

    List<CommentDto> selectCommentActions(@Param("boardId") int boardId, @Param("userId") String userId);

    void deleteCommentRecommendation(@Param("commentId") int commentId, @Param("userId") String userId);

    void deleteContent(int boardId);

    void modifyBoard(BoardDto board);

    void deleteBoardAttachAll(@Param("imageFiles") List<Map<String, String>> imageFiles, @Param("boardId") int boardId);

    List<BoardDto> selectHubBoard(@Param("boardNo") int boardNo, @Param("sortValue") String sortValue,
                                  @Param("category") String category, @Param("largeCategory") int largeCategory,
                                  @Param("userId") String userId, @Param("searchValue") String searchValue);

    int selectAllCommentCount(int boardId);

    int selectAllNoticeBoardCount();

    List<BoardDto> selectNoticeBoardWithPaging(int start);

    List<BoardDto> selectNoticeBoard(@Param("boardNo") int boardNo, @Param("fromPage") String fromPage);

    YoutubeDto selectYoutube();

    List<BoardDto> selectMypageBoard(@Param("boardNo") int boardNo, @Param("userId") String userId,
                                     @Param("tabNo") int tabNo);

    List<BoardDto> selectMypageCommentBoard(@Param("boardNo") int boardNo, @Param("userId") String userId,
                                            @Param("tabNo") int tabNo);
}
