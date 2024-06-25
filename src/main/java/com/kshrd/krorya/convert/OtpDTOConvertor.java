package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.OtpDTO;
import com.kshrd.krorya.model.entity.otp;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OtpDTOConvertor {
    private ModelMapper modelMapper;

    public OtpDTO toDTO(otp otp){
        return modelMapper.map(otp, OtpDTO.class);
    }
    public otp toEntity(OtpDTO dto){
        return modelMapper.map(dto, otp.class);
    }


}
