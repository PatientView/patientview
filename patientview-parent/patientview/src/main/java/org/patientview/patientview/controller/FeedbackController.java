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

import org.apache.commons.codec.binary.Base64;
import org.patientview.model.Unit;
import org.patientview.patientview.model.Conversation;
import org.patientview.patientview.model.ConversationStatus;
import org.patientview.patientview.model.FeedbackData;
import org.patientview.patientview.model.MessageRecipient;
import org.patientview.patientview.model.Rating;
import org.patientview.patientview.model.User;
import org.patientview.service.MessageManager;
import org.patientview.service.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    private MessageManager messageManager;

    /**
     * Deal with the URIs "/feedback/getFeedbackRecipients"
     * get the available recipients based on logged in user
     */
    @RequestMapping(value = Routes.GET_FEEDBACK_RECIPIENTS, method = RequestMethod.GET)
    @ResponseBody
    public HashMap<Long, String> getFeedbackRecipients() {
        List<MessageRecipient> recipients = messageManager.getFeedbackRecipients(userManager.getLoggedInUser());

        if (!recipients.isEmpty()) {
            HashMap<Long, String> recipientsSimple = new HashMap<Long, String>();

            for (MessageRecipient recipient : recipients) {
                User staff = recipient.getUser();
                Unit staffUnit = recipient.getUnit();
                recipientsSimple.put(staff.getId(), staff.getName() + " ("
                        + (staff.getRole().equals("unitadmin") ? "Admin" : "Staff") + ", "
                        + staffUnit.getShortname() + ")");
            }

            return recipientsSimple;
        } else { return null; }
    }

    /**
     * Deal with the URIs "/feedback/submitFeedback"
     * send feedback, including image data
     */
    @RequestMapping(value = Routes.SUBMIT_FEEDBACK, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> sendFeedback(HttpServletRequest request, FeedbackData feedbackData) {
        User user = userManager.getLoggedInUser();
        User staff = userManager.get(Long.parseLong(feedbackData.getRecipient()));

        try {
            messageManager.createMessage(request.getSession().getServletContext()
                    , feedbackData.getSubject(), feedbackData.getMessage()
                    , user, staff, feedbackData.getImageData(), true);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deal with the URIs "/feedback/rateConversation"
     * send feedback, including image data
     */
    @RequestMapping(value = Routes.RATE_CONVERSATION, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> rateConversation(Rating rating) {
        User user = userManager.getLoggedInUser();

        try {
            Conversation conversation = messageManager.getConversation(rating.getConversationId());
            if (user.equals(conversation.getParticipant1()) || user.equals(conversation.getParticipant2())) {
                conversation.setRating(rating.getRating());
                messageManager.saveConversation(conversation);
                return new ResponseEntity<String>(HttpStatus.OK);

            } else {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deal with the URIs "/feedback/setConversationStatus"
     * send feedback, including image data
     */
    @RequestMapping(value = Routes.SET_CONVERSATION_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> setConversationStatus(@RequestParam("status") String status,
                                        @RequestParam("conversationId") String conversationId) {
        User user = userManager.getLoggedInUser();

        try {
            Conversation conversation = messageManager.getConversation(Long.parseLong(conversationId));
            ConversationStatus conversationStatus = messageManager.getConversationStatus(Long.parseLong(status));

            if (user.equals(conversation.getParticipant1()) || user.equals(conversation.getParticipant2())) {
                conversation.setConversationStatus(conversationStatus);
                if (conversationStatus != null) {
                    conversation.setClinicianClosed(conversationStatus.getClosedStatus());
                } else {
                    conversation.setClinicianClosed(false);
                }
                messageManager.saveConversation(conversation);
                return new ResponseEntity<String>(HttpStatus.OK);
            } else {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deal with the URIs "/feedback/downloadImage"
     * download image data by uri, avoiding issues with datauri not being supported, return null on all errors
     */
    @RequestMapping(value = Routes.DOWNLOAD_IMAGE, method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] downloadImage(@RequestParam(value = "conversationId", required = true) String conversationId) {
        try {
            User user = userManager.getLoggedInUser();
            Conversation conversation = messageManager.getConversation(Long.parseLong(conversationId));
            if (conversation != null) {
                if (user.equals(conversation.getParticipant1()) || user.equals(conversation.getParticipant2())) {
                    if (conversation.getImageData() != null) {
                        String imageData = conversation.getImageData().split(",")[1];
                        return Base64.decodeBase64(imageData.getBytes());
                    }
                }
            }
        } catch (Exception ex) {
            return null;
        }

        return null;
    }
}
