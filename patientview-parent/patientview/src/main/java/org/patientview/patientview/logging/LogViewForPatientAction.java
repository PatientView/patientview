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

package org.patientview.patientview.logging;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.patientview.logon.LogonUtils;
import org.patientview.patientview.model.Unit;
import org.patientview.patientview.model.User;
import org.patientview.patientview.unit.UnitUtils;
import org.patientview.utils.LegacySpringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LogViewForPatientAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Calendar startdate = LoggingUtils.getDefaultStartDateForLogQuery();
        Calendar enddate = LoggingUtils.getDefaultEndDateForLogQuery();
        String nhsno = BeanUtils.getProperty(form, "nhsno");
        List log = getLogEntries(nhsno, startdate, enddate);
        request.setAttribute("log", log);
        User user = LegacySpringUtils.getUserManager().retrieveUser(request);
        List<Unit> usersUnits
                = LegacySpringUtils.getUnitManager().
                getLoggedInUsersUnits(new String[]{UnitUtils.PATIENT_ENTERS_UNITCODE}, new String[]{});

        request.getSession().setAttribute("units", usersUnits);

        LoggingUtils.defaultDatesInForm(form, startdate, enddate);
        return LogonUtils.logonChecks(mapping, request);
    }

    private List getLogEntries(String nhsno, Calendar startdate, Calendar enddate) throws Exception {
        List logEntries = new ArrayList();
        if (nhsno != null && !nhsno.equals("")) {
            logEntries = LegacySpringUtils.getLogEntryManager().getWithNhsNo(nhsno, startdate, enddate, null);
        }
        return logEntries;
    }
}
