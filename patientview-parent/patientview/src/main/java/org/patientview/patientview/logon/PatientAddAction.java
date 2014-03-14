/*
 * PatientView
 *
 * Copyright (c) Worth Solutions Limited 2004-2013
 *
 * This file is part of PatientView.
 *
 * PatientView is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * PatientView is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with PatientView in a file
 * titled COPYING. If not, see <http://www.gnu.org/licenses/>.
 *
 * @package PatientView
 * @link http://www.patientview.org
 * @author PatientView <info@patientview.org>
 * @copyright Copyright (c) 2004-2013, Worth Solutions Limited
 * @license http://www.gnu.org/licenses/gpl-3.0.html The GNU General Public License V3.0
 */

package org.patientview.patientview.logon;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.model.Patient;
import org.patientview.model.Unit;
import org.patientview.model.enums.SourceType;
import org.patientview.patientview.logging.AddLog;
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.UserMapping;
import org.patientview.patientview.unit.UnitUtils;
import org.patientview.service.PatientManager;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.UnitManager;
import org.patientview.service.UserManager;
import org.patientview.util.CommonUtils;
import org.springframework.web.struts.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PatientAddAction extends ActionSupport {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        UnitManager unitManager = getWebApplicationContext().getBean(UnitManager.class);
        UserManager userManager = getWebApplicationContext().getBean(UserManager.class);
        SecurityUserManager securityUserManager = getWebApplicationContext().getBean(SecurityUserManager.class);
        PatientManager patientManager = getWebApplicationContext().getBean(PatientManager.class);

        // get patient details from form fields
        String username = BeanUtils.getProperty(form, "username");
        String password = LogonUtils.generateNewPassword();
        String gppassword = LogonUtils.generateNewPassword();
        String firstName = BeanUtils.getProperty(form, "firstName");
        String lastName = BeanUtils.getProperty(form, "lastName");
        String email = BeanUtils.getProperty(form, "email");
        String nhsno = BeanUtils.getProperty(form, "nhsno").trim();
        String unitcode = BeanUtils.getProperty(form, "unitcode");
        String overrideInvalidNhsno = BeanUtils.getProperty(form, "overrideInvalidNhsno");
        boolean dummypatient = "true".equals(BeanUtils.getProperty(form, "dummypatient"));

        // get Unit selected by user
        Unit unit = unitManager.get(unitcode);

        // check if user is attempting to add patient to RADAR unit
        if ("radargroup".equalsIgnoreCase(unit.getSourceType())) {
            request.setAttribute("radarGroupPatient", unit.getName());
            return mapping.findForward("input");
        }

        // create new PatientLogon object and set fields
        PatientLogon patientLogon = new PatientLogon(username, password, firstName, lastName,
                email, false, true, dummypatient, null, 0, false);

        // create 2 new UserMapping objects, one with selected details and another
        // with generic PATIENT_ENTERS_UNITCODE unit code (currently user code "PATIENT")
        UserMapping userMapping = new UserMapping(username, unitcode, nhsno);
        UserMapping userMappingPatientEnters = new UserMapping(username, UnitUtils.PATIENT_ENTERS_UNITCODE, nhsno);

        // add username-GP user for GP to log in and associated UserMapping
        PatientLogon gpPatientLogon = new PatientLogon(username + "-GP", gppassword, firstName,
                lastName + "-GP", null, false, true, dummypatient, null, 0, false);
        UserMapping userMappingGp = new UserMapping(username + "-GP", unitcode, nhsno);

        // get User object, used to check if user already exists
        User existingUser = userManager.get(username);

        // get list of patients with same NHS number
        List existingPatientsWithSameNhsno = userManager.getUserMappingsForNhsNo(nhsno);

        String mappingToFind = "";

        // check if NHS number is valid, if not then check again allowing pseudo NHS numbers and prompt user
        // to allow pseudo NHS number in UI
        if (!CommonUtils.isNhsNumberValid(nhsno) && !"on".equals(overrideInvalidNhsno)) {
            request.setAttribute(LogonUtils.INVALID_NHSNO, nhsno);

            if (CommonUtils.isNhsNumberValidWhenUppercaseLettersAreAllowed(nhsno)) {
                request.setAttribute(LogonUtils.OFFER_TO_ALLOW_INVALID_NHSNO, nhsno);
            }

            mappingToFind = "input";
        }

        // check if user already exists
        if (existingUser != null) {
            request.setAttribute(LogonUtils.USER_ALREADY_EXISTS, username);
            patientLogon.setUsername("");
            mappingToFind = "input";
        }

        // check other patients exist with same NHS no.
        if (existingPatientsWithSameNhsno != null && !existingPatientsWithSameNhsno.isEmpty()) {
            for (Object obj : existingPatientsWithSameNhsno) {
                UserMapping userMappingWithSameNhsno = (UserMapping) obj;
                if (userMappingWithSameNhsno.getUnitcode().equalsIgnoreCase(unitcode)) {
                    // patient with NHS no. found in current unit
                    request.setAttribute(LogonUtils.PATIENT_ALREADY_IN_UNIT, nhsno);
                    mappingToFind = "input";
                }
            }
            if ("".equals(mappingToFind)) {
                // patient with same NHS no. found in another unit, forwards to action asking to add this existing
                // patient to current unit, ignoring all user entered details, firstname/lastname/username etc
                request.setAttribute(LogonUtils.NHSNO_ALREADY_EXISTS, nhsno);
                request.setAttribute(LogonUtils.PATIENTS_WITH_SAME_NHSNO, existingPatientsWithSameNhsno.get(0));
                mappingToFind = "samenhsno";
            }
        }

        // if all checks passed, save patient and related users
        if (mappingToFind.equals("")) {
            PatientLogon hashedPatient = (PatientLogon) patientLogon.clone();
            PatientLogon hashedGp = (PatientLogon) gpPatientLogon.clone();

            hashedPatient.setPassword(LogonUtils.hashPassword(hashedPatient.getPassword()));
            hashedGp.setPassword(LogonUtils.hashPassword(hashedGp.getPassword()));

            userManager.saveUserFromPatient(hashedPatient);
            userManager.saveUserFromPatient(hashedGp);

            userManager.save(userMapping);
            userManager.save(userMappingPatientEnters);
            userManager.save(userMappingGp);

            if (patientManager.get(nhsno, unitcode) == null) {
                Patient patient = new Patient();
                patient.setNhsno(nhsno);
                patient.setUnitcode(unitcode);
                patient.setEmailAddress(email);
                patient.setSourceType(SourceType.PATIENT_VIEW.getName());
                patientManager.save(patient);
            }

            AddLog.addLog(securityUserManager.getLoggedInUsername(), AddLog.PATIENT_ADD,
                    patientLogon.getUsername(),
                    userMapping.getNhsno(), userMapping.getUnitcode(), "");
            mappingToFind = "success";
        }

        List<Unit> units = unitManager.getAll(false);

        request.setAttribute("units", units);
        request.setAttribute("patient", patientLogon);
        request.setAttribute("userMapping", userMapping);
        request.getSession().setAttribute("gp", gpPatientLogon);
        request.getSession().setAttribute("userMappingGp", userMappingGp);

        return mapping.findForward(mappingToFind);
    }
}
