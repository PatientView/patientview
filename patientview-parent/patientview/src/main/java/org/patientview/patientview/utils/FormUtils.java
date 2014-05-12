package org.patientview.patientview.utils;

import org.patientview.model.Unit;
import org.patientview.patientview.controller.form.JoinRequestInput;
import org.patientview.patientview.model.JoinRequest;

import java.util.Date;

/**
 * Created by james@solidstategroup.com on 12/05/2014.
 */
public class FormUtils {


    public static JoinRequest createJoinRequestFromInput(JoinRequestInput joinRequestInput, Unit unit) {

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setAntiSpamAnswer(joinRequestInput.getSecurityQuestion());
        joinRequest.setUnitcode(unit.getUnitcode());
        //TODO validation
        //joinRequest.setDateOfBirth(joinRequestInput.getDateOfBirth());
        joinRequest.setDateOfRequest(new Date());
        joinRequest.setFirstName(joinRequestInput.getFirstName());
        joinRequest.setLastName(joinRequestInput.getLastName());
        joinRequest.setEmail(joinRequestInput.getEmail());
        joinRequest.setNhsNo(joinRequest.getNhsNo());

        return joinRequest;

    }

}
