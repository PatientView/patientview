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

package org.patientview.patientview.controller;

import org.apache.commons.lang.StringUtils;
import org.patientview.patientview.model.User;
import org.patientview.security.impl.PatientViewPasswordEncoder;
import org.patientview.service.UserManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 *  Emergency care record controller, deals with retrieving simple list of suitable NHS numbers
 */
@Controller
public class EcrController extends BaseController {

    @Inject
    private UserManager userManager;

    /**
     * Deal with the URIs "/ecr/getPatientIdentifiers"
     * get the available responders based on current shared thought, ignores those already attached
     * hardcoded to only accept "ecr" username and password
     */
    @RequestMapping(value = Routes.ECR_GET_PATIENT_IDENTIFIERS, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getPatientIdentifiers(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password)
    {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        try {
            // check ECR user details
            PatientViewPasswordEncoder encoder = new PatientViewPasswordEncoder();
            User ecrUser = userManager.get("ecr");

            if (username.equals(ecrUser.getUsername()) && ecrUser.getPassword().equals(encoder.encode(password))) {

                List<String> identifiers = userManager.getEcrPatientIdentifiers();

                if (identifiers.size() > 0) {
                    return new ResponseEntity<String>(identifiers.toString(), new HttpHeaders(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>(HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) { return new ResponseEntity<String>(HttpStatus.BAD_REQUEST); }
    }

    /**
     * Deal with the URIs "/ecr/changeOptInOut"
     * opt in to ECR
     */
    @RequestMapping(value = Routes.ECR_CHANGE_OPT_IN_OUT, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> changeOptInOut(
            @RequestParam(value = "optIn", required = false) String optIn,
            @RequestParam(value = "optOutPermanently", required = false) String optOutPermanently) {

        if (StringUtils.isEmpty(optIn) || StringUtils.isEmpty(optOutPermanently)) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        try {
            User user = userManager.getLoggedInUser();
            userManager.setEcrOptInStatus(user, Boolean.parseBoolean(optIn), Boolean.parseBoolean(optOutPermanently));
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
}
