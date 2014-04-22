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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.patientview.exception.UsernameExistsException;
import org.patientview.patientview.model.SpecialtyUserRole;
import org.patientview.patientview.model.User;
import org.patientview.service.UnitManager;
import org.patientview.service.UserManager;
import org.patientview.utils.LegacySpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.struts.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class UnitUserEditAction extends ActionSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitUserEditAction.class);

    public ActionForward execute(
        ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        UserManager userManager = getWebApplicationContext().getBean(UserManager.class);
        UnitManager unitManager = getWebApplicationContext().getBean(UnitManager.class);


        try {
            userManager.save(createUser(form));
        } catch (UsernameExistsException uee) {
            LOGGER.info("Tried to allocate a user with ane existing username");
            request.setAttribute(LogonUtils.USER_ALREADY_EXISTS, BeanUtils.getProperty(form, "username"));
            return mapping.findForward("input");
        }


        String unitcode = BeanUtils.getProperty(form, "unitcode");
        request.setAttribute("unit", unitManager.get(unitcode));
        List unitUsers = LegacySpringUtils.getUnitManager().getUnitUsers(unitcode);
        PagedListHolder pagedListHolder = new PagedListHolder(unitUsers);
        request.getSession().setAttribute("unitUsers", pagedListHolder);

        return LogonUtils.logonChecks(mapping, request);
    }

    private User createUser(ActionForm form) {

        UserManager userManager = getWebApplicationContext().getBean(UserManager.class);
        User user = null;
        try {
            user = userManager.get(Long.parseLong(BeanUtils.getProperty(form, "id")));

            if (user == null) {
                throw new UsernameNotFoundException("User is not found to edit");
            }

            user.setUsername(BeanUtils.getProperty(form, "username"));
            user.setFirstName(BeanUtils.getProperty(form, "firstName"));
            user.setLastName(BeanUtils.getProperty(form, "lastName"));
            user.setEmail(BeanUtils.getProperty(form, "email"));
            user.setEmailverified("true".equals(BeanUtils.getProperty(form, "emailverified")));
            user.setFirstlogon("true".equals(BeanUtils.getProperty(form, "firstlogon")));
            user.setIsrecipient("true".equals(BeanUtils.getProperty(form, "isrecipient")));
            user.setIsclinician("true".equals(BeanUtils.getProperty(form, "isclinician")));

            String role = BeanUtils.getProperty(form, "role");
            updateRole(user, role);

        } catch (IllegalAccessException iae) {
            LOGGER.error("Unable to create user from input", iae);
        } catch (InvocationTargetException ite) {
            LOGGER.error("Unable to create user from input", ite);
        } catch (NoSuchMethodException nsm) {
            LOGGER.error("Unable to create user from input", nsm);
        }


        return user;


    }

    /**
     * Find a specialty user role for the logged in user and update. This should go in the manager but resolution of
     * which specialty user record should be updated from the form should be found first.
     *
     * @param user
     * @param role
     * @return
     */
    private void updateRole(User user, String role) {

        UserManager userManager = getWebApplicationContext().getBean(UserManager.class);

        if (CollectionUtils.isNotEmpty(user.getSpecialtyUserRoles())) {

            for (SpecialtyUserRole specialtyUserRole : user.getSpecialtyUserRoles()) {

                if (specialtyUserRole.getSpecialty()
                        == userManager.getCurrentSpecialty(userManager.getLoggedInUser())) {
                    if (role.equalsIgnoreCase(specialtyUserRole.getRole())) {
                        specialtyUserRole.setRole(role);
                    }

                }
            }

        }

    }

}
