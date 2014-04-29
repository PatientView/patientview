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

    private UserManager userManager;
    private PatientManager patientManager;
    private SecurityUserManager securityUserManager;
    private UnitManager unitManager;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        userManager = getWebApplicationContext().getBean(UserManager.class);
        patientManager = getWebApplicationContext().getBean(PatientManager.class);
        securityUserManager = getWebApplicationContext().getBean(SecurityUserManager.class);
        unitManager = getWebApplicationContext().getBean(UnitManager.class);

        // get properties from form elements
        String username = BeanUtils.getProperty(form, "username");
        String email = BeanUtils.getProperty(form, "email");
        String unitcode = BeanUtils.getProperty(form, "unitcode");
        String role = BeanUtils.getProperty(form, "role");

        if ("unitstaff".equalsIgnoreCase(role)) {
            Unit unit = unitManager.get(unitcode);
            if ("radargroup".equalsIgnoreCase(unit.getSourceType())) {
                request.setAttribute("roleInRadargroup", unit.getName());
                return mapping.findForward("input");
            }
        }

        // check if existing username is patient, only want to add admins with unique usernames
        if (!CollectionUtils.isEmpty(patientManager.getByUsername(username))) {
            request.setAttribute("patientAlreadyExists", username);
            return mapping.findForward("input");
        }

        // no existing patients with this username, get list of user mappings
        List<UserMapping> usermappingsAllSpecialties = userManager.getUserMappingsIgnoreSpecialty(username);
        List<UserMapping> usermappingsThisSpecialty = userManager.getUserMappings(username);

        if (!usermappingsAllSpecialties.isEmpty()) {

            // check if user exists has mapping in currently specialty
            if (!usermappingsThisSpecialty.isEmpty()) {
                // user has mappings in this specialty
                UserMapping userMappingThisUnit = getUserMappingThisUnit(username, unitcode);

                // check if user has mapping for current unit
                if (userMappingThisUnit != null) {
                    // user already exists in unit requested
                    request.setAttribute(LogonUtils.USER_ALREADY_EXISTS, username);
                    return mapping.findForward("input");
                } else {
                    // user exists in another unit
                    request.setAttribute("currentUnitCodes", getUnitCodeDisplay(usermappingsAllSpecialties));
                    request.setAttribute("usermapping", new UserMapping(username, unitcode, null));
                    request.setAttribute(LogonUtils.USER_ALREADY_EXISTS, username);
                    return mapping.findForward("existinguser");
                }

            } else {
                // user has mappings in other specialties but not this one
                request.setAttribute("currentUnitCodes"
                        , "This user does not currently belong to any units in this specialty.");
                request.setAttribute("usermapping", new UserMapping(username, unitcode, null));
                request.setAttribute(LogonUtils.USER_ALREADY_EXISTS_OTHER_SPECIALTY, username);
                return mapping.findForward("existinguser");
            }
        } else {
            // no users exist with this username
            // check if email already on system, means user already exists but no user mapping
            List<User> existingUsersByEmail = userManager.getByEmailAddress(email);
            if (!CollectionUtils.isEmpty(existingUsersByEmail)) {
                // users exist with this email
                User existingUserByEmail = existingUsersByEmail.get(0);
                String existingUsername = existingUserByEmail.getUsername();
                usermappingsThisSpecialty = userManager.getUserMappings(existingUsername);

                // check if found user (by email) has mapping in currently specialty
                if (!usermappingsThisSpecialty.isEmpty()) {
                    // found user (by email) has mappings in this specialty
                    UserMapping userMappingThisUnit = getUserMappingThisUnit(existingUsername, unitcode);

                    // check if user has mapping for current unit
                    if (userMappingThisUnit != null) {
                        // found user (by email) already exists in unit requested
                        request.setAttribute(LogonUtils.USER_ALREADY_EXISTS_WITH_EMAIL, email);
                        return mapping.findForward("input");
                    } else {
                        // found user (by email) exists in another unit in this specialty
                        request.setAttribute("currentUnitCodes", getUnitCodeDisplay(usermappingsThisSpecialty));
                        request.setAttribute("usermapping", new UserMapping(existingUsername, unitcode, null));
                        request.setAttribute(LogonUtils.USER_ALREADY_EXISTS_WITH_EMAIL, email);
                        return mapping.findForward("existinguser");
                    }
                } else {
                    // found user (by email) has mappings in other specialties but not this one
                    request.setAttribute("usermapping", new UserMapping(existingUsername, unitcode, null));
                    request.setAttribute("currentUnitCodes"
                            , "This user does not currently belong to any units in this specialty.");
                    request.setAttribute(LogonUtils.USER_ALREADY_EXISTS_WITH_EMAIL_OTHER_SPECIALTY, email);
                    return mapping.findForward("existinguser");
                }
            }

            // user doesn't exist already, get relevant details from form and generate password
            String password = LogonUtils.generateNewPassword();
            String firstName = BeanUtils.getProperty(form, "firstName");
            String lastName = BeanUtils.getProperty(form, "lastName");
            boolean isRecipient = "true".equals(BeanUtils.getProperty(form, "isrecipient"));
            boolean feedbackRecipient = "true".equals(BeanUtils.getProperty(form, "feedbackRecipient"));
            boolean isClinician = "true".equals(BeanUtils.getProperty(form, "isclinician"));
            boolean sharedThoughtAdministrator
                    = "true".equals(BeanUtils.getProperty(form, "sharedThoughtAdministrator"));

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
            unitAdmin.setFeedbackRecipient(feedbackRecipient);
            unitAdmin.setIsclinician(isClinician);
            unitAdmin.setSharedThoughtAdministrator(sharedThoughtAdministrator);
            unitAdmin.setAccounthidden(false);
            unitAdmin.setAccountlocked(false);

            // create the new user
            UnitAdmin hashedUnitAdmin = (UnitAdmin) unitAdmin.clone();
            hashedUnitAdmin.setPassword(LogonUtils.hashPassword(hashedUnitAdmin.getPassword()));
            User user = userManager.saveUserFromUnitAdmin(hashedUnitAdmin, unitcode);

            // create mappings in radar if they don't already exist
            if (!userManager.userExistsInRadar(user.getId())) {
                userManager.createProfessionalUserInRadar(user, unitcode);
            }

            // note this check is not necessarily required as user mapping should be created
            // in userManager.saveUserFromUnitAdmin()
            if (CollectionUtils.isEmpty(userManager.getUserMappings(username, unitcode))) {
                UserMapping userMappingNew = new UserMapping(username, unitcode, null);
                userManager.save(userMappingNew);
                request.setAttribute("usermapping", userMappingNew);
            }

            AddLog.addLog(securityUserManager.getLoggedInUsername(), AddLog.ADMIN_ADD, unitAdmin.getUsername(),
                    "", unitcode, "");
            EmailVerificationUtils.createEmailVerification(hashedUnitAdmin.getUsername(), hashedUnitAdmin.getEmail(),
                    request);
            request.setAttribute("adminuser", unitAdmin);
            return mapping.findForward("success");
        }
    }

    /**
     * Gets a single UserMapping based on a user's username and a unitcode
     * @param username The user's username
     * @param unitcode The unitcode of the Unit to search for
     * @return UserMapping if exists, otherwise null
     */
    private UserMapping getUserMappingThisUnit(String username, String unitcode) {
        List<UserMapping> userMappingsThisUnit = userManager.getUserMappings(username, unitcode);
        UserMapping userMappingThisUnit = null;
        if (!CollectionUtils.isEmpty(userMappingsThisUnit)) {
            userMappingThisUnit = userMappingsThisUnit.get(0);
        }
        return userMappingThisUnit;
    }

    /**
     * Gets correctly formatted list of unit codes given a List of UserMapping
     * @param userMappings List of user mappings
     * @return Correctly formatted list of unit codes
     */
    private String getUnitCodeDisplay(List<UserMapping> userMappings) {
        // get string list of current user unit mappings and format into string for information
        StringBuilder unitCodes = new StringBuilder();
        for (UserMapping userMap : userMappings) {
            if (userMap.getUnitcode().length() > 0) {
                unitCodes.append(userMap.getUnitcode());
                unitCodes.append(" (");
                unitCodes.append(userMap.getSpecialty().getName());
                unitCodes.append("), ");
            }
        }

        return unitCodes.substring(0, unitCodes.length() - 2);
    }
}
