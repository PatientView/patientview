package org.patientview.patientview.controller;

import org.apache.commons.collections.CollectionUtils;
import org.patientview.model.Patient;
import org.patientview.model.Unit;
import org.patientview.patientview.controller.form.PatientInput;
import org.patientview.patientview.logging.AddLog;
import org.patientview.patientview.logon.LogonUtils;
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.UserMapping;
import org.patientview.service.PatientManager;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.UnitManager;
import org.patientview.service.UserManager;
import org.patientview.service.UserMappingManager;
import org.patientview.util.CommonUtils;
import org.patientview.utils.FormUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by james@solidstategroup.com on 21/03/2014.
 */
@Controller
@RequestMapping(value = Routes.PATIENT_CONTROLLER)
public class PatientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

    @Inject
    private UserManager userManager;

    @Inject
    private UnitManager unitManager;

    @Inject
    private SecurityUserManager securityUserManager;

    @Inject
    private PatientManager patientManager;

    @Inject
    private UserMappingManager userMappingManager;



    @RequestMapping(value = Routes.ADD_PATIENT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addPatient(@RequestBody PatientInput patientInput, HttpServletRequest requestBody) throws Exception {

        // Check the input
        if (!isValidNhsNumber(patientInput)) {

            if (isValidNhsNumberWithLetters(patientInput)) {
                requestBody.setAttribute(LogonUtils.OFFER_TO_ALLOW_INVALID_NHSNO, patientInput.getNhsNo());
            } else {
                requestBody.setAttribute(LogonUtils.INVALID_NHSNO, patientInput.getNhsNo());
            }
            requestBody.setAttribute("patient", patientInput);

            return "/control/patient_add_input.jsp";
        }

        if (isPatientAlreadyInUnit(patientInput)) {
            requestBody.setAttribute(LogonUtils.PATIENT_ALREADY_IN_UNIT, patientInput.getNhsNo());
            requestBody.setAttribute("patient", patientInput);
            return "/control/patient_add_input.jsp";
        }

        if (isPatientAlreadyInSpecialty(patientInput)) {
            // patient with same NHS no. found in another unit, forwards to action asking to add this existing
            // patient to current unit, ignoring all user entered details, firstname/lastname/username etc
            requestBody.setAttribute(LogonUtils.PATIENTS_WITH_SAME_NHSNO,
                    userMappingManager.getAllByNhsNo(patientInput.getNhsNo()));
            requestBody.setAttribute("patient", patientInput);
            return "/control/patient_add_samenhsno.jsp";
        }

        if (!isUsernameAvailable(patientInput)) {
            requestBody.setAttribute(LogonUtils.USER_ALREADY_EXISTS, patientInput.getUsername());
            patientInput.setUsername("");
            requestBody.setAttribute("patient", patientInput);
            return "/control/patient_add_input.jsp";
        };

        // Produce required object for a User account on Patient View
        Patient patient = FormUtils.createPatient(patientInput);
        User user = FormUtils.createUser(patientInput);
        User gpUser = FormUtils.createGpUser(patientInput);

        // Process Objects
        savePatient(patient);
        createUserMapping(user, patient);
        userManager.saveHashPassword(user);
        userManager.saveHashPassword(gpUser);


        // Page Set Up
        requestBody.setAttribute("patient", patientInput);
        List<Unit> units = unitManager.getUnitsBySpecialty(
                userManager.getCurrentSpecialty(userManager.getLoggedInUser()));
        requestBody.setAttribute("units", units);
        // Logging
        AddLog.addLog(securityUserManager.getLoggedInUsername(), AddLog.PATIENT_ADD,
                patientInput.getUsername(), patientInput.getNhsNo(), patientInput.getUnitCode(), "");

        return "/control/patient_add_confirm.jsp";

    }


    private boolean isUsernameAvailable(PatientInput patientInput) {
        // get User object, used to check if user already exists
        return userManager.get(patientInput.getUsername()) == null? true : false;

    }



    private void createUserMapping(User user, Patient patient) {
        userMappingManager.createPatientMapping(user, patient);
    }


    private boolean isValidNhsNumber(PatientInput patientInput) {
        // check if NHS number is valid, if not then check again allowing pseudo NHS numbers and prompt user
        // to allow pseudo NHS number in UI
        return !CommonUtils.isNhsNumberValid(patientInput.getNhsNo())
                && !"on".equals(patientInput.getOverrideDuplicateNhsNo());
    }

    private boolean isValidNhsNumberWithLetters(PatientInput patientInput) {
        return CommonUtils.isNhsNumberValidWhenUppercaseLettersAreAllowed(patientInput.getNhsNo());

    }

    private boolean isPatientAlreadyInUnit(PatientInput patientInput) {
        List<UserMapping> userMappings = userManager.getUserMappingsByNhsNo(patientInput.getNhsNo());

        // patients exist across all specialties
        if (!CollectionUtils.isEmpty(userMappings)) {
            // patients exist in this specialty
            for (UserMapping userMappingWithSameNhsno : userMappings) {
                if (userMappingWithSameNhsno.getUnitcode().equalsIgnoreCase(patientInput.getUnitCode())) {
                    // patient with NHS no. found in current unit
                    return true;
                }
            }
        }

        return false;

    }


    private boolean isPatientAlreadyInSpecialty(PatientInput patientInput) {

        // get list of patients with same NHS number across specialties
        List<UserMapping> userMappingsAllSpecialties = userMappingManager.getAllByNhsNo(patientInput.getNhsNo());
        // check other patients exist with same NHS no.
        if (!CollectionUtils.isEmpty(userMappingsAllSpecialties)) {
            return true;
        }

        return false;
    }


    private void savePatient(Patient patient) {
        if (patientManager.get(patient.getNhsno(), patient.getUnitcode()) != null)  {
            patientManager.save(patient);
        }
    }





}
