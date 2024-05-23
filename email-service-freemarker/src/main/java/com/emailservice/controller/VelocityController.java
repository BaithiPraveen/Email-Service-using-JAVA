package com.emailservice.controller;

import com.emailservice.dto.EmailRequestDTO;
import com.emailservice.dto.EmailResponsceDTO;
import com.emailservice.service.VelocityService;
import lombok.AllArgsConstructor;
import org.apache.velocity.VelocityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/email/velocity/")
@AllArgsConstructor
public class VelocityController {

    private VelocityService velocityService;

    @PostMapping("sent")
    public EmailResponsceDTO sentEmail(@RequestBody EmailRequestDTO emailRequestDTO){
        VelocityContext context = new VelocityContext();
        context.put("recipientName", emailRequestDTO.getName());
        context.put("senderName","${email.sender.name}");
        context.put("location","${email.sender.location}");
        return velocityService.sendEmail(emailRequestDTO,context);
    }
}
