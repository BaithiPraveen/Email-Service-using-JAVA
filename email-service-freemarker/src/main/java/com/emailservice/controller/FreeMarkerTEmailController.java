package com.emailservice.controller;

import com.emailservice.dto.EmailRequestDTO;
import com.emailservice.dto.EmailResponsceDTO;
import com.emailservice.service.FreeMarkerTEmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/email/freemarkerTemplate/")
@AllArgsConstructor
public class FreeMarkerTEmailController {

    private FreeMarkerTEmailService emailService;
    private ObjectMapper objectMapper;

    @PostMapping("sent")
    public EmailResponsceDTO sentEmail(@RequestBody EmailRequestDTO emailRequestDTO,@RequestParam(value = "file",required = false) List<MultipartFile> files) {
        log.info("Enter the controller in sent email method with  EmailRequestDTO : {}",emailRequestDTO.toString());
        return emailService.sendEmail(emailRequestDTO,files);
    }

    @PostMapping("sentDataAndFile")
    public EmailResponsceDTO sentEmailWithBoth(@RequestParam("requestDTO") String data,@RequestParam("file") List<MultipartFile> files) throws JsonProcessingException {
        log.info("sentEmailWithBoth -> Enter the controller in sent email method with  EmailRequestDTO : {} ",data);
        EmailRequestDTO emailRequestDTO = objectMapper.readValue(data, EmailRequestDTO.class);
        log.info("string to dto data2 : {} ",emailRequestDTO);
        log.info("no of files : {} ",files.size());
        return emailService.sendEmail(emailRequestDTO,files);
    }

    @GetMapping("checking")
    public void dataChecking(){
        emailService.checkingResult();
    }
}
