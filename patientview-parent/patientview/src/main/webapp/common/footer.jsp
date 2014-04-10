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
                    <li><a href="#" class="feedbackButton">Report Issue</a></li>
                </logic:present>
            </ul>
        </div>
    </div>
</div>

<logic:present specialty="renal">
    <!-- Feedback dialog -->
    <div id="feedbackModal" style="display:none;">

        <form action="/web/feedbackSubmit" class="js-feedback-form" styleClass="form-horizontal">

            <h2>Submit Feedback</h2>
            <p>Your feedback will be sent with details of what you are currently looking at in Patient View.</p>
            <div class="control-group">
                <label class="control-label">Recipient</label>
                <div class="controls">
                    <select id="unitcode" class="js-feedback-recipients">
                        <option value="-1" selected="selected">-- Select a recipient --</option>
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label">Subject</label>
                <div class="controls"><input name="subject" class="js-feedback-subject"/></div>
            </div>

            <div class="control-group">
                <label class="control-label">Message</label>
                <div class="controls"><textarea name="message" class="js-feedback-message"></textarea></div>
            </div>

            <div class="control-group">
                <div class="controls">
                    <input type="submit" value="Send Feedback" class="js-message-submit-btn btn"/>&nbsp;
                    <input type="submit" value="Cancel" class="js-message-cancel-btn btn"/>
                </div>
            </div>

        </form>
    </div>
</logic:present>



<script type="text/javascript" src="/js/bootstrap.js"></script>
<script type="text/javascript" src="/js/bootstrap-datepicker.js"></script>

<script type="text/javascript" src="/ibd/js/ico.min.js"></script>
<script type="text/javascript" src="/ibd/js/ibd.js"></script>
<script type="text/javascript" src="/js/pwdmeter.js"></script>

<script type="text/javascript" src="/js/feedback.js"></script>
<script type="text/javascript" src="/js/html2canvas.js"></script>
<script src="/js/messages.js" type="text/javascript"></script>
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
