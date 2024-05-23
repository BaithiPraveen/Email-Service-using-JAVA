package com.emailservice.service;

import com.emailservice.dto.EmailRequestDTO;
import com.emailservice.dto.EmailResponsceDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
@Service
@AllArgsConstructor
public class VelocityService {

    private final JavaMailSender emailSender;
    private final VelocityEngine velocityEngine;


    public EmailResponsceDTO sendEmail(EmailRequestDTO emailRequest,VelocityContext context) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        EmailResponsceDTO emailResponsceDTO = new EmailResponsceDTO();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            Template template = velocityEngine.getTemplate("templates/email-velocity.vm");

            String content = evaluateTemplate(context, template);

            helper.setTo(emailRequest.getTo());
            helper.setSubject(emailRequest.getSubject());
            helper.setFrom("${spring.mail.username}");
            helper.setText(content, true);

            emailSender.send(mimeMessage);
            emailResponsceDTO.setMessage(String.format("sent email to %s",emailRequest.getTo()));
            emailResponsceDTO.setStatus("SUCCESS..✅");

        } catch (MessagingException e) {
            emailResponsceDTO.setMessage(e.getMessage());
            emailResponsceDTO.setStatus("FAILED...✅");
        }
        return emailResponsceDTO;
    }

    private String evaluateTemplate(VelocityContext context, Template template) {
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}
