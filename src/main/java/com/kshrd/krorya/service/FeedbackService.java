package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.FeedbackDTO;
import com.kshrd.krorya.model.request.FeedbackRequest;

import java.util.List;
import java.util.UUID;

public interface FeedbackService {
    FeedbackDTO insertFeedback(FeedbackRequest feedbackRequest);
    FeedbackDTO replyToFeedback(FeedbackRequest feedbackRequest);
    List<FeedbackDTO> getFeedbacksByFoodId(UUID foodId);
}
