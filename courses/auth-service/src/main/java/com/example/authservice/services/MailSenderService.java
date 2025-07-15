package com.example.authservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        // message.setFrom("");// this is the email
        message.setSubject(subject);
        message.setText(body);


        mailSender.send(message);
    }


    // this will execute the method intended less time needed to execute other method(caller method)
    @Async
    public void sendResetPassword(String email, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mshlool38@gmail");
        message.setTo(email);
        message.setSubject("reset password");
        message.setText(url);
        mailSender.send(message);
    }

    // future update make an email send in it html page to make it more UI friendly or UX
}
