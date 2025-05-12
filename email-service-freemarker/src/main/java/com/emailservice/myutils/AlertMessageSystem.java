package com.osi.myutils;

import com.osi.ems.attendance.domain.EmployeeAttendance;
import com.osi.tsm.service.dto.UserTimesheetDTO;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertMessageSystem {

    // all users
    // this for one day...
    @Transactional
    public AlertMessageEntity saveAlertMsgSystem(EmployeeAttendance attendance, UserTimesheetDTO userTimesheetDTO) throws Exception {
        AlertMessageEntity alertMessageEntity = new AlertMessageEntity();
        List<AlertMsgType> alertMsgTypeList = new ArrayList<>();

        alertMessageEntity.setEId(userTimesheetDTO.getEmployeeId());
        alertMessageEntity.setAttendanceId(attendance.getId());
        alertMessageEntity.setIsBillable(userTimesheetDTO.getIsBillable());
        alertMessageEntity.setSpentDate(userTimesheetDTO.getSpentDate());
        alertMessageEntity.setOrgId(userTimesheetDTO.getOrgId());
        alertMessageEntity.setShift(userTimesheetDTO.getShift());
        alertMessageEntity.setUserTimeSheetId(userTimesheetDTO.getTimeSheetId());
//        alertMessageEntity.setCalendarId(userTimesheetDTO.getc);

        LocalDateTime inTime = attendance.getInTime();
        LocalDateTime outTime = attendance.getOutTime();
        double totalWorkingHrs = attendance.getWorkingHrs();

        Map<String,Integer> shiftHrs =  checkTheShift(attendance);


        // shift wise working or missing hrs // doubt

        //recognize the sift
        String shiftName = getActualShiftAccordingToAttendanceTrack(shiftHrs);

        // so now check the which shit is enter
        if(!userTimesheetDTO.getShift().equals(shiftName)){

            //throw AlertMessage with u mismatch the shift && enter for reason for mismatch
            AlertMsgType alertMsgType = new AlertMsgType();
            alertMsgType.setAlertMsg("mismatch the shift please check proper shift..");
            alertMsgType.setAlertType("mismatch shift"); //static variable
            alertMsgType.setReason("enter reason .."); //user enter the reason

            alertMsgTypeList.add(alertMsgType);
            alertMessageEntity.setFlag(true);
        }

        //calculated missing Hrs
        double totalWorkingHrAsPerMap = shiftHrs.values().stream().mapToInt(integer -> integer).sum();
        if (totalWorkingHrs !=totalWorkingHrAsPerMap){
            throw new Exception("exp due to mismatch the hrs");
        }
        double missingWorkingHrs = calculatedMissingHrs(shiftHrs,userTimesheetDTO);
        if (missingWorkingHrs > 0){
            //throw AlertMessage with u mismatch the working Hrs && enter for reason for mismatch
            AlertMsgType alertMsgType = new AlertMsgType();
            alertMsgType.setAlertMsg("mismatch the wrk Hrs please check ..");
            alertMsgType.setAlertType("mismatch Working Hrs"); //static variable
            alertMsgType.setReason("enter reason .."); //user enter the reason

            alertMsgTypeList.add(alertMsgType);
            alertMessageEntity.setFlag(true);
        }
        alertMessageEntity.setAlertMsgTypes(alertMsgTypeList);

        return alertMessageEntity;
        // in save method in time sheet by daily basis check the flag.
        //if flag is TRUE then it shows alert msg with popup box
        // and we have one more problem like when fill time shit WRK Hrs is 7/8 .
        // he submitted time and he also cover reaming 1 hr.
        //in this case every weekend in  end week column update the wrk hrs based on attendance track.
    }

    private double calculatedMissingHrs(Map<String, Integer> shiftHrs,UserTimesheetDTO userTimesheetDTO) {

        //check the diff b/w user enter hrs and attendance working hrs

        double totalWorkingHrAsPerMap = shiftHrs.values().stream().mapToInt(integer -> integer).sum();
        double timeSheetHrs = userTimesheetDTO.getHours();
        return timeSheetHrs-totalWorkingHrAsPerMap;
    }

    private Map<String, Integer> checkTheShift(EmployeeAttendance attendance) {

        // logic

        Map<String, Integer> shiftHrs = new HashMap<>();
        shiftHrs.put("shift1",6);
        shiftHrs.put("shift2",2);

        return shiftHrs;
    }
    private String getActualShiftAccordingToAttendanceTrack(Map<String,Integer> shiftHrs){
        // get highest value of map values
        // example "shift1": 6 & "shift2": 2
        int maxWorkingHrsInShit = shiftHrs.values().stream().mapToInt(integer -> integer).max().getAsInt();
        //get maxWorkingShift using maxWorkingHrsInShit
        //we get shift1
        // return max value or Max hrs of shift;
        return "shift1";
    }

    //update
    // RM & PMO
    // alertMessageEntity based on user time sheet Entry

    public void updateAlertMsg(AlertMessageEntity alertMessageEntity){
        if(!alertMessageEntity.getFlag())
            return;
        List<AlertMsgType> alertMsgTypes = alertMessageEntity.getAlertMsgTypes();
        for (AlertMsgType msgType:alertMsgTypes){
            String msgTypeAlertType = msgType.getAlertType();
            String alertMsg = msgType.getAlertMsg();
            String reason = msgType.getReason();

            // if reason is genuine
            // then RM or PMO changed their flag status to false
            // msgType.  // doubt ?
        }
        // over all alerts Set flag True
        alertMessageEntity.setFlag(true);
        alertMessageEntity.setFlagEditBy("Authenticate.user.name"); //RM or PMO

        // then it goes to PMO approvals
        // they are same like RM if flag is true then see the all Alert Msg
        // they check uer enter reason , alertType ,& project type
        // if he convinced with reason he approved or reject.

    }

}
