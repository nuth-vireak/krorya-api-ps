package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.ReportDTO;
import com.kshrd.krorya.model.entity.Report;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ReportDTOConvertor {
    private final ModelMapper modelMapper;

    public ReportDTO toDto(Report report){
        return modelMapper.map(report, ReportDTO.class);
    }

    public List<ReportDTO> toListDto(List<Report> reports){
        return reports.stream().map(this::toDto).collect(Collectors.toList());
    }

}
