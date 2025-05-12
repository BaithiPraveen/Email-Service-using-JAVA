package com.emailservice.myutils;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class AlertMessageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userTimeSheetId;
    private long attendanceId;
    private int calendarId;
    private String task;
    private String spentDate;
    private int isBillable;
    private int eId;
    private int orgId;
    private String shift;
    private int workingHrs;
//    private AlrtLevelTypeEnum alertLevelType; // from enum
    private String alertMsgNotification;
    private String customMsg;
    private String projectType;
    private Date creationDate;
    private Date updatedDate;
//    private long listAlertsId; // ref to
    private String colourCode;
    private Boolean flag;
    private Boolean flag_status;
    private String flagEditBy;

    @OneToMany(mappedBy = "alertMessageEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlertMsgType> alertMsgTypes = new ArrayList<>();
}
