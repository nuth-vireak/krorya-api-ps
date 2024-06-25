package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.AppUserDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.request.*;
import com.kshrd.krorya.model.response.AuthResponse;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AppUserService extends UserDetailsService {

    List<AppUserDTO> getAllUsers();

    AppUserDTO createUser(AppUserRequest appUserRequest) throws MessagingException, IOException;

    AppUser findUserByEmail(String email);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    CustomUserDetail createUserFromGoogle(GoogleUserRequest googleUserRequest);

    AppUserDTO updateUserById(UUID userId, UserRequest userRequest);

    void changeUserPassword(ResetPasswordRequest resetPasswordRequest, UUID userId);

    AppUserDTO findUserByUserId(UUID currentUserId, UUID userId);

    AppUserDTO getCurrentUserInfo();
}
