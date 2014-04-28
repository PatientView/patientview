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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

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



    @RequestMapping(value = Routes.ADD_PATIENT, consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView addPatient(@RequestBody PatientInput patientInput, HttpServletRequest requestBody)
            throws Exception {

        // Check the input
        if (!isValidNhsNumber(patientInput)) {


            ModelAndView modelAndView = new ModelAndView("control/patient_add_input");
            modelAndView.addObject("patient", patientInput);


            if (isValidNhsNumberWithLetters(patientInput)) {
                modelAndView.addObject(LogonUtils.OFFER_TO_ALLOW_INVALID_NHSNO, patientInput.getNhsNo());
            } else {
                modelAndView.addObject(LogonUtils.INVALID_NHSNO, patientInput.getNhsNo());
            }
            requestBody.setAttribute("patient", patientInput);

            return modelAndView;
        }

        if (isPatientAlreadyInUnit(patientInput)) {

            ModelAndView modelAndView = new ModelAndView("control/patient_add_input");
            modelAndView.addObject(LogonUtils.PATIENT_ALREADY_IN_UNIT, patientInput.getNhsNo());
            modelAndView.addObject("patient", patientInput);
            return modelAndView;
        }

        if (isPatientAlreadyInSpecialty(patientInput)) {
            // patient with same NHS no. found in another unit, forwards to action asking to add this existing
            // patient to current unit, ignoring all user entered details, firstname/lastname/username etc
            ModelAndView modelAndView = new ModelAndView("control/patient_add_input");
            modelAndView.addObject(LogonUtils.PATIENTS_WITH_SAME_NHSNO,
                    userMappingManager.getAllByNhsNo(patientInput.getNhsNo()));
            modelAndView.addObject("patient", patientInput);
            return modelAndView;
        }

        if (!isUsernameAvailable(patientInput)) {
            requestBody.setAttribute(LogonUtils.USER_ALREADY_EXISTS, patientInput.getUsername());
            patientInput.setUsername("");
            ModelAndView modelAndView = new ModelAndView("control/patient_add_input");
            modelAndView.addObject("patient", patientInput);
            return modelAndView;
        }


        // Produce required object for a User account on Patient View
        Patient patient = FormUtils.createPatient(patientInput);
        User user = FormUtils.createUser(patientInput);
        User gpUser = FormUtils.createGpUser(patientInput);

        // Process Objects
        savePatient(patient);
        createUserMapping(user, patient);
        userManager.saveHashPassword(user);
        userManager.saveHashPassword(gpUser);

        // Success! Page Set Up
        ModelAndView modelAndView;
        if (userManager.getCurrentSpecialtyRole(user).equalsIgnoreCase("ibd")) {
            modelAndView = new ModelAndView("control/patient_ibd_input");
        } else {
            modelAndView = new ModelAndView("control/patient_add_confirm");
        }
        modelAndView.addObject("patient", patientInput);
        List<Unit> units = unitManager.getUnitsBySpecialty(
                userManager.getCurrentSpecialty(userManager.getLoggedInUser()));
        modelAndView.addObject("units", units);
        // Logging
        AddLog.addLog(securityUserManager.getLoggedInUsername(), AddLog.PATIENT_ADD,
                patientInput.getUsername(), patientInput.getNhsNo(), patientInput.getUnitCode(), "");

        return modelAndView;

    }


    private boolean isUsernameAvailable(PatientInput patientInput) {
        // get User object, used to check if user already exists
        return userManager.get(patientInput.getUsername()) == null ? true : false;

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
