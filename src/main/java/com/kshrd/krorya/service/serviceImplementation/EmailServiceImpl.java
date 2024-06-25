package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public String sendEmail(String toEmail, String otp) throws MessagingException, IOException {
        log.info("Sending email from: {}", fromEmail);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your OTP for Email Verification");
        helper.setFrom(fromEmail);

        Context context = new Context();
        context.setVariable("otp", otp);

        String htmlContent = templateEngine.process("otp-mail", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        return otp;
    }
}
