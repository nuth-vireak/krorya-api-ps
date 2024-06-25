package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.FeedbackDTO;
import com.kshrd.krorya.model.dto.SimpleAppUserDTO;
import com.kshrd.krorya.model.entity.Feedback;
import com.kshrd.krorya.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FeedbackDTOConvertor {

    private ModelMapper modelMapper;
    private AppUserRepository appUserRepository;

    public FeedbackDTO entityToDTO(Feedback feedback) {
        FeedbackDTO feedbackDTO = modelMapper.map(feedback, FeedbackDTO.class);
        feedbackDTO.setCommentator(appUserRepository.getSimpleAppUserById(feedback.getCommentator()));
        Feedback reply = feedback.getReply();
        if (reply != null) {
            feedbackDTO.setReply(entityToDTO(reply));
        }
        return feedbackDTO;
    }

    public List<FeedbackDTO> entityListToDTOList(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

}
