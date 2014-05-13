package org.patientview.patientview.utils;

import org.patientview.model.Unit;
import org.patientview.patientview.controller.form.JoinRequestInput;
import org.patientview.patientview.model.JoinRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by james@solidstategroup.com on 12/05/2014.
 */
public class FormUtils {


    public static JoinRequest createJoinRequestFromInput(JoinRequestInput joinRequestInput, Unit unit)
            throws ParseException {

        SimpleDateFormat smt = new SimpleDateFormat("DD-mm-yyyy");

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setAntiSpamAnswer(joinRequestInput.getSecurityQuestion());
        joinRequest.setUnitcode(unit.getUnitcode());

        joinRequest.setDateOfBirth(smt.parse(joinRequestInput.getDateOfBirth()));
        joinRequest.setDateOfRequest(new Date());
        joinRequest.setFirstName(joinRequestInput.getFirstName());
        joinRequest.setLastName(joinRequestInput.getLastName());
        joinRequest.setEmail(joinRequestInput.getEmail());
        joinRequest.setNhsNo(joinRequest.getNhsNo());

        return joinRequest;

    }

}
