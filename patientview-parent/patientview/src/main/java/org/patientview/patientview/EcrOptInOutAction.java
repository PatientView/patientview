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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.patientview.logon.LogonUtils;
import org.patientview.patientview.model.User;
import org.patientview.patientview.user.UserUtils;
import org.patientview.service.UserManager;
import org.springframework.web.struts.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EcrOptInOutAction extends ActionSupport {

    private UserManager userManager;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        userManager = getWebApplicationContext().getBean(UserManager.class);

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

        // add user object for ECS/SCS integration
        request.setAttribute("user", user);

        String buttonAction = request.getParameter("buttonAction");

        if (StringUtils.isNotEmpty(buttonAction)) {
            if (buttonAction.equals("Opt In")) {
                userManager.setEcrOptInStatus(user, true, false);
            } else if (buttonAction.equals("Opt Out")) {
                userManager.setEcrOptInStatus(user, false, false);
            } else if (buttonAction.equals("Never Ask Me Again")) {
                userManager.setEcrOptInStatus(user, false, true);
            }
        }

        return LogonUtils.logonChecks(mapping, request);
    }
}
