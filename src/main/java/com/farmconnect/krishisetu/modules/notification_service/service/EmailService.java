package com.farmconnect.krishisetu.modules.notification_service.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String link) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify Your Email");
        message.setText("Click to verify: " + link);

        mailSender.send(message);
    }

    public void sendResetPasswordMail(String to, String link) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Your Password");
        message.setText("Click to reset: " + link);

        mailSender.send(message);
    }

    public void sendWelcomeEmail(String to, String name) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to KrishiSetu");
        message.setText("Hi " + name + ", Welcome to KrishiSetu!");

        mailSender.send(message);
    }

    public void sendPasswordChangedAlert(String to) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Changed");
        message.setText("Your password has been successfully changed.");

        mailSender.send(message);
    }
}