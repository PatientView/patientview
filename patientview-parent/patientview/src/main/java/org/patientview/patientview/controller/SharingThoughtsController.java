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

import org.patientview.patientview.model.User;
import org.patientview.service.SharedThoughtManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

/**
 *  Sharing Thoughts controller, deals with responder retrieval & adding responders to shared thoughts
 */
@Controller
public class SharingThoughtsController extends BaseController {
    @Inject
    private SharedThoughtManager sharedThoughtManager;

    /**
     * Deal with the URIs "/sharingThoughts/getOtherResponders"
     * get the available responders based on current shared thought, ignores those already attached
     */
    @RequestMapping(value = Routes.SHARING_THOUGHTS_GET_OTHER_RESPONDERS, method = RequestMethod.POST)
    @ResponseBody
    public HashMap<Long, String> getResponders(@RequestParam("sharedThoughtId") String sharedThoughtId) {

        List<User> responders
                = sharedThoughtManager.getOtherResponders(
                sharedThoughtManager.get(Long.parseLong(sharedThoughtId), false, true));

        if (!responders.isEmpty()) {
            HashMap<Long, String> responderData = new HashMap<Long, String>();
            for (User responder : responders) {
                responderData.put(responder.getId(), responder.getName());
            }
            return responderData;
        } else { return null; }
    }

    /**
     * Deal with the URIs "/sharingThoughts/addResponder"
     * adds a responder to a shared thought (they can join the conversation etc)
     */
    @RequestMapping(value = Routes.SHARING_THOUGHTS_ADD_RESPONDER, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addResponder(
            @RequestParam("sharedThoughtId") String sharedThoughtId,
            @RequestParam("responderId") String responderId) {

        try {
            if (sharedThoughtManager.addResponder(Long.parseLong(sharedThoughtId), Long.parseLong(responderId))) {
                return new ResponseEntity<String>(HttpStatus.OK);
            } else { return new ResponseEntity<String>(HttpStatus.BAD_REQUEST); }
        } catch (Exception ex) { return new ResponseEntity<String>(HttpStatus.BAD_REQUEST); }
    }

    /**
     * Deal with the URIs "/sharingThoughts/removeResponder"
     * remove a responder from a shared thought
     */
    @RequestMapping(value = Routes.SHARING_THOUGHTS_REMOVE_RESPONDER, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> removeResponder(
            @RequestParam("sharedThoughtId") String sharedThoughtId,
            @RequestParam("responderId") String responderId) {

        try {
            if (sharedThoughtManager.removeResponder(Long.parseLong(sharedThoughtId), Long.parseLong(responderId))) {
                return new ResponseEntity<String>(HttpStatus.OK);
            } else { return new ResponseEntity<String>(HttpStatus.BAD_REQUEST); }
        } catch (Exception ex) { return new ResponseEntity<String>(HttpStatus.BAD_REQUEST); }
    }

    /**
     * Deal with the URIs "/sharingThoughts/addMessage"
     * remove a responder from a shared thought
     */
    @RequestMapping(value = Routes.SHARING_THOUGHTS_ADD_MESSAGE, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addMessage(
            @RequestParam("sharedThoughtId") String sharedThoughtId,
            @RequestParam("message") String message) {

        try {
            if (sharedThoughtManager.addMessage(Long.parseLong(sharedThoughtId), message)) {
                return new ResponseEntity<String>(HttpStatus.OK);
            } else { return new ResponseEntity<String>(HttpStatus.BAD_REQUEST); }
        } catch (Exception ex) { return new ResponseEntity<String>(HttpStatus.BAD_REQUEST); }
    }
}
