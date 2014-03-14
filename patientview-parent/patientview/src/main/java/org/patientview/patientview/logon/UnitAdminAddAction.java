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

import org.apache.commons.collections.CollectionUtils;
import org.patientview.patientview.logging.AddLog;
import org.patientview.model.Unit;
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.UserMapping;
import org.patientview.patientview.unit.UnitUtils;
import org.patientview.patientview.user.EmailVerificationUtils;
import org.patientview.service.PatientManager;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.UnitManager;
import org.patientview.service.UserManager;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.struts.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UnitAdminAddAction extends ActionSupport {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        UserManager userManager = getWebApplicationContext().getBean(UserManager.class);
        PatientManager patientManager = getWebApplicationContext().getBean(PatientManager.class);
        SecurityUserManager securityUserManager = getWebApplicationContext().getBean(SecurityUserManager.class);
        UnitManager unitManager = getWebApplicationContext().getBean(UnitManager.class);

        // get properties from form elements and generate new passwords
        String username = BeanUtils.getProperty(form, "username");
        String password = LogonUtils.generateNewPassword();
        String firstName = BeanUtils.getProperty(form, "firstName");
        String lastName = BeanUtils.getProperty(form, "lastName");
        String email = BeanUtils.getProperty(form, "email");
        String unitcode = BeanUtils.getProperty(form, "unitcode");
        String role = BeanUtils.getProperty(form, "role");
        boolean isRecipient = "true".equals(BeanUtils.getProperty(form, "isrecipient"));
        boolean isClinician = "true".equals(BeanUtils.getProperty(form, "isclinician"));

        // create new UnitAdmin (extended from Logon but currently with no extra fields/methods)
        UnitAdmin unitAdmin = new UnitAdmin();
        unitAdmin.setUsername(username);
        unitAdmin.setPassword(password);
        unitAdmin.setFirstName(firstName);
        unitAdmin.setLastName(lastName);
        unitAdmin.setEmail(email);
        unitAdmin.setEmailverified(false);
        unitAdmin.setRole(role);
        unitAdmin.setFirstlogon(true);
        unitAdmin.setIsrecipient(isRecipient);
        unitAdmin.setIsclinician(isClinician);

        if ("unitstaff".equalsIgnoreCase(role)) {
            Unit unit = unitManager.get(unitcode);
            if ("radargroup".equalsIgnoreCase(unit.getSourceType())) {
                request.setAttribute("roleInRadargroup", unit.getName());
                request.setAttribute("adminuser", unitAdmin);
                UnitUtils.setUserUnits(request);
                return mapping.findForward("input");
            }
        }

        List<UserMapping> usermappingList = userManager.getUserMappings(username);

        // check if existing username is patient, only want to add admins with unique usernames
        if (!CollectionUtils.isEmpty(patientManager.getByUsername(username))) {
            // patient exists with this username
            unitAdmin.setUsername("");
            request.setAttribute("adminuser", unitAdmin);
            request.setAttribute("patientAlreadyExists", username);
            return mapping.findForward("input");
        } else {
            // no existing patients with this username
            String mappingToFind;
            if (!usermappingList.isEmpty()) {

                // Note: legacy code assumes that there is a unique results here
                List<UserMapping> userMappings = userManager.getUserMappings(username, unitcode);
                UserMapping userMapping = null;
                if (!CollectionUtils.isEmpty(userMappings)) {
                    userMapping = userMappings.get(0);
                }

                if (userMapping != null) {
                    // user exists in unit requested
                    request.setAttribute(LogonUtils.USER_ALREADY_EXISTS, username);
                    unitAdmin.setUsername("");
                    UnitUtils.setUserUnits(request);
                    mappingToFind = "input";
                } else {
                    // user exists but in other units
                    UserMapping userMappingNew = new UserMapping(username, unitcode, "");

                    // get string list of current user unit mappings and format into string for information
                    StringBuilder currentUnitCodes = new StringBuilder();
                    for (UserMapping existingUserMapping : usermappingList) {
                        if (existingUserMapping.getUnitcode().length() > 0) {
                            currentUnitCodes.append(existingUserMapping.getUnitcode() + ", ");
                        }
                    }

                    request.setAttribute("currentUnitCodes", currentUnitCodes.substring(0,
                            currentUnitCodes.length() - 2));
                    request.setAttribute("usermapping", userMappingNew);
                    mappingToFind = "existinguser";
                }
            } else {
                // create the new user
                UnitAdmin hashedUnitAdmin = (UnitAdmin) unitAdmin.clone();
                hashedUnitAdmin.setPassword(LogonUtils.hashPassword(hashedUnitAdmin.getPassword()));
                User user = userManager.saveUserFromUnitAdmin(hashedUnitAdmin, unitcode);

                // create mappings in radar if they don't already exist
                if (!userManager.userExistsInRadar(user.getId())) {
                    userManager.createProfessionalUserInRadar(user, unitcode);
                }

                if (CollectionUtils.isEmpty(userManager.getUserMappings(username, unitcode))) {
                    UserMapping userMappingNew = new UserMapping(username, unitcode, "");
                    userManager.save(userMappingNew);
                    request.setAttribute("usermapping", userMappingNew);
                }

                AddLog.addLog(securityUserManager.getLoggedInUsername(), AddLog.ADMIN_ADD, unitAdmin.getUsername(),
                        "", unitcode, "");
                EmailVerificationUtils.createEmailVerification(hashedUnitAdmin.getUsername(),
                        hashedUnitAdmin.getEmail(), request);
                mappingToFind = "success";
            }
            request.setAttribute("adminuser", unitAdmin);
            return mapping.findForward(mappingToFind);
        }
    }

}
