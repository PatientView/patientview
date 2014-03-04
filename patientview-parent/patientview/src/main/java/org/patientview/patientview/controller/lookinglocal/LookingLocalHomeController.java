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
package org.patientview.patientview.controller.lookinglocal;

import org.patientview.patientview.controller.BaseController;
import org.patientview.patientview.controller.Routes;
import org.patientview.patientview.model.User;
import org.patientview.security.impl.PatientViewPasswordEncoder;
import org.patientview.security.model.SecurityUser;
import org.patientview.service.SecurityUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *  Looking local home controller
 */
@Controller
public class LookingLocalHomeController extends BaseController {

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private SecurityUserManager securityUserManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(LookingLocalHomeController.class);
    /**
     * Deal with the URIs "/lookinglocal/home"
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_HOME)
    @ResponseBody
    public void getHomeXml(HttpServletResponse response) {
        LOGGER.debug("home start");
        try {
            LookingLocalUtils.getHomeXml(response);
        } catch (Exception e) {
            LOGGER.error("Could not create home screen response output stream{}" + e);
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/auth", check POSTed credentials and forward to main or error
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_AUTH)
    @ResponseBody
    public void getAuth(HttpServletRequest request,
                        @RequestParam(value = "username", required = false) String username,
                        @RequestParam(value = "password", required = false) String password,
                        HttpServletResponse response) {
        LOGGER.debug("auth start");

        PatientViewPasswordEncoder encoder = new PatientViewPasswordEncoder();
        User user = securityUserManager.get(username);

        if (user != null) {
            if (user.getPassword().equals(encoder.encode(password))) {
                // Authenticate user manually and add to session
                SecurityUser userLogin = (SecurityUser) userDetailsService.loadUserByUsername(username);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userLogin,
                        userLogin.getPassword(), userLogin.getAuthorities()));
                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
                LOGGER.debug("auth passed");
                try {
                    LookingLocalUtils.getAuthXml(response);
                } catch (Exception e) {
                    LOGGER.error("Could not create home screen response output stream{}" + e);
                }
            } else {
                LOGGER.debug("auth failed, password");
                try {
                    LookingLocalUtils.getErrorXml(response);
                } catch (Exception e) {
                    LOGGER.error("Could not create home screen response output stream{}" + e);
                }
            }
        } else {
            LOGGER.debug("auth failed, user null");
            try {
                LookingLocalUtils.getErrorXml(response);
            } catch (Exception e) {
                LOGGER.error("Could not create home screen response output stream{}" + e);
            }
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/error"
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_ERROR)
    @ResponseBody
    public void getErrorScreenXml(HttpServletResponse response) {
        LOGGER.debug("error start");
        try {
            LookingLocalUtils.getErrorXml(response);
        } catch (Exception e) {
            LOGGER.error("Could not create main screen response output stream{}" + e);
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/secure/main"
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_MAIN)
    @ResponseBody
    public void getMainScreenXml(HttpServletResponse response) {
        LOGGER.debug("main start");
        try {
            LookingLocalUtils.getMainXml(response);
        } catch (Exception e) {
            LOGGER.error("Could not create main screen response output stream{}" + e);
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/secure/details"
     * @param request
     * @param response
     * @param selection User option selection
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_DETAILS)
    @ResponseBody
    public void getDetailsScreenXml(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "selection", required = false) String selection) {
        LOGGER.debug("details start");
        try {
            if (selection != null) {
                switch (Integer.parseInt(selection)) {
                    case LookingLocalUtils.OPTION_1 : LookingLocalUtils.getMyDetailsXml(request, response);
                        break;
                    case LookingLocalUtils.OPTION_2 : LookingLocalUtils.getMedicalResultsXml(request, response);
                        break;
                    case LookingLocalUtils.OPTION_3 : LookingLocalUtils.getDrugsXml(request, response);
                        break;
                    case LookingLocalUtils.OPTION_4 : LookingLocalUtils.getLettersXml(request, response);
                        break;
                    default:break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Could not create details response output stream{}" + e);
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/secure/resultsDisplay"
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_RESULTS_DISPLAY)
    @ResponseBody
    public void getMedicalResultsXml(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam(value = "selection", required = false) String selection) {
        LOGGER.debug("resultsDisplay start");
        try {
            if (selection != null) {
                LookingLocalUtils.getResultsDetailsXml(request, response, selection);
            }
        } catch (Exception e) {
            LOGGER.error("Could not create medical result details response output stream{}" + e);
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/secure/letterDisplay"
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_LETTER_DISPLAY)
    @ResponseBody
    public void getLetterXml(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "selection", required = false) String selection) {

        try {
            if (selection != null) {
                LookingLocalUtils.getLetterDetailsXml(request, response, selection);
            }
        } catch (Exception e) {
            LOGGER.error("Could not create letter details response output stream{}" + e);
        }
    }
}
