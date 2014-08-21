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

/**
 *  Base Controller,supplies the forward/redirect method.All spring controller should extend this.
 */
public final class Routes {

    public static final String JOIN_REQUEST_LIST_URL = "/control/joinRequestList";
    public static final String JOIN_REQUEST_EDIT_URL = "/control/joinRequestEdit";
    public static final String JOIN_REQUEST_EDIT_INPUT_URL = "/control/joinRequestEditInput";



    public static final String SERVER_URL = "/web";

    public static final String LOOKING_LOCAL_HOME = "/lookinglocal/home";
    public static final String LOOKING_LOCAL_AUTH = "/lookinglocal/auth";
    public static final String LOOKING_LOCAL_ERROR = "/lookinglocal/error";
    public static final String LOOKING_LOCAL_ERROR_REDIRECT = "error";
    public static final String LOOKING_LOCAL_MAIN = "/lookinglocal/secure/main";
    public static final String LOOKING_LOCAL_MAIN_REDIRECT = "secure/main";
    public static final String LOOKING_LOCAL_DETAILS = "/lookinglocal/secure/details";
    public static final String LOOKING_LOCAL_MY_DETAILS = "/lookinglocal/secure/myDetails";
    public static final String LOOKING_LOCAL_RESULTS_DISPLAY = "/lookinglocal/secure/resultsDisplay";
    public static final String LOOKING_LOCAL_LETTER_DISPLAY = "/lookinglocal/secure/letterDisplay";

    public static final String LIST_SPECIALTIES = "/listSpecialties";


    public static final String PATIENT_CONTROLLER = "/patient";
    public static final String ADD_PATIENT = "/add";
    public static final String UPDATE_PATIENT = "/update";

    public static final String LOOKUP_CONTROLLER = "/lookup";
    public static final String GET_LOOKUP = "/component";


    public static final String JOIN_REQUEST_LIST_PAGE = "/control/join_request_list";
    public static final String JOIN_REQUEST_EDIT_INPUT_PAGE = "/control/join_requests_edit_input";
    public static final String JOIN_REQUEST_SUBMIT = "/joinRequestSubmit";

    public static final String UNIT_PATIENTS_LIST_URL = "/control/unitPatients";
    public static final String UNIT_USERS_LIST_URL = "/control/unitUsers";
    public static final String UNIT_PATIENTS_LIST_PAGE = "/control/unit_patients";
    public static final String UNIT_USERS_LIST_PAGE = "/control/unit_users";
    public static final String UNIT_BY_SPECIALTY_LIST_URL = "/unitBySpecialty";


    public static final String EMAIL_VERIFICATION_URL = "/control/emailverification";

    public static final String DIABETES_CAREPLAN_URL = "/careplan-diabetes";
    public static final String DIABETES_CAREPLAN_UPDATE_URL = "/careplan-update";
    public static final String DIABETES_CAREPLAN_PAGE = "/diabetes/careplan/careplan-edit";


    // feedback
    public static final String GET_FEEDBACK_RECIPIENTS = "/feedback/getFeedbackRecipients";
    public static final String SUBMIT_FEEDBACK = "/feedback/submitFeedback";
    public static final String RATE_CONVERSATION = "/feedback/rateConversation";
    public static final String SET_CONVERSATION_STATUS = "/feedback/setConversationStatus";
    public static final String DOWNLOAD_IMAGE = "/feedback/downloadImage";

    // sharing thoughts
    public static final String SHARING_THOUGHTS_GET_OTHER_RESPONDERS = "/sharingThoughts/getOtherResponders";
    public static final String SHARING_THOUGHTS_ADD_RESPONDER = "/sharingThoughts/addResponder";
    public static final String SHARING_THOUGHTS_REMOVE_RESPONDER = "/sharingThoughts/removeResponder";
    public static final String SHARING_THOUGHTS_ADD_MESSAGE = "/sharingThoughts/addMessage";
    public static final String SHARING_THOUGHTS_OPEN_CLOSE = "/sharingThoughts/openCloseSharedThought";
    public static final String SHARING_THOUGHTS_SEND_MESSAGE_TO_PATIENT = "/sharingThoughts/sendMessageToPatient";

    // ECR integration
    public static final String ECR_GET_PATIENT_IDENTIFIERS = "/ecr/getPatientIdentifiers";
    public static final String ECR_CHANGE_OPT_IN_OUT = "/ecr/changeOptInOut";


    private Routes() { }
}
