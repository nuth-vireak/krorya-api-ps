package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.ReportDTO;
import com.kshrd.krorya.model.request.ReportOtherUserRequest;
import com.kshrd.krorya.model.request.ReportRecipeRequest;

import java.util.List;
import java.util.UUID;

public interface ReportService {
    ReportDTO createRecipeReport(ReportRecipeRequest reportRecipeRequest);

    ReportDTO getReportedRecipeByRecipeReportId(UUID reportId);

    List<ReportDTO> getAllReportedRecipes();

    void deleteReportedRecipeByReportId(UUID reportId);

    ReportDTO createOtherUserReport(ReportOtherUserRequest reportOtherUserRequest);

    List<ReportDTO> getAllReportedUser();

    ReportDTO getReportedUserByUserReportId(UUID reportedUserId);
    void banReportedUserAndDeleteReportedUserByUserReportId(UUID userReportId);
}
