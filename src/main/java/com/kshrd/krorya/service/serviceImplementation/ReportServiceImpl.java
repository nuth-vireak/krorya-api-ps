package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.ReportDTOConvertor;
import com.kshrd.krorya.exception.CustomBadRequestException;
import com.kshrd.krorya.exception.SearchNotFoundException;
import com.kshrd.krorya.model.dto.ReportDTO;
import com.kshrd.krorya.model.entity.*;
import com.kshrd.krorya.model.request.ReportOtherUserRequest;
import com.kshrd.krorya.model.request.ReportRecipeRequest;
import com.kshrd.krorya.repository.AppUserRepository;
import com.kshrd.krorya.repository.RecipeRepository;
import com.kshrd.krorya.repository.ReportRepository;
import com.kshrd.krorya.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final RecipeRepository recipeRepository;
    private final AppUserRepository appUserRepository;
    private final ReportDTOConvertor reportDTOConvertor;

    @Override
    public ReportDTO createRecipeReport(ReportRecipeRequest reportRecipeRequest) {
        UUID currentUserId = getCurrentUserId();
        Recipe recipe = recipeRepository.getRecipeById(reportRecipeRequest.getRecipeId());
        if (recipe == null){
            throw new SearchNotFoundException("Recipe with ID " + reportRecipeRequest.getRecipeId() + " does not exist");
        }
        UUID recipeId = reportRecipeRequest.getRecipeId();
        AppUser recipeOwner = reportRepository.getRecipeOwnerId(recipeId);
        UUID recipeOwnerId = recipeOwner.getUserId();
        System.out.println(currentUserId.equals(recipeOwnerId));
        if (currentUserId.equals(recipeOwnerId)){
            throw new CustomBadRequestException("This recipe is yours. You cannot report your own recipe");
        }
        Report existingRecipeReportByUser = reportRepository.findReportByUserIdAndRecipeId(currentUserId, recipeId);
        if (existingRecipeReportByUser != null) {
            throw new CustomBadRequestException("You have already reported this recipe, cannot report again.");
        }
        return reportDTOConvertor.toDto(reportRepository.createRecipeReport(reportRecipeRequest, currentUserId));
    }

    @Override
    public ReportDTO getReportedRecipeByRecipeReportId(UUID reportId) {
        Report report = reportRepository.getReportedRecipeByRecipeReportId(reportId);
        if (report == null){
            throw new SearchNotFoundException("The recipe report with ID " + reportId + " does not exist");
        }
        return reportDTOConvertor.toDto(report);
    }

    @Override
    public List<ReportDTO> getAllReportedRecipes() {
        List<Report> reportList = reportRepository.getAllReportedRecipes();
        if (reportList.isEmpty()){
            throw new SearchNotFoundException("List of reported recipe is empty");
        }
        return reportDTOConvertor.toListDto(reportList);
    }

    @Override
    public void deleteReportedRecipeByReportId(UUID reportId) {
        Report reportToDelete = reportRepository.getReportedRecipeByRecipeReportId(reportId);
        if (reportToDelete == null){
            throw new SearchNotFoundException("The recipe report with ID " + reportId + " does not exist");
        }
        UUID recipeIdToDelete = reportToDelete.getRecipe().getRecipeId();
        reportRepository.deleteReportedRecipe(reportId, recipeIdToDelete);
    }

////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ReportDTO createOtherUserReport(ReportOtherUserRequest reportOtherUserRequest) {
        UUID currentUserId = getCurrentUserId();
        AppUser otherUser = appUserRepository.getUserById(reportOtherUserRequest.getReporteeId());
        if (otherUser == null){
            throw new SearchNotFoundException("User with ID " + reportOtherUserRequest.getReporteeId() + " does not exist.");
        }
        UUID reporteeId = reportOtherUserRequest.getReporteeId();
        UUID profileOwnerId = reportRepository.getProfileOwnerId(reporteeId);
        System.out.println(currentUserId.equals(profileOwnerId));
        if (currentUserId.equals(profileOwnerId)){
            throw new CustomBadRequestException("You are the owner of this profile. You cannot report yourself.");
        }
        Report existingReportByCurrentUserAndReportee = reportRepository.findReportByCurrentUserIdAndReporteeId(currentUserId, reporteeId);
        if (existingReportByCurrentUserAndReportee != null) {
            throw new CustomBadRequestException("You have already reported this user, cannot report again.");
        }
        return reportDTOConvertor.toDto(reportRepository.createOtherUserReport(reportOtherUserRequest, currentUserId));
    }

    @Override
    public List<ReportDTO> getAllReportedUser() {
        List<Report> reportList = reportRepository.getAllReportedUser();
        if (reportList.isEmpty()){
            throw new SearchNotFoundException("List of reported user is empty");
        }
        return reportDTOConvertor.toListDto(reportList);
    }

    @Override
    public ReportDTO getReportedUserByUserReportId(UUID reportedUserId) {
        Report report = reportRepository.getReportedUserByUserReportId(reportedUserId);
        if (report == null){
            throw new SearchNotFoundException("The user report with ID " + reportedUserId + " does not exist");
        }
        return reportDTOConvertor.toDto(report);
    }

    @Override
    public void banReportedUserAndDeleteReportedUserByUserReportId(UUID userReportId) {
        Report reportToDelete = reportRepository.getReportedUserByUserReportId(userReportId);
        if (reportToDelete == null){
            throw new SearchNotFoundException("The user report with ID " + userReportId + " does not exist");
        }
        UUID reporteeId = reportToDelete.getReporteeInfo().getUserId();
        reportRepository.banReportedUser(reporteeId);
        reportRepository.deleteReportedUserReport(userReportId);
    }

    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }
}
