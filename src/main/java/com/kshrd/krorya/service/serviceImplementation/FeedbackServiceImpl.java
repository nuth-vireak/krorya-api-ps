package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.FeedbackDTOConvertor;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.exception.ForbiddenException;
import com.kshrd.krorya.model.dto.FeedbackDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.entity.Feedback;
import com.kshrd.krorya.model.request.FeedbackRequest;
import com.kshrd.krorya.repository.AppUserRepository;
import com.kshrd.krorya.repository.FeedbackRepository;
import com.kshrd.krorya.repository.FoodRepository;
import com.kshrd.krorya.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger log = LoggerFactory.getLogger(FollowServiceImpl.class);

    private final FeedbackRepository feedbackRepository;
    private final FeedbackDTOConvertor feedbackDTOConvertor;
    private final AppUserRepository appUserRepository;
    private final FoodRepository foodRepository;

    @Override
    public FeedbackDTO insertFeedback(FeedbackRequest feedbackRequest) {

        // Ensure parentId is null when inserting feedback
        if (feedbackRequest.getParentId() != null) {
            throw new IllegalArgumentException("Parent ID must be null when inserting root feedback");
        }

        // check if the food id is not exist
        if (!feedbackRepository.isFoodExist(feedbackRequest.getFoodId())) {
            throw new CustomNotFoundException("Food not found");
        }

        // Ensure that the food owner cannot insert feedback on their own food
        UUID foodOwnerId = feedbackRepository.isFoodOwner(feedbackRequest.getFoodId(), getUsernameOfCurrentUser());
        log.info("Food Owner ID: {}", foodOwnerId);
        log.info("Current User ID: {}", getUsernameOfCurrentUser());
        if (foodOwnerId != null && foodOwnerId.equals(getUsernameOfCurrentUser())) {
            throw new ForbiddenException("Food owner cannot insert feedback on their own food");
        }


        Feedback feedback = Feedback.builder()
                .foodId(feedbackRequest.getFoodId())
                .commentator(getUsernameOfCurrentUser())
                .comment(feedbackRequest.getComment())
                .build();

        Feedback insertedFeedback = feedbackRepository.insertFeedback(feedback);
        return feedbackDTOConvertor.entityToDTO(insertedFeedback);
    }

    @Override
    public FeedbackDTO replyToFeedback(FeedbackRequest feedbackRequest) {
        UUID currentUserId = getUsernameOfCurrentUser();

        // Check if the food ID exists
        if (!feedbackRepository.isFoodExist(feedbackRequest.getFoodId())) {
            log.error("Food not found: {}", feedbackRequest.getFoodId());
            throw new CustomNotFoundException("Food not found");
        }

        // Check if the parent feedback exists
        Feedback parentFeedback = feedbackRepository.findById(feedbackRequest.getParentId());
        if (parentFeedback == null) {
            log.error("Parent feedback not found: {}", feedbackRequest.getParentId());
            throw new CustomNotFoundException("Parent feedback does not exist");
        }
        log.info("Parent feedback found: {}", parentFeedback);

        // Ensure the parent feedback is a root feedback
        if (parentFeedback.getParentId() != null) {
            log.error("Parent feedback is not a root feedback. Parent ID: {}", parentFeedback.getParentId());
            throw new ForbiddenException("Cannot reply to a reply feedback");
        }
        log.info("Parent feedback is a root feedback. Parent ID: {}", parentFeedback.getParentId());

        // Ensure only the food owner can reply to the feedback
        UUID foodOwnerId = feedbackRepository.isFoodOwner(feedbackRequest.getFoodId(), currentUserId);
        log.info("Current User ID: {}", currentUserId);
        log.info("Food Owner ID: {}", foodOwnerId);
        if (foodOwnerId == null || !foodOwnerId.equals(currentUserId)) {
            log.error("Current user is not the owner of the food. Current User ID: {}, Food Owner ID: {}", currentUserId, foodOwnerId);
            throw new ForbiddenException("Only the owner of the food can reply to the feedback");
        }

        // Ensure only one reply is allowed for each feedback
        int replyCount = feedbackRepository.countReply(feedbackRequest.getParentId());
        if (replyCount > 0) {
            log.error("Reply already exists for this feedback. Parent Feedback ID: {}", feedbackRequest.getParentId());
            throw new ForbiddenException("Only one reply is allowed for each feedback");
        }

        // food owner cannot reply to his/her own feedback
        UUID parentIdByFeebackId =  feedbackRepository.findParentIdByFeedbackId(feedbackRequest.getParentId());
        log.info("Parent ID by Feedback ID: {}", parentIdByFeebackId);
        // if parentIdByFeebackId is null, it means the parent feedback is a root feedback
        if (parentIdByFeebackId != null) {
            log.error("Parent feedback is a root feedback. Parent ID: {}", feedbackRequest.getParentId());
            throw new ForbiddenException("Food owner cannot reply to his/her own feedback");
        }

        Feedback feedback = Feedback.builder()
                .parentId(feedbackRequest.getParentId())
                .foodId(feedbackRequest.getFoodId())
                .commentator(currentUserId)
                .comment(feedbackRequest.getComment())
                .build();

        Feedback insertedFeedback = feedbackRepository.insertFeedback(feedback);
        return feedbackDTOConvertor.entityToDTO(insertedFeedback);
    }


    @Override
    public List<FeedbackDTO> getFeedbacksByFoodId(UUID foodId) {
        List<Feedback> feedbacks = feedbackRepository.getFeedbacksByFoodId(foodId);

        if (feedbacks.isEmpty()) {
            throw new CustomNotFoundException("No feedbacks found for this food");
        }

        return feedbackDTOConvertor.entityListToDTOList(feedbacks);
    }


    UUID getUsernameOfCurrentUser() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = userDetails.getAppUser();
        return appUser.getUserId();
    }
}
