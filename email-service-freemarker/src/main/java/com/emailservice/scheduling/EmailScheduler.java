package com.emailservice.scheduling;

import com.emailservice.dto.EmailRequestDTO;
import com.emailservice.entity.User;
import com.emailservice.repository.UserRepo;
import com.emailservice.service.FreeMarkerTEmailService;
import com.emailservice.utils.DateAndTimeUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class EmailScheduler {

    private FreeMarkerTEmailService emailService;
    private UserRepo userRepository;

    @Scheduled(fixedDelayString = "PT02M")
//    @Scheduled(cron = "0 10 14 ? * THU")
//    @Scheduled(cron = "*/ */2 * * * *")
    public void sendEmailAccordingToScheduler(){
        log.info("Entered the Scheduler Method with time is : {} ", DateAndTimeUtil.getDateAndTime());
        List<User> AdminList = userRepository.findByRole("ADMIN");
        AdminList
                .forEach(adminUser -> {
                    EmailRequestDTO emailRequestDTO = EmailRequestDTO.builder()
                            .to(adminUser.getEmail())
                            .name(adminUser.getName())
                            .subject("LOG-DETAILS")
                            .description("Project Log Details..")
                            .build();
                    emailService.sendEmail(emailRequestDTO, null);
                    log.info("sent Email to {} this user in Scheduler ",adminUser.getName());
                });
    }
}
