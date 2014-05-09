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
import org.apache.commons.lang.StringUtils;
import org.patientview.patientview.logon.LogonUtils;
import org.patientview.patientview.model.LogEntry;
import org.patientview.patientview.utils.TimestampUtils;
import org.patientview.service.LogEntryManager;
import org.patientview.service.UnitManager;
import org.patientview.util.CommonUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.struts.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LogViewAction extends ActionSupport {

    private UnitManager unitManager;
    private LogEntryManager logEntryManager;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        unitManager = getWebApplicationContext().getBean(UnitManager.class);
        logEntryManager = getWebApplicationContext().getBean(LogEntryManager.class);

        Calendar startdate = determineStartDate(BeanUtils.getProperty(form, "startdate"));
        Calendar enddate = determineEndDate(BeanUtils.getProperty(form, "enddate"));
        String nhsno = BeanUtils.getProperty(form, "nhsno");
        nhsno = CommonUtils.cleanNhsNumber(nhsno);
        String user = BeanUtils.getProperty(form, "user");
        String actor = BeanUtils.getProperty(form, "actor");
        String action = BeanUtils.getProperty(form, "action");
        String unitcode = BeanUtils.getProperty(form, "unitcode");
        String order = BeanUtils.getProperty(form, "order");
        Boolean orderByAsc = null;

        // get order mark, false: order by date desc, true : order by date asc
        if (StringUtils.isNotEmpty(order)) {
            if ("false".equals(order)) {
                orderByAsc = false;
            } else {
                orderByAsc = true;
            }
        }

        request.setAttribute("log"
                , getLogEntries(nhsno, user, actor, action, unitcode, startdate, enddate, orderByAsc));

        request.getSession().setAttribute("units", unitManager.getLoggedInUsersUnits(
                new String[]{"PATIENT"}, new String[]{}));

        LoggingUtils.defaultDatesInForm(form, startdate, enddate);

        if (orderByAsc == null) {
            orderByAsc = false;
        } else {
            if (orderByAsc) {
                orderByAsc = false;
            } else {
                orderByAsc = true;
            }
        }
        request.setAttribute("order", orderByAsc);

        return LogonUtils.logonChecks(mapping, request);
    }

    private Calendar determineStartDate(String startDateString)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (StringUtils.isEmpty(startDateString)) {
            return LoggingUtils.getDefaultStartDateForLogQuery();
        } else {
            return TimestampUtils.createTimestampStartDay(startDateString);
        }
    }

    private Calendar determineEndDate(String endDateString)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (StringUtils.isEmpty(endDateString)) {
            return LoggingUtils.getDefaultEndDateForLogQuery();
        } else {
            return TimestampUtils.createTimestampEndDay(endDateString);
        }
    }

    private List<LogEntry> getLogEntries(String nhsno, String user, String actor, String action, String unitcode,
                               Calendar startdate, Calendar enddate, Boolean orderByAsc) throws Exception {
        List<LogEntry> logEntries = new ArrayList();

        if (orderByAsc != null) {
            logEntries = logEntryManager.getWithNhsNo(nhsno, user, actor, action, unitcode,
                    startdate, enddate, orderByAsc);
        }

        return logEntries;
    }
}
