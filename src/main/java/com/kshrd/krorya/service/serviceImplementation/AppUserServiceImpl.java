package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.AppUserConvertor;
import com.kshrd.krorya.exception.CustomBadRequestException;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.exception.DuplicatedException;
//import com.kshrd.krorya.exception.DuplicatedEmailException;
import com.kshrd.krorya.exception.DuplicatedException;
import com.kshrd.krorya.exception.EmailAlreadyExistsException;
import com.kshrd.krorya.exception.SearchNotFoundException;
import com.kshrd.krorya.model.dto.AppUserDTO;
import com.kshrd.krorya.model.dto.OtpDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.entity.Follow;
import com.kshrd.krorya.model.request.AppUserRequest;
import com.kshrd.krorya.model.request.GoogleUserRequest;
import com.kshrd.krorya.model.request.ResetPasswordRequest;
import com.kshrd.krorya.model.request.UserRequest;
import com.kshrd.krorya.repository.AppUserRepository;
import com.kshrd.krorya.repository.FollowRepository;
import com.kshrd.krorya.service.AppUserService;
import com.kshrd.krorya.service.EmailService;
import com.kshrd.krorya.service.OtpService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserConvertor appUserConvertor;
    private final OtpService otpService;
    private final EmailService emailService;
    private final FollowRepository followRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(username);
        log.info("loadUserByUsername 1 : {}",user);
        return new CustomUserDetail(user);

    }

    @Override
    public CustomUserDetail createUserFromGoogle(GoogleUserRequest googleUserRequest) {
        googleUserRequest.setPassword(passwordEncoder.encode(googleUserRequest.getPassword()));
        // Check if user already exists by email
        if (appUserRepository.findByEmail(googleUserRequest.getEmail()) != null){
            throw new DuplicatedException("Email already exists");
        }
//        log.info("createUserFromGoogle : {}",appUserRepository.registerWithGoogle(googleUserRequest));
        return appUserConvertor.toCustomUserDetail(appUserRepository.registerWithGoogle(googleUserRequest));
    }

    @Override
    public AppUserDTO updateUserById(UUID userId, UserRequest userRequest) {
        return appUserConvertor.toDTO(appUserRepository.updateUserById(userId, userRequest));
    }

    @Override
    public void changeUserPassword(ResetPasswordRequest resetPasswordRequest, UUID userId) {
        AppUser appUser = appUserRepository.getUserById(userId);
        boolean isMatch = passwordEncoder.matches(resetPasswordRequest.getOldPassword(), appUser.getPassword());
        System.out.println(isMatch);
        if (!isMatch){
            throw new CustomBadRequestException("The old password is invalid!");
        }
        String newPassword = passwordEncoder.encode(resetPasswordRequest.getNewPassword());
        appUserRepository.resetPassword(newPassword, userId);
    }

    @Override
    public AppUserDTO findUserByUserId(UUID currentUserId, UUID userId) {
        AppUser appUser = appUserRepository.getUserById(userId);
        if(appUser == null){
            throw new CustomNotFoundException("user id "+ userId +" doesn't exist");
        }
        AppUserDTO appUserDTO = appUserConvertor.toDTO(appUser);
//        Follow follow = appUserRepository.getFollowingByFollower(currentUserId, userId);
        System.out.println("Current user id (before) : " + currentUserId);
        if (currentUserId == null){
//            Follow follow = appUserRepository.getFollowingByFollower(currentUserId, userId);
                appUserDTO.setIsFollowingByCurrentUser(false);
        }
        else{
            Follow follow = followRepository.getFollowingByFollower(currentUserId, userId);
            System.out.printf("My following: "+follow);
            if (follow != null){
                appUserDTO.setIsFollowingByCurrentUser(true);
            }
//            else {
//                appUserDTO.setIsFollowingByCurrentUser(false);
//            }
        }

        System.out.println("Current user id (after) : " + currentUserId);




        return appUserDTO;
    }

    @Override
    public AppUserDTO getCurrentUserInfo() {
        UUID currentUserId = getCurrentUserId();
        System.out.println("Current user id : " + currentUserId);
        return appUserConvertor.toDTO(appUserRepository.getUserById(currentUserId));
    }

    @Override
    public List<AppUserDTO> getAllUsers() {
        return appUserConvertor.toListDTO(appUserRepository.findAllUsers());
    }

    @Override
    public AppUser findUserByEmail(String email) {
        AppUser appUserRepositoryByEmail = appUserRepository.findByEmail(email);

        if (appUserRepositoryByEmail == null) {
            throw new SearchNotFoundException("Email: " + email + " not found");
        }

        return appUserRepositoryByEmail;
    }


//    @Override
//    public AppUserDTO createUser(AppUserRequest appUserRequest) {
//        System.out.println(appUserRequest + " request user");
//        System.out.println(findUserByEmail(appUserRequest.getEmail()));
//        if (findUserByEmail(appUserRequest.getEmail()) != null) {
//            throw new EmailAlreadyExistsException("Email already exists");
//        }
//        String encodedPassword = passwordEncoder.encode(appUserRequest.getPassword());
//        appUserRequest.setPassword(encodedPassword);
//        log.info("Created new AppUser: {}", appUserRequest);
//        AppUser appUser = appUserRepository.saveUser(appUserRequest);
//        System.out.println(appUser  + " after create new user");
//        return appUserConvertor.toDTO(appUser);
//    }

    @Override
    public AppUserDTO createUser(AppUserRequest appUserRequest) throws MessagingException, IOException {

        String email = appUserRequest.getEmail();

        if (appUserRepository.findByEmail(email) != null){
            throw new DuplicatedException("Email already exists");
        }
        if (!appUserRequest.getPassword().equals(appUserRequest.getConfirmPassword())){
            throw new CustomBadRequestException("Password and confirm password do not match");
        }

        String encodedPassword = passwordEncoder.encode(appUserRequest.getPassword());
        appUserRequest.setPassword(encodedPassword);
        AppUser appUser = appUserRepository.saveUser(appUserRequest);

        AppUserDTO appUserDTO = appUserConvertor.toDTO(appUser);

        OtpDTO otpDTO = otpService.generateOtp(6, appUserConvertor.toEntity(appUserDTO));
        otpService.save(otpDTO);

        String otpGenerated = emailService.sendEmail(appUserRequest.getEmail(), otpDTO.getOtpCode());
        log.info("OTP generated: {}", otpGenerated);

        return appUserDTO;
    }

    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }
}
