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

import org.apache.commons.collections.CollectionUtils;
import org.patientview.model.Specialty;
import org.patientview.patientview.controller.BaseController;
import org.patientview.patientview.controller.Routes;
import org.patientview.patientview.model.SpecialtyUserRole;
import org.patientview.patientview.model.User;
import org.patientview.security.impl.PatientViewPasswordEncoder;
import org.patientview.security.model.SecurityUser;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.UserManager;
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
import java.util.List;

/**
 *  Looking local home controller
 */
@Controller
public class LookingLocalHomeController extends BaseController {

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private UserManager userManager;

    @Inject
    private SecurityUserManager securityUserManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(LookingLocalHomeController.class);

    private int page = 0, itemsPerPage = 5, lineLength = 50, linesPerPage = 8;
    private String letterSelection = null;
    private String resultSelection = null;

    /**
     * Deal with the URIs "/lookinglocal/home"
     * @param response HTTP response
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
     * Deal with the URIs "/lookinglocal/auth", check POSTed credentials
     * @param request HTTP request
     * @param response HTTP response
     * @param username User entered username
     * @param password User entered password
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

                // Authenticate user manually
                SecurityUser userLogin = (SecurityUser) userDetailsService.loadUserByUsername(username);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userLogin,
                        userLogin.getPassword(), userLogin.getAuthorities()));

                // manage extra authentication success handlers manually (usually
                // managed by PatientViewAuthenticationSuccessHandler.onAuthenticationSuccess)
                SecurityUser securityUser = (SecurityUser) securityContext.getAuthentication().getPrincipal();
                List<SpecialtyUserRole> specialtyUserRoles = userManager.getSpecialtyUserRoles(user);

                if (CollectionUtils.isNotEmpty(specialtyUserRoles)) {
                    Specialty specialty = specialtyUserRoles.get(0).getSpecialty();
                    securityUser.setSpecialty(specialty);
                    // manually add to session
                    HttpSession session = request.getSession(true);
                    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
                    LOGGER.debug("auth passed");
                    try {
                        LookingLocalUtils.getAuthXml(response);
                    } catch (Exception e) {
                        LOGGER.error("Could not create home screen response output stream{}" + e);
                    }

                } else {
                    LOGGER.debug("auth failed, no specialties");
                    try {
                        LookingLocalUtils.getErrorXml(response);
                    } catch (Exception e) {
                        LOGGER.error("Could not create home screen response output stream{}" + e);
                    }
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
     * @param response HTTP response
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
     * @param response HTTP response
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
     * @param request HTTP request
     * @param response HTTP response
     * @param selection User option selection
     * @param buttonPressed button according to Looking Local, used for "Back", "More" etc buttons
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_DETAILS)
    @ResponseBody
    public void getDetailsScreenXml(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "selection", required = false) String selection,
                                    @RequestParam(value = "buttonPressed", required = false) String buttonPressed) {
        LOGGER.debug("details start");
        page = 0;

        try {
            if (buttonPressed != null) {
                if (buttonPressed.equals("left")) {
                    getMainScreenXml(response);
                } else if (selection != null) {
                switch (Integer.parseInt(selection)) {
                    case LookingLocalUtils.OPTION_1 :
                        getMyDetailsScreenXml(request, response, null);
                    case LookingLocalUtils.OPTION_2 : LookingLocalUtils.getMedicalResultsXml(request, response);
                        break;
                    case LookingLocalUtils.OPTION_3 :
                        getDrugsScreenXml(request, response, null);
                    case LookingLocalUtils.OPTION_4 :
                        getLettersScreenXml(request, response, null, null);
                    default : getErrorScreenXml(response);
                    }
                } else {
                    getErrorScreenXml(response);
                }
            } else {
                getErrorScreenXml(response);
            }
        } catch (Exception e) {
            getErrorScreenXml(response);
            LOGGER.error("Could not create details response output stream: " + e.toString());
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/secure/myDetails"
     * @param request HTTP request
     * @param response HTTP response
     * @param buttonPressed button according to Looking Local, used for "Back", "More" etc buttons
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_MY_DETAILS)
    @ResponseBody
    public void getMyDetailsScreenXml(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "buttonPressed", required = false) String buttonPressed) {
        LOGGER.debug("my details start");

        try {
            if (buttonPressed != null) {
                if (buttonPressed.equals("right")) {
                    page++;
                } else if (buttonPressed.equals("left")) {
                    page--;
                }
            }

            if (page == -1) {
                getDetailsScreenXml(request, response, null, "left");
            }

            LookingLocalUtils.getMyDetailsXml(request, response, page);

        } catch (Exception e) {
            getErrorScreenXml(response);
            LOGGER.error("Could not create details response output stream: " + e.toString());
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/secure/drugs"
     * @param request HTTP request
     * @param response HTTP response
     * @param buttonPressed button according to Looking Local, used for "Back", "More" etc buttons
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_DRUGS)
    @ResponseBody
    public void getDrugsScreenXml(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "buttonPressed", required = false) String buttonPressed) {
        LOGGER.debug("drugs start");

        try {
            if (buttonPressed != null) {
                if (buttonPressed.equals("right")) {
                    page++;
                } else if (buttonPressed.equals("left")) {
                    page--;
                }
            }

            if (page == -1) {
                getDetailsScreenXml(request, response, null, "left");
            }

            LookingLocalUtils.getDrugsXml(request, response, page, itemsPerPage);

        } catch (Exception e) {
            getErrorScreenXml(response);
            LOGGER.error("Could not create details response output stream: " + e.toString());
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/secure/resultsDisplay"
     * @param request HTTP request
     * @param response HTTP response
     * @param selection User option selection
     * @param buttonPressed button according to Looking Local, used for "Back", "More" etc buttons
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_RESULTS_DISPLAY)
    @ResponseBody
    public void getMedicalResultsXml(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam(value = "selection", required = false) String selection,
                                     @RequestParam(value = "buttonPressed", required = false) String buttonPressed) {
        LOGGER.debug("resultsDisplay start");
        try {
            if (buttonPressed != null) {
                if (buttonPressed.equals("left")) {
                    getDetailsScreenXml(request, response, null, "left");
                } else if (selection != null) {
                    //LookingLocalUtils.getResultsDetailsXml(request, response, selection);
                    page = 0;
                    resultSelection = selection;
                    getResultXml(request, response, resultSelection, null);
                } else {
                    getErrorScreenXml(response);
                }
            } else {
                getErrorScreenXml(response);
            }
        } catch (Exception e) {
            LOGGER.error("Could not create medical result details response output stream{}" + e);
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/secure/resultDisplay"
     * @param request HTTP request
     * @param response HTTP response
     * @param selection User option selection
     * @param buttonPressed button according to Looking Local, used for "Back", "More" etc buttons
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_RESULT_DISPLAY)
    @ResponseBody
    public void getResultXml(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "selection", required = false) String selection,
                             @RequestParam(value = "buttonPressed", required = false) String buttonPressed) {
        LOGGER.debug("resultDisplay start");
        try {
            if (buttonPressed != null) {
                if (buttonPressed.equals("right")) {
                    page++;
                } else if (buttonPressed.equals("left")) {
                    page--;
                }

                if (page == -1) {
                    page = 0;
                    LookingLocalUtils.getMedicalResultsXml(request, response);
                } else {
                    LookingLocalUtils.getResultsDetailsXml(request, response, resultSelection, page, itemsPerPage);
                }
            } else {
                page = 0;

                if (selection == null) {
                    page = 0;
                    LookingLocalUtils.getMedicalResultsXml(request, response);
                } else {
                    resultSelection = selection;
                    LookingLocalUtils.getResultsDetailsXml(request, response, resultSelection, page, itemsPerPage);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Could not create result details response output stream{}" + e);
            getErrorScreenXml(response);
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/secure/letters"
     * @param request HTTP request
     * @param response HTTP response
     * @param buttonPressed button according to Looking Local, used for "Back", "More" etc buttons
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_LETTERS)
    @ResponseBody
    public void getLettersScreenXml(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "selection", required = false) String selection,
                                    @RequestParam(value = "buttonPressed", required = false) String buttonPressed) {
        LOGGER.debug("letters start");
        letterSelection = null;

        try {
            if (buttonPressed != null) {
                if (buttonPressed.equals("right")) {
                    page++;
                } else if (buttonPressed.equals("left")) {
                    page--;
                }

                if (page == -1) {
                    getDetailsScreenXml(request, response, null, "left");
                } else {
                    LookingLocalUtils.getLettersXml(request, response, page, itemsPerPage);
                }
            } else if (selection != null) {
                page = 0;
                letterSelection = selection;
                getLetterXml(request, response, letterSelection, null);
            } else {
                LookingLocalUtils.getLettersXml(request, response, page, itemsPerPage);
            }
        } catch (Exception e) {
            getErrorScreenXml(response);
            LOGGER.error("Could not create details response output stream: " + e.toString());
        }
    }

    /**
     * Deal with the URIs "/lookinglocal/secure/letterDisplay"
     * @param request HTTP request
     * @param response HTTP response
     * @param selection User option selection
     * @param buttonPressed button according to Looking Local, used for "Back", "More" etc buttons
     */
    @RequestMapping(value = Routes.LOOKING_LOCAL_LETTER_DISPLAY)
    @ResponseBody
    public void getLetterXml(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "selection", required = false) String selection,
                             @RequestParam(value = "buttonPressed", required = false) String buttonPressed) {
        LOGGER.debug("letterDisplay start");
        try {
            if (buttonPressed != null) {
                if (buttonPressed.equals("right")) {
                    page++;
                } else if (buttonPressed.equals("left")) {
                    page--;
                }

                if (page == -1) {
                    page = 0;
                    LookingLocalUtils.getLettersXml(request, response, page, itemsPerPage);
                } else {
                    LookingLocalUtils.getLetterDetailsXml(request, response, letterSelection, page, linesPerPage,
                            lineLength);
                }
            } else {
                page = 0;

                if (selection == null) {
                    page = 0;
                    LookingLocalUtils.getLettersXml(request, response, page, itemsPerPage);
                } else {
                    letterSelection = selection;
                    LookingLocalUtils.getLetterDetailsXml(request, response, letterSelection, page, linesPerPage,
                            lineLength);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Could not create letter details response output stream{}" + e);
            getErrorScreenXml(response);
        }
    }
}
