package org.patientview.utils;

import org.patientview.model.Patient;
import org.patientview.model.enums.SourceType;
import org.patientview.patientview.controller.form.PatientInput;
import org.patientview.patientview.logon.LogonUtils;
import org.patientview.patientview.model.User;

/**
 * Created by eatek on 26/03/2014.
 */
public class FormUtils {


    private FormUtils() {


    }

    /**
     * Helper to create a patient object for a patient input object
     *
     * @param patientInput
     * @return
     */
    public static Patient createPatient(PatientInput patientInput) {
        Patient patient = new Patient();
        patient.setNhsno(patientInput.getNhsNo());
        patient.setUnitcode(patientInput.getUnitCode());
        patient.setEmailAddress(patientInput.getEmail());
        patient.setForename(patientInput.getFirstName());
        patient.setSurname(patientInput.getLastName());
        patient.setSourceType(SourceType.PATIENT_VIEW.getName());
        return patient;
    }

    public static User createUser(PatientInput patientInput) {
        User user = new User();
        user.setUsername(patientInput.getUsername());
        user.setEmail(patientInput.getEmail());
        user.setFirstName(patientInput.getFirstName());
        user.setLastName(patientInput.getLastName());
        user.setPassword(LogonUtils.generateNewPassword());
        return user;
    }

    public static User createGpUser(PatientInput patientInput) {
        User user = new User();
        user.setUsername(patientInput.getUsername() + "-GP");
        user.setEmail(patientInput.getEmail());
        user.setFirstName(patientInput.getFirstName());
        user.setLastName(patientInput.getLastName());
        user.setPassword(LogonUtils.generateNewPassword());
        return user;
    }



}
