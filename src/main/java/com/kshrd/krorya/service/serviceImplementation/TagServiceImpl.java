package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.TagDTOConvertor;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.model.dto.TagDTO;
import com.kshrd.krorya.model.entity.Tag;
import com.kshrd.krorya.repository.TagRepository;
import com.kshrd.krorya.service.TagService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {
    private static final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);
    private TagRepository tagRepository;
    private final TagDTOConvertor tagDTOConvertor;

    @Override
    public List<TagDTO> getTags() {
        log.info("Total Tags : {}",tagRepository.getTags());
        if (tagRepository.getTags() == null) {
            throw new CustomNotFoundException("Tags not found");
        }
        List<TagDTO> tagDTOList = tagDTOConvertor.toListDTO(tagRepository.getTags());
        return tagDTOList;
    }
}
