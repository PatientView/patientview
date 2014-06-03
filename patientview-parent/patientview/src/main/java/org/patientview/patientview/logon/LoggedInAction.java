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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.actionutils.ActionUtils;
import org.patientview.patientview.model.User;
import org.patientview.patientview.news.NewsUtils;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.UserManager;
import org.patientview.utils.LegacySpringUtils;
import org.springframework.web.struts.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class LoggedInAction extends ActionSupport {

    private final DateFormat format = new SimpleDateFormat("d MMM yyyy HH:mm");

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        SecurityUserManager securityUserManager = getWebApplicationContext().getBean(SecurityUserManager.class);
        UserManager userManager = getWebApplicationContext().getBean(UserManager.class);

        if ("/back_to_admin.do".equals(request.getRequestURI())) {
            request.getSession().setAttribute("userBeingViewedUsername", null);
        }

        String forward = "";
        ActionUtils.setUpNavLink(mapping.getParameter(), request);
        NewsUtils.putAppropriateNewsForViewingInRequest(request);
        User user = userManager.getLoggedInUser();

        if (user != null) {

            // ECR enabled for that user's unit?
            request.setAttribute("ecrEnabled", userManager.getEcrEnabled(user));

            request.setAttribute("user", user);
            final String role = userManager.getCurrentSpecialtyRole(user);

            // Set the specialty is the session. This isn't great but the specialty TLD is worse. A lot worse.
            request.getSession().setAttribute("specialty", userManager.getCurrentSpecialty(user));

            // Is user patient or admin?
            if ("patient".equalsIgnoreCase(role)) {
                request.setAttribute("isPatient", true);
            }
            if ((user.getLastlogon() != null)) {
                request.setAttribute("lastLogin", format.format(user.getLastlogon()));
            }
            user.setLastlogon(new Date());

            LegacySpringUtils.getUserManager().save(user);

            if ("patient".equalsIgnoreCase(role)) {

                String nhsno = LegacySpringUtils.getUserManager().getUsersRealNhsNoBestGuess(user.getUsername());

                Map.Entry<String, Date> testTestRange = LegacySpringUtils.getPatientManager().getLatestTestResultUnit(
                        nhsno);

                String lastDataDate = null;
                String testTestRangeKey = null;

                if (testTestRange != null) {
                    lastDataDate = format.format(testTestRange.getValue().getTime());
                    testTestRangeKey = testTestRange.getKey();
                }

                request.setAttribute("lastDataDate", lastDataDate);
                request.setAttribute("lastDataFrom", testTestRangeKey);

                forward = "patient";
            } else {
                forward = "admin";
            }
        }
        return LogonUtils.logonChecks(mapping, request, forward);
    }
}
