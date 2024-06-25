package com.kshrd.krorya.service;

import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailService {
    String sendEmail(String toEmail, String otp) throws MessagingException, IOException;

}