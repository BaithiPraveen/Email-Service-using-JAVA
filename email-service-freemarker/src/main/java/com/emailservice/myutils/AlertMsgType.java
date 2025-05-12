package com.emailservice.myutils;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class AlertMsgType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String alertMsg;
    private String alertType;
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_message_entity_id")
    private AlertMessageEntity alertMessageEntity;
}
