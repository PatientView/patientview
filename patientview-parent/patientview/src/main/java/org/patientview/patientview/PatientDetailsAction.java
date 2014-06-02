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

package org.patientview.patientview;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.patientview.actionutils.ActionUtils;
import org.patientview.model.Unit;
import org.patientview.model.enums.SourceType;
import org.patientview.patientview.edtacode.EdtaCodeUtils;
import org.patientview.patientview.logon.LogonUtils;
import org.patientview.patientview.model.EdtaCode;
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.UserMapping;
import org.patientview.patientview.news.NewsUtils;
import org.patientview.patientview.user.UserUtils;
import org.patientview.service.EdtaCodeManager;
import org.patientview.service.PatientManager;
import org.patientview.service.UnitManager;
import org.patientview.service.UserManager;
import org.springframework.web.struts.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class PatientDetailsAction extends ActionSupport {

    private EdtaCodeManager edtaCodeManager;
    private UserManager userManager;
    private PatientManager patientManager;
    private UnitManager unitManager;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        edtaCodeManager = getWebApplicationContext().getBean(EdtaCodeManager.class);
        userManager = getWebApplicationContext().getBean(UserManager.class);
        patientManager = getWebApplicationContext().getBean(PatientManager.class);
        unitManager = getWebApplicationContext().getBean(UnitManager.class);

        NewsUtils.putAppropriateNewsForViewingInRequest(request);

        // allow the logged in user to be overridden when viewing the site as a patient using an admin account
        User user = null;
        String param = mapping.getParameter();
        boolean isRadarGroup = false;
        if ("demographics".equals(param)) {
            isRadarGroup = true;
            user = UserUtils.retrieveUser(request);
        } else  if ("controlDemographics".equals(param)) {
            isRadarGroup = true;
            String username = (String) request.getSession().getAttribute("userBeingViewedUsername");
            user = userManager.get(username);
        } else {
            user = UserUtils.retrieveUser(request);
        }

        List<PatientDetails> patientDetails = patientManager.getPatientDetails(user.getUsername());
        PatientDetails patientDetail = getRadarPatientDetails(patientDetails);
        request.setAttribute("patientDetails", patientDetails);

        // add user object for ECS/SCS integration
        request.setAttribute("user", user);

        // this form is only used for ibd for now, so just check it exist before trying to use it
        if (form != null && form instanceof DynaActionForm && patientDetail != null) {
            // add the editable ibd only patient details
            DynaActionForm dynaForm = (DynaActionForm) form;
            // let's just store this info against the first patient object if there are many for this nhsno
            dynaForm.set("patientId", patientDetail.getPatient().getId());
            dynaForm.set("otherConditions", patientDetail.getPatient().getOtherConditions());
            dynaForm.set("email", userManager.getLoggedInUser().getEmail());
        }

        EdtaCodeUtils.addEdtaCodeToRequest("static", "staticLinks", request);

        // get unit links (EdtaCode) for each unit
        List<EdtaCode> unitLinks = new ArrayList<EdtaCode>();

        for (UserMapping userMapping : userManager.getUserMappings(user.getUsername())) {
            Unit unit = unitManager.get(userMapping.getUnitcode());
            if (unit != null) {
                EdtaCode unitLink = edtaCodeManager.getUnitLinks(unit);
                if (unitLink != null) {
                    unitLink.setDescription(unit.getName());
                    unitLinks.add(unitLink);
                }
            }
        }

        if (unitLinks.size() > 0) {
            request.setAttribute("unitLinks", unitLinks);
        }

        ActionUtils.setUpNavLink(mapping.getParameter(), request);

        return LogonUtils.logonChecks(mapping, request);
    }


    private PatientDetails getRadarPatientDetails(List<PatientDetails> patientDetails) {
        for (PatientDetails patientDetail : patientDetails) {
            if (patientDetail.getPatient().getSourceType().equalsIgnoreCase(SourceType.RADAR.getName())) {
                return patientDetail;
            }
        }

        return null;
    }
}
