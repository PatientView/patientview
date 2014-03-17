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

import org.patientview.model.Patient;
import org.patientview.model.enums.SourceType;
import org.patientview.patientview.logging.AddLog;
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.UserMapping;
import org.patientview.service.PatientManager;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.UserManager;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.struts.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PatientAddToUnitAction extends ActionSupport {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        PatientManager patientManager = getWebApplicationContext().getBean(PatientManager.class);
        UserManager userManager = getWebApplicationContext().getBean(UserManager.class);
        SecurityUserManager securityUserManager = getWebApplicationContext().getBean(SecurityUserManager.class);

        String username = BeanUtils.getProperty(form, "username");
        String nhsno = BeanUtils.getProperty(form, "nhsno");
        String unitcode = BeanUtils.getProperty(form, "unitcode");

        UserMapping userMapping = new UserMapping(username, unitcode, nhsno);
        userManager.save(userMapping);

        if (thereIsAGpUser(username)) {
            UserMapping userMappingGp = new UserMapping(username + "-GP", unitcode, nhsno);
            userManager.save(userMappingGp);
        }

        // add dummy patient row, necessary for "Patients in Unit" screen
        Patient patient = new Patient();
        patient.setNhsno(nhsno);
        patient.setUnitcode(unitcode);
        patient.setSourceType(SourceType.PATIENT_VIEW.getName());
        patientManager.save(patient);

        AddLog.addLog(securityUserManager.getLoggedInUsername(), AddLog.PATIENT_ADD, username, nhsno, unitcode, "");
        String mappingToFind = "success";

        request.setAttribute("userMapping", userMapping);
        return mapping.findForward(mappingToFind);
    }

    private boolean thereIsAGpUser(String username) {
        UserManager userManager = getWebApplicationContext().getBean(UserManager.class);
        User user = userManager.get(username + "-GP");
        return null != user;
    }
}
