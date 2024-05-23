package com.emailservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Builder
@ToString
@Data
public class EmailRequestDTO {

    private String name;
    private String to;
    private String subject;
    private String description;
//    private MultipartFile file;

}
