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

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.actionutils.ActionUtils;
import org.patientview.patientview.logon.LogonUtils;
import org.patientview.patientview.model.ResultHeading;
import org.patientview.patientview.model.User;
import org.patientview.patientview.user.UserUtils;
import org.patientview.service.ResultHeadingManager;
import org.patientview.service.SecurityUserManager;
import org.springframework.web.struts.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ResultsInitAction extends ActionSupport {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ResultHeadingManager resultHeadingManager = getWebApplicationContext().getBean(ResultHeadingManager.class);
        SecurityUserManager securityUserManager = getWebApplicationContext().getBean(SecurityUserManager.class);
        User user = UserUtils.retrieveUser(request);

        if (user != null) {
            request.setAttribute("user", user);

            List<ResultHeading> resultsHeadingsList = resultHeadingManager.getAll(user.getUsername());

            if (!CollectionUtils.isEmpty(resultsHeadingsList)) {
                request.setAttribute("resultsHeadings", resultsHeadingsList);
                ResultHeading heading = resultHeadingManager.get(resultsHeadingsList.get(0).getHeadingcode());
                request.setAttribute("resultTypeHeading", heading);
                request.setAttribute("period", "0");
            }
        } else if (!securityUserManager.isRolePresent("patient")) {
            return LogonUtils.logonChecks(mapping, request, "control");
        }

        ActionUtils.setUpNavLink(mapping.getParameter(), request);

        return LogonUtils.logonChecks(mapping, request);
    }
}

