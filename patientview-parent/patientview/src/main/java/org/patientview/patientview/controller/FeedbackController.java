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

import org.patientview.patientview.model.FeedbackData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 *  Feedback controller, deals with recipient retrieval and sending feedback
 */
@Controller
public class FeedbackController extends BaseController {

    /**
     * Deal with the URIs "/feedback/getFeedbackRecipients"
     * get the available recipients based on logged in user
     */
    @RequestMapping(value = Routes.GET_FEEDBACK_RECIPIENTS, method = RequestMethod.GET)
    @ResponseBody
    public String getFeedbackRecipients(HttpServletRequest request) {

        return "{id:2 , name: \"Dr John Smith\"}";
    }

    /**
     * Deal with the URIs "/feedback/submitFeedback"
     * send feedback, including image data
     */
    @RequestMapping(value = Routes.SUBMIT_FEEDBACK, method = RequestMethod.POST)
    @ResponseBody
    public String sendFeedback(FeedbackData feedbackData) {

        //System.out.println(feedbackData.getMessage());
        return "{\"success\": \"success\", \"errors\": \"\"}";
    }
}
