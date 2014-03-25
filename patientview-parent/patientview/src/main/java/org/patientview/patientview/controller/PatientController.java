package org.patientview.patientview.controller;

import org.patientview.ibd.action.BaseAction;
import org.patientview.patientview.controller.form.PatientInput;
import org.patientview.service.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * Created by james@solidstategroup.com on 21/03/2014.
 */
@Controller(value = Routes.PATIENT_CONTROLLER)
public class PatientController extends BaseAction {


    private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

    @Inject
    private UserManager userManager;


    @RequestMapping(value = Routes.ADD_PATIENT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addPatient(PatientInput patientForm) {
        LOGGER.info("Add a patient!!!!");

    }

    public void updatePatient(PatientInput patientInput) {


    }

}
