<%@ page import="org.patientview.patientview.messaging.Messaging" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%--
  ~ PatientView
  ~
  ~ Copyright (c) Worth Solutions Limited 2004-2013
  ~
  ~ This file is part of PatientView.
  ~
  ~ PatientView is free software: you can redistribute it and/or modify it under the terms of the
  ~ GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
  ~ or (at your option) any later version.
  ~ PatientView is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  ~ the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  ~ GNU General Public License for more details.
  ~ You should have received a copy of the GNU General Public License along with PatientView in a file
  ~ titled COPYING. If not, see <http://www.gnu.org/licenses/>.
  ~
  ~ @package PatientView
  ~ @link http://www.patientview.org
  ~ @author PatientView <info@patientview.org>
  ~ @copyright Copyright (c) 2004-2013, Worth Solutions Limited
  ~ @license http://www.gnu.org/licenses/gpl-3.0.html The GNU General Public License V3.0
  --%>

<head>
  <title>Patient View</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/datepicker.css" rel="stylesheet">
    <link href="/css/rateit.css" rel="stylesheet">
    <link href="/css/main.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/css/slider.css"/>
    <link rel="icon" type="image/png" href="/images/kidney.gif">
    <!--[if IE]>
        <link href="/css/ie.css" rel="stylesheet">
    <![endif]-->
    <script type="text/javascript" src="/js/jquery-1.10.1.min.js"></script>
    <script type="text/javascript" src="/js/jquery-migrate-1.2.1.min.js"></script>
    <script type="text/javascript" src="/js/jquery.tools.min.js"></script>
    <script type="text/javascript" src="/js/jquery-ui-1.10.4.min.js"></script>

</head>

<!--[if IE 7 ]>
<body class="ie7">
<![endif]-->

<!--[if IE 8 ]>
<body class="ie8">
<![endif]-->

<!--[if IE 9 ]>
<body class="ie9">
<![endif]-->

<!--[if (gt IE 9)|!(IE)]><!-->
<body>
<!--<![endif]-->


<logic:present specialty="renal">
    <logic:present name="<%=Messaging.FEEDBACK_ENABLED %>" scope="session">
        <div id="feedbackModal" class="boxShadow1">
            <form id="js-feedback-form">
                <h2>Report Issue</h2>
                <p>Your feedback will be sent with details of what you are currently looking at</p><br/>
                <div id="dialogScreenshot"></div>
                <div class="alert alert-error" id="js-feedback-error-found"></div>
                <div class="control-group">
                    <label class="control-label">Recipient</label>
                    <div class="controls">
                        <select id="js-feedback-recipient"></select>
                        <div class="alert alert-error" id="js-feedback-recipient-error"></div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">Subject</label>
                    <div class="controls"><input name="subject" id="js-feedback-subject"/></div>
                    <div class="alert alert-error" id="js-feedback-subject-error"></div>
                </div>
                <div class="control-group">
                    <label class="control-label">Message</label>
                    <div class="controls"><textarea name="message" id="js-feedback-message"></textarea></div>
                    <div class="alert alert-error" id="js-feedback-message-error"></div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <input type="submit" value="Send Feedback" id="js-feedback-submit-btn" class="btn btn-primary"/>&nbsp;
                        <input type="submit" value="Cancel" id="js-feedback-cancel-btn" class="btn"/>
                    </div>
                </div>
            </form>
            <div id="js-feedback-loading">
                <h2>Sending Feedback</h2>
                <p>Please wait for your feedback to be sent</p>
            </div>
            <div id="js-feedback-success">
                <h2>Feedback Sent</h2>
                <br/>
                <div class="alert alert-success">Your feedback has been sent to the relevant recipients</div>
                <input type="submit" value="Close" id="js-feedback-close-btn" class="btn"/>
            </div>
        </div>
        <div class="hider"><div id="screenshot"></div></div>
        <script type="text/javascript" src="/js/feedback.js"></script>
        <script type="text/javascript" src="/js/html2canvas.js"></script>
    </logic:present>
</logic:present>
