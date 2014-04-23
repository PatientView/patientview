<%@ page import="org.patientview.patientview.messaging.Messaging" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
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

<div class="row">
        <div class="span12 footer">
            <ul class="barSpeperatedNav">
                <logic:present specialty="renal">
                    <li><a href="/disclaimer.do">Disclaimer</a></li>
                    <logic:present name="<%=Messaging.FEEDBACK_ENABLED %>" scope="session">
                        <li><a href="#" class="feedbackButton">Report Issue</a></li>
                    </logic:present>
                </logic:present>
            </ul>
        </div>
    </div>
</div>

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

<script type="text/javascript" src="/js/bootstrap.js"></script>
<script type="text/javascript" src="/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="/ibd/js/ico.min.js"></script>
<script type="text/javascript" src="/ibd/js/ibd.js"></script>
<script type="text/javascript" src="/js/pwdmeter.js"></script>
<script type="text/javascript" src="/js/jquery.rateit.min.js"></script>
<%
    // todo tracking code should only be present on live...
%>
<logic:present specialty="renal">
    <script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    </script>
    <script type="text/javascript">
    try {
    var pageTracker = _gat._getTracker("UA-12523245-4");
    pageTracker._trackPageview();
    } catch(err) {}</script>
</logic:present>
<logic:present specialty="ibd">
    <script type="text/javascript">

        var _gaq = _gaq || [];
        _gaq.push(['_setAccount', 'UA-12523245-11']);
        _gaq.push(['_trackPageview']);

        (function() {
            var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
        })();

    </script>
    <script type="text/javascript" src="/ibd/js/raphael-min.js"></script>
</logic:present>

</body>
