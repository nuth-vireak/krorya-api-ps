package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.TagDTO;
import com.kshrd.krorya.model.entity.Tag;

import java.util.List;

public interface TagService {
    List<TagDTO> getTags();
}
