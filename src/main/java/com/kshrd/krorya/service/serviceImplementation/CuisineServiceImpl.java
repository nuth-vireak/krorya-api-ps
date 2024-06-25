package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.CuisinesDTOConvertor;
import com.kshrd.krorya.model.dto.CuisinesDTO;
import com.kshrd.krorya.repository.CuisineRepository;
import com.kshrd.krorya.service.CuisineService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CuisineServiceImpl implements CuisineService {
    private final CuisineRepository cuisineRepository;
    private final CuisinesDTOConvertor cuisinesDTOConvertor;
    @Override
    public List<CuisinesDTO> getCuisines() {
        System.out.println(cuisinesDTOConvertor.toDTO(cuisineRepository.getCuisines()));
        return cuisinesDTOConvertor.toDTO(cuisineRepository.getCuisines());
    }
}
