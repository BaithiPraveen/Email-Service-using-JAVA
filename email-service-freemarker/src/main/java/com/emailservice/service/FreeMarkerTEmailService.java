package com.emailservice.service;

import com.emailservice.dto.EmailRequestDTO;
import com.emailservice.dto.EmailResponsceDTO;
import com.emailservice.exceptions.CustomExceptionHandler;
import com.emailservice.utils.Constants;
import com.emailservice.utils.DateAndTimeUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
//@EnableAsync
public class FreeMarkerTEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration configuration;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.email.sender.name}")
    private String senderName;

    @Value("${spring.email.sender.location}")
    private String location;

    @Value("${spring.mail.username}")
    private String userName;

    public EmailResponsceDTO sendEmail(EmailRequestDTO emailRequestDTO, List<MultipartFile> mFiles) {
        log.info("Enter the sendEmail method in service with EmailRequestDTO: {}", emailRequestDTO);

        EmailResponsceDTO emailResponsceDTO = new EmailResponsceDTO();
        Map<String, Object> model = new HashMap<>();

        try {
            File file = new File(Constants.LOGS_FILE_PATH);
            log.info("Get file from this path : {} ",Constants.LOGS_FILE_PATH);

            model.put(Constants.RECIPIENT_NAME, emailRequestDTO.getName());
            model.put(Constants.SENDER_NAME, senderName);
            model.put(Constants.LOCATION, location);
            if(emailRequestDTO.getDescription()!=null)
                model.put(Constants.DESCRIPTION,emailRequestDTO.getDescription());
            log.info("Created Email Template Model");

            Template template = configuration.getTemplate(Constants.TEMPLATE_FILE_NAME);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            log.info("Merged Template with Data");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            log.info("Created MimeMessage and MimeMessageHelper");

            helper.setTo(emailRequestDTO.getTo());
            helper.setSubject(emailRequestDTO.getSubject());
            helper.setText(html, true);
            helper.setFrom(userName); // our email address
            log.info("Set email content");

            FileSystemResource logsFileResource = new FileSystemResource(file);
            helper.addAttachment(file.getName(), logsFileResource);
            log.info("Attached the logs-file");

            if( mFiles != null && !mFiles.isEmpty() ){
                int fileSize = mFiles.size();
                for(MultipartFile mFile: mFiles) {
                    FileSystemResource ExternalFileResource = fileToFileResource(mFile);
                    helper.addAttachment(Objects.requireNonNull(mFile.getOriginalFilename()), ExternalFileResource);
                    log.info("Attached the External File with : {} ", mFile.getOriginalFilename());
                    fileSize-=1;
                }
                if(fileSize !=0 ) {
                    log.error(" {} No of files Missing..!", fileSize);
                    throw new CustomExceptionHandler("error due to missing files : '"+fileSize+"' for uploading the  External Files ");
                }
            }

            mailSender.send(message);
            log.info("Email sent to: {}", emailRequestDTO.getTo());

            emailResponsceDTO.setMessage(String.format("Email sent to %s", emailRequestDTO.getTo()));
            emailResponsceDTO.setStatus("SUCCESS ✅");
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
            emailResponsceDTO.setMessage(e.getMessage());
            emailResponsceDTO.setStatus("FAILED ❌");
        }

        return emailResponsceDTO;
    }



    //    public EmailResponsceDTO sendEmail(EmailRequestDTO emailRequestDTO,MultipartFile mFile) {
//        log.info("Enter the sendEmail method in service with EmailRequestDTO: {}", emailRequestDTO);
//
//        EmailResponsceDTO emailResponsceDTO = new EmailResponsceDTO();
//        Map<String, Object> model = new HashMap<>();
//
//        try {
//            File file = new File(Constants.LOGS_FILE_PATH);
//            log.info("File converted into String content");
//
//            model.put(Constants.RECIPIENT_NAME, emailRequestDTO.getName());
//            model.put(Constants.SENDER_NAME, senderName);
//            model.put(Constants.LOCATION, location);
//            if(emailRequestDTO.getDescription()!=null)
//                model.put(Constants.DESCRIPTION,emailRequestDTO.getDescription());
//            log.info("Created Email Template Model");
//
//            Template template = configuration.getTemplate(Constants.TEMPLATE_FILE_NAME);
//            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//            log.info("Merged Template with Data");
//
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
//            log.info("Create MimeMessage and MimeMessageHelper");
//
//            helper.setTo(emailRequestDTO.getTo());
//            helper.setSubject(emailRequestDTO.getSubject());
//            helper.setText(html, true);
//            helper.setFrom(userName); // our email address
//            log.info("Set email content");
//
//            FileSystemResource fileResource = new FileSystemResource(file);
//            helper.addAttachment(file.getName(), fileResource);
//            helper.addAttachment("original"+file.getName(), fileResource);
//            log.info("Attached the log-file");
//            if(mFile !=null){
//                FileSystemResource fileResource2 = fileToFileResource(mFile);
//                helper.addAttachment(Objects.requireNonNull(mFile.getOriginalFilename()),fileResource2);
//                log.info("Attached the External File with : {} ",mFile.getName());
//            }
//
//            mailSender.send(message);
//            log.info("Email sent to: {}", emailRequestDTO.getTo());
//
//            emailResponsceDTO.setMessage(String.format("Email sent to %s", emailRequestDTO.getTo()));
//            emailResponsceDTO.setStatus("SUCCESS ✅");
//        } catch (Exception e) {
//            log.error("Failed to send email: {}", e.getMessage());
//            emailResponsceDTO.setMessage(e.getMessage());
//            emailResponsceDTO.setStatus("FAILED ❌");
//        }
//
//        return emailResponsceDTO;
//    }
    private FileSystemResource fileToFileResource(MultipartFile file){
        try {
            log.info("Converting file to FileSystemResource...");
            Path tempFile = Files.createTempFile("temp-", "-" + file.getOriginalFilename());
            file.transferTo(tempFile);
            log.info("Temp - File path address : {} ",tempFile);
            return new FileSystemResource(tempFile.toFile());
        } catch (IOException e) {
            log.error("Failed to convert file to FileSystemResource: {}", e.getMessage());
            throw new RuntimeException("Failed to convert file to FileSystemResource", e);
        }

    }

    //FIXED-DELAY

//    @Scheduled(fixedDelay = 1000)
//    public void schedulingExample() throws InterruptedException {
//        log.info("schedulingExample -> Current time now: {}", DateAndTimeUtil.getDateAndTime());
////        Thread.sleep(1000);
//    }

    // FIXED-RATE
//    @Async
//    @Scheduled(fixedRate = 1000)
//    public void schedulingExample1() throws InterruptedException {
//        log.info("schedulingExample1 -> Current time now: {}", DateAndTimeUtil.getDateAndTime());
//        Thread.sleep(1000);
//    }

    // FIXED-DELAY-STRING
//    @Scheduled(fixedDelayString ="PT02S" )
//    public void schedulingExample1() throws InterruptedException {
//        log.info("schedulingExample1 -> Current time now: {}", DateAndTimeUtil.getDateAndTime());
//    }

    @Scheduled(cron = "0 */1 * * * *")
    public void schedulingExample1() throws InterruptedException {
        log.info("schedulingExample1 -> Current time now: {}", DateAndTimeUtil.getDateAndTime());
    }

    public void checkingResult(){
        String url = "http://localhost:8081/files/pdf/user/download";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<InputStreamResource> response =  restTemplate.exchange(url, HttpMethod.GET,entity,InputStreamResource.class);//restTemplate.getForObject("http://localhost:8081/files/pdf/user/download",InputStreamResource.class)
        log.info("data from rest template : {}",response);
    }
}
