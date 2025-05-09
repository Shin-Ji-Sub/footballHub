package com.mezzala.service;

import com.mezzala.dto.*;
import com.mezzala.mapper.AdminMapper;
import com.mezzala.mapper.BoardMapper;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<BoardDto> findNoticeBoardWithBoardId(int boardId) {
        return adminMapper.selectNoticeBoardWithBoardId(boardId);
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
    public int findAllBoardCount(String totalSelectValue, String smallSelectValue, String searchValue) {
        return adminMapper.selectAllBoardCount(totalSelectValue, smallSelectValue, searchValue);
    }

    @Override
    public List<BoardDto> findBoardWithPaging(int start, String sortValue, String totalSelectValue, String smallSelectValue, String searchValue) {
        return adminMapper.selectBoardWithPaging(start, sortValue, totalSelectValue, smallSelectValue, searchValue);
    }

    @Override
    public List<BoardDto> findBoardWithBoardId(int boardId) {
        return adminMapper.selectBoardWithBoardId(boardId);
    }

    @Override
    public int findAllReportBoardCount(String searchValue) {
        return adminMapper.selectAllReportBoardCount(searchValue);
    }

    @Override
    public List<BoardDto> findReportBoardWithPaging(int start, String sortValue, String searchValue) {
        return adminMapper.selectReportBoardWithPaging(start, sortValue, searchValue);
    }

    @Override
    public int findAllReportBoardCommentCount(String searchValue) {
        return adminMapper.selectAllReportBoardCommentCount(searchValue);
    }

    @Override
    public List<CommentDto> findReportBoardCommentWithPaging(int start, String sortValue, String searchValue) {
        return adminMapper.selectReportBoardCommentWithPaging(start, sortValue, searchValue);
    }

    @Override
    public int findAllCategoryCount(String categoryPart, int largeCategoryValue) {
        int count = 0;
        if (categoryPart.equals("large")) {
            count = adminMapper.selectAllLargeCategoryCount();
        }
        if (categoryPart.equals("small")) {
            count = adminMapper.selectAllSmallCategoryCount(largeCategoryValue);
        }

        return count;
    }

    @Override
    public List<BoardLargeCategoryDto> findLargeCategory(int start) {
        return adminMapper.selectLargeCategory(start);
    }

    @Override
    public List<BoardSmallCategoryDto> findSmallCategory(int start, int largeCategoryValue) {
        return adminMapper.selectSmallCategory(start, largeCategoryValue);
    }

    @Override
    public List<BoardLargeCategoryDto> findAllLargeCategory() {
        return adminMapper.selectAllLargeCategory();
    }

    @Override
    public void addCategory(String part, String categoryValue, String largeCategoryValue) {
        if (part.equals("large")) {
            adminMapper.insertLargeCategory(categoryValue);
        }
        if (part.equals("small")) {
            adminMapper.insertSmallCategory(categoryValue, largeCategoryValue);
        }
    }

    @Override
    public void modifyCategoryName(String categoryName, int categoryId, String categoryPart) {
        if (categoryPart.equals("large")) {
            adminMapper.updateLargeCategory(categoryName, categoryId);
        }
        if (categoryPart.equals("small")) {
            adminMapper.updateSmallCategory(categoryName, categoryId);
        }
    }

    @Override
    public void deleteCategory(String categoryPart, int categoryId) {
        if (categoryPart.equals("large")) {
            adminMapper.deleteLargeCategory(categoryId);
        }
        if (categoryPart.equals("small")) {
            adminMapper.deleteSmallCategory(categoryId);
        }
    }

    @Override
    public List<UserRoleDto> findAllUserRole() {
        return adminMapper.selectAllUserRole();
    }

    @Override
    public int findAllUserCount(String category, String searchValue) {
        return adminMapper.selectAllUserCount(category, searchValue);
    }

    @Override
    public List<UserDto> findUserList(int start, String category, String searchValue, String sortValue) {
        return adminMapper.selectUserList(start, category, searchValue, sortValue);
    }

    @Override
    public void modifyUserRole(int roleValue, String userId) {
        adminMapper.updateUserRole(roleValue, userId);
    }

    @Override
    public void addScheduleCategory(String value, String logo, String from, String competitionCategory) {
        if (from.equals("competition")) {
            adminMapper.insertCompetition(value, competitionCategory);
        }
        if (from.equals("round")) {
            adminMapper.insertCompetitionRound(value);
        }
        if (from.equals("team")) {
            adminMapper.insertTeam(value, logo);
        }
    }

    @Override
    public List<TeamDto> findAllTeam() {
        return adminMapper.selectAllTeam();
    }

    @Override
    public List<CompetitionDto> findAllCompetition() {
        return adminMapper.selectAllCompetition();
    }

    @Override
    public List<CompetitionRoundDto> findAllCompetitionRound() {
        return adminMapper.selectAllCompetitionRound();
    }

    @Override
    public void addSchedule(ScheduleDto schedule) {
        adminMapper.insertSchedule(schedule);
    }

    @Override
    public int findAllCompetitionCount(String searchValue) {
        return adminMapper.selectAllCompetitionCount(searchValue);
    }

    @Override
    public List<CompetitionDto> findCompetition(int start, String searchValue) {
        return adminMapper.selectCompetition(start, searchValue);
    }

    @Override
    public int findAllCompetitionRoundCount(String searchValue) {
        return adminMapper.selectAllCompetitionRoundCount(searchValue);
    }

    @Override
    public List<CompetitionRoundDto> findCompetitionRound(int start, String searchValue) {
        return adminMapper.selectCompetitionRound(start, searchValue);
    }

    @Override
    public int findAllTeamCount(String searchValue) {
        return adminMapper.selectAllTeamCount(searchValue);
    }

    @Override
    public List<TeamDto> findTeam(int start, String searchValue) {
        return adminMapper.selectTeam(start, searchValue);
    }

    @Override
    public void modifyNameAndLogo(int id, String name, String logo, String category, String competitionCategory) {
        if (category.equals("competition")) {
            adminMapper.updateCompetitionName(id, name, competitionCategory);
        }
        if (category.equals("round")) {
            adminMapper.updateCompetitionRoundName(id, name);
        }
        if (category.equals("team")) {
            adminMapper.updateTeam(id, name, logo);
        }
    }

    @Override
    public void deleteTeam(int id, String category) {
        adminMapper.deleteTeam(id, category);
    }

    @Override
    public int findAllScheduleCount(String category, int year, int month, int day) {
        LocalDate fromDate = LocalDate.of(year, month, day);
        LocalDate tillDate = fromDate.plusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDay = fromDate.format(formatter);
        String tillDay = tillDate.format(formatter);
        return adminMapper.selectAllScheduleCount(category, fromDay, tillDay);
    }

    @Override
    public Map<Integer, List<ScheduleDto>> findSchedule(int start, String category, int year, int month, int day) {
        LocalDate fromDate = LocalDate.of(year, month, day);
        LocalDate tillDate = fromDate.plusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDay = fromDate.format(formatter);
        String tillDay = tillDate.format(formatter);

        List<ScheduleDto> schedules = adminMapper.selectSchedule(start, category, fromDay, tillDay);

        Map<Integer, List<ScheduleDto>> groupedSchedules = schedules.stream()
                .collect(Collectors.groupingBy(ScheduleDto::getCompetitionId));

//        for (Map.Entry<Integer, List<ScheduleDto>> entry : groupedSchedules.entrySet()) {
//            System.out.println("\uD83C\uDFC6 : " + entry.getValue());
//            Integer competitionId = entry.getKey();
//            List<ScheduleDto> scheduleList = entry.getValue();
//
//            System.out.println("🏆 대회 ID: " + competitionId);
//            for (ScheduleDto schedule : scheduleList) {
//                System.out.println(" - 경기 ID: " + schedule.getScheduleId() + ", 시간: " + schedule.getScheduleDate());
//            }
//        }

        return groupedSchedules;
    }

    @Override
    public void modifySchedule(ScheduleDto schedule, int year, int month, int day, int hour, int minute) {
        LocalDateTime date = LocalDateTime.of(year, month, day, hour, minute);
        adminMapper.updateSchedule(schedule, date);
    }

    @Override
    public void removeSchedule(int scheduleId) {
        adminMapper.deleteSchedule(scheduleId);
    }

    @Override
    public void addRanking(int seasonValue, int competitionId, int homeId, int homeScore, int awayId, int awayScore) {
        String homeResult = "";
        String awayResult = "";
        if (homeScore > awayScore) {
            homeResult = "win";
            awayResult = "lose";
        } else if (awayScore > homeScore) {
            homeResult = "lose";
            awayResult = "win";
        } else {
            homeResult = "draw";
            awayResult = "draw";
        }

        RankingDto homeRanking = adminMapper.selectRankingWithSeasonAndTeamId(seasonValue, homeId);
        RankingDto awayRanking = adminMapper.selectRankingWithSeasonAndTeamId(seasonValue, awayId);

        if (homeRanking == null) {
            adminMapper.insertRanking(seasonValue, competitionId, homeId, homeScore, awayScore, homeResult);
        } else {
            if (homeResult.equals("win")) {
                homeRanking.setWin(homeRanking.getWin() + 1);
            }
            if (homeResult.equals("draw")) {
                homeRanking.setDraw(homeRanking.getDraw() + 1);
            }
            if (homeResult.equals("lose")) {
                homeRanking.setLose(homeRanking.getLose() + 1);
            }
            homeRanking.setScorePoint(homeRanking.getScorePoint() + homeScore);
            homeRanking.setLosePoint(homeRanking.getLosePoint() + awayScore);
            System.out.println("[HOME] : " + homeRanking);
            adminMapper.updateRanking(homeRanking);
        }

        if (awayRanking == null) {
            adminMapper.insertRanking(seasonValue, competitionId, awayId, awayScore, homeScore, awayResult);
        } else {
            if (awayResult.equals("win")) {
                awayRanking.setWin(awayRanking.getWin() + 1);
            }
            if (awayResult.equals("draw")) {
                awayRanking.setDraw(awayRanking.getDraw() + 1);
            }
            if (awayResult.equals("lose")) {
                awayRanking.setLose(awayRanking.getLose() + 1);
            }
            awayRanking.setScorePoint(awayRanking.getScorePoint() + awayScore);
            awayRanking.setLosePoint(awayRanking.getLosePoint() + homeScore);
            System.out.println("[AWAY] : " + awayRanking);
            adminMapper.updateRanking(awayRanking);
        }

    }

    @Override
    public void addCompetitionLeague(int competitionId, int clFrom, int clTo, int olFrom, int olTo) {
        adminMapper.updateCompetitionLeague(competitionId, clFrom, clTo, olFrom, olTo);
    }

    @Override
    public List<RankingDto> findRanking(int competitionValue, int seasonValue) {
        return adminMapper.selectRanking(competitionValue, seasonValue);
    }

    @Override
    public void modifyRanking(RankingDto ranking) {
        adminMapper.updateRanking(ranking);
    }

    @Override
    public YoutubeDto findYoutube() {
        return adminMapper.selectYoutube();
    }

    @Override
    public void modifyYoutube(String part, String value, int youtubeId) {
        if (youtubeId == 0) {
            adminMapper.insertYoutube(part, value);
        } else {
            adminMapper.updateYoutube(part, value, youtubeId);
        }
    }

}
