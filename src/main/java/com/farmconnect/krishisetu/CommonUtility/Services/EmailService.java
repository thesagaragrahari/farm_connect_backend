package com.farmconnect.krishisetu.CommonUtility.Services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /*
     * ===============================
     * EMAIL VERIFICATION MAIL
     * ===============================
     */
    public void sendVerificationEmail(String to, String verificationLink) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify Your Email – Farm Connect");

        message.setText(
                "Hello,\n\n" +
                "Thank you for registering with Farm Connect.\n\n" +
                "Please verify your email address by clicking the link below:\n\n" +
                verificationLink + "\n\n" +
                "This verification link is valid for 15 minutes.\n\n" +
                "If you did not create this account, please ignore this email.\n\n" +
                "Regards,\n" +
                "Farm Connect Team"
        );

        mailSender.send(message);
    }

    /*
     * ===============================
     * RESET PASSWORD MAIL (Future Use)
     * ===============================
     */
    public void sendResetPasswordMail(String to, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Your Password – Farm Connect");

        message.setText(
                "Hello,\n\n" +
                "Click the link below to reset your password:\n\n" +
                resetLink + "\n\n" +
                "This link is valid for 15 minutes.\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Regards,\n" +
                "Farm Connect Team"
        );

        mailSender.send(message);
    }
}