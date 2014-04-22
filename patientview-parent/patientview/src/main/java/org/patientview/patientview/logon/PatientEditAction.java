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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.model.Patient;
import org.patientview.model.Unit;
import org.patientview.patientview.exception.UsernameExistsException;
import org.patientview.patientview.model.User;
import org.patientview.service.PatientManager;
import org.patientview.service.UnitManager;
import org.patientview.service.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.struts.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.List;

public class PatientEditAction extends ActionSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientEditAction.class);

    private static final int YEAR_START = 0;
    private static final int YEAR_END = 4;
    private static final int MONTH_START = 5;
    private static final int MONTH_END = 7;
    private static final int DAY_START = 8;
    private static final int DAY_END = 10;
    private static final int HOUR_START = 11;
    private static final int HOUR_END = 13;
    private static final int MINUTE_START = 14;
    private static final int MINUTE_END = 16;
    private static final int SECOND_START = 17;
    private static final int SECOND_END = 19;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        UserManager userManager = getWebApplicationContext().getBean(UserManager.class);
        UnitManager unitManager = getWebApplicationContext().getBean(UnitManager.class);
        PatientManager patientManager = getWebApplicationContext().getBean(PatientManager.class);

        String nhsno = BeanUtils.getProperty(form, "nhsno");
        String unitcode = BeanUtils.getProperty(form, "unitcode");
        String overrideDuplicateNhsno = BeanUtils.getProperty(form, "overrideDuplicateNhsno");

        String mappingToFind = "";

        User user = null;

        try {
            user = createUser(form);
        } catch (UsernameExistsException uee) {
            LOGGER.info("Trying to update a user with an existing username");
            request.setAttribute(LogonUtils.USER_ALREADY_EXISTS,BeanUtils.getProperty(form, "username"));
            return mapping.findForward("input");
        }



        List<User> users = userManager.get(nhsno, BeanUtils.getProperty(form, "username"));

        if (!doesAnotherUserExist(users, user) && !overrideDuplicateNhsno.equals("on")) {
            request.setAttribute(LogonUtils.NHSNO_ALREADY_EXISTS, nhsno);
            mappingToFind = "input";
        } else {

            userManager.save(user);

            Unit unit = unitManager.get(unitcode);

            if (unit != null) {
                request.setAttribute("unit", unit);
            }

            List<Patient> patients = patientManager.getUnitPatientsAllWithTreatmentDao(unitcode);
            PagedListHolder pagedListHolder = new PagedListHolder(patients);
            request.getSession().setAttribute("patients", pagedListHolder);
            mappingToFind = "success";
        }

        return mapping.findForward(mappingToFind);
    }

    private boolean doesAnotherUserExist(List<User> matches, User user) {

        if (CollectionUtils.isNotEmpty(matches)) {
            for (User match : matches) {
                if (match.equals(user)) {
                    continue;
                } else {
                    return true;
                }
            }
        }
        return true;
    }


    private User createUser(ActionForm form) throws UsernameExistsException {
        UserManager userManager = getWebApplicationContext().getBean(UserManager.class);
        User user = null;
        try {
            user = userManager.get(Long.parseLong(BeanUtils.getProperty(form, "id")));

            if (user == null) {
                throw new UsernameNotFoundException("User is not found to edit");
            }

            if (!user.getUsername().equals(BeanUtils.getProperty(form, "username"))) {
                if (userManager.get(BeanUtils.getProperty(form, "username")) != null) {
                    throw new UsernameExistsException("Username already allocated");
                }
            }

            user.setUsername(BeanUtils.getProperty(form, "username"));
            user.setPassword(BeanUtils.getProperty(form, "password"));
            user.setFirstName(BeanUtils.getProperty(form, "firstName"));
            user.setLastName(BeanUtils.getProperty(form, "lastName"));
            user.setEmail(BeanUtils.getProperty(form, "email"));
            user.setEmailverified("true".equals(BeanUtils.getProperty(form, "emailverified")));
            user.setAccountlocked("true".equals(BeanUtils.getProperty(form, "accountlocked")));

            if (StringUtils.isNotEmpty(BeanUtils.getProperty(form, "failedlogons"))) {
                user.setFailedlogons(Integer.decode(BeanUtils.getProperty(form, "failedlogons")));
            }

            if (StringUtils.isNotEmpty(BeanUtils.getProperty(form, "lastlogon"))) {
                user.setLastlogon(createDatestamp(BeanUtils.getProperty(form, "lastlogon")).getTime());
            }
            user.setDummypatient("true".equals(BeanUtils.getProperty(form, "dummypatient")));
            user.setFirstlogon("true".equals(BeanUtils.getProperty(form, "firstlogon")));

        } catch (IllegalAccessException iae) {
            LOGGER.debug("BeanUtil failed", iae);
        } catch (NoSuchMethodException nsm) {
            LOGGER.debug("BeanUtil failed", nsm);
        } catch (InvocationTargetException ite) {
            LOGGER.debug("BeanUtil failed", ite);
        }

        return user;
    }

    private static Calendar createDatestamp(String dateTimeString) {
        Calendar datestamp = null;

        if (!"".equals(dateTimeString)) {
            datestamp = Calendar.getInstance();

            int year = Integer.parseInt(dateTimeString.substring(YEAR_START, YEAR_END));
            int month = Integer.parseInt(dateTimeString.substring(MONTH_START, MONTH_END));
            int day = Integer.parseInt(dateTimeString.substring(DAY_START, DAY_END));

            datestamp.set(year, month - 1, day, 0, 0, 0);
            datestamp.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateTimeString.substring(HOUR_START, HOUR_END)));
            datestamp.set(Calendar.MINUTE, Integer.parseInt(dateTimeString.substring(MINUTE_START, MINUTE_END)));

            if (dateTimeString.length() == SECOND_END) {
                datestamp.set(Calendar.SECOND, Integer.parseInt(dateTimeString.substring(SECOND_START, SECOND_END)));
            }

            datestamp.set(Calendar.MILLISECOND, 0);
        }
        return datestamp;
    }


}
