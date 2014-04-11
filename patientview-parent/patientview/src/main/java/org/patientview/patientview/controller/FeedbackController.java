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

import org.patientview.model.Unit;
import org.patientview.patientview.model.FeedbackData;
import org.patientview.patientview.model.MessageRecipient;
import org.patientview.patientview.model.User;
import org.patientview.service.MessageManager;
import org.patientview.service.UnitManager;
import org.patientview.service.UserManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 *  Feedback controller, deals with recipient retrieval and sending feedback
 */
@Controller
public class FeedbackController extends BaseController {

    @Inject
    private UserManager userManager;
    @Inject
    private UnitManager unitManager;
    @Inject
    private MessageManager messageManager;

    /**
     * Deal with the URIs "/feedback/getFeedbackRecipients"
     * get the available recipients based on logged in user
     */
    @RequestMapping(value = Routes.GET_FEEDBACK_RECIPIENTS, method = RequestMethod.GET)
    @ResponseBody
    public HashMap<Long, String> getFeedbackRecipients(HttpServletRequest request) {

        User user = userManager.getLoggedInUser();
        List<Unit> units = unitManager.getUsersUnits(user);
        List<MessageRecipient> unitAdminRecipients = messageManager.getUnitAdminRecipients(units, user);
        List<MessageRecipient> unitStaffRecipients = messageManager.getUnitStaffRecipients(units, user);
        unitAdminRecipients.addAll(unitStaffRecipients);
        HashMap<Long, String> recipients = new HashMap<Long, String>();

        for (MessageRecipient recipient : unitAdminRecipients) {
            User staff = recipient.getUser();
            Unit staffUnit = recipient.getUnit();
            recipients.put(staff.getId(), staff.getName() + " ("
                    + staff.getRole() + ", " + staffUnit.getShortname() + ")");
        }

        return recipients;
    }

    /**
     * Deal with the URIs "/feedback/submitFeedback"
     * send feedback, including image data
     */
    @RequestMapping(value = Routes.SUBMIT_FEEDBACK, method = RequestMethod.POST)
    @ResponseBody
    public String sendFeedback(HttpServletRequest request, FeedbackData feedbackData) {

        User user = userManager.getLoggedInUser();
        User staff = userManager.get(Long.parseLong(feedbackData.getRecipient()));

        try {
            messageManager.createMessage(request.getSession().getServletContext()
                , "Feedback: " + feedbackData.getSubject(), feedbackData.getMessage(), user, staff);
            return "{\"success\": \"success\", \"errors\": \"\"}";
        } catch (Exception ex) {
            return "{\"success\": \"failure\", \"errors\": \"Error creating message\"}";
        }
    }
}
