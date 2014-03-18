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
import org.patientview.model.Unit;
import org.patientview.patientview.logging.AddLog;
import org.patientview.patientview.model.User;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.UnitManager;
import org.patientview.service.UserManager;
import org.springframework.web.struts.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PatientHideAction extends ActionSupport {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        UnitManager unitManager = getWebApplicationContext().getBean(UnitManager.class);
        UserManager userManager = getWebApplicationContext().getBean(UserManager.class);
        SecurityUserManager securityUserManager = getWebApplicationContext().getBean(SecurityUserManager.class);

        String username = BeanUtils.getProperty(form, "username");
        User user = userManager.get(username);

        String mappingToFind = "";

        if (user != null) {
            user.setFailedlogons(0);
            user.setAccountlocked(true);
            user.setAccounthidden(true);
            userManager.save(user);

            AddLog.addLog(securityUserManager.getLoggedInUsername(), AddLog.PASSWORD_LOCKED,
                    user.getUsername(), "", userManager.getUsersRealUnitcodeBestGuess(username), "");

            AddLog.addLog(securityUserManager.getLoggedInUsername(), AddLog.PATIENT_HIDE,
                    user.getUsername(), "", userManager.getUsersRealUnitcodeBestGuess(username), "");

            mappingToFind = "success";
        }

        List<Unit> units = unitManager.getAll(false);
        request.setAttribute("units", units);
        request.setAttribute("user", user);

        return mapping.findForward(mappingToFind);
    }
}