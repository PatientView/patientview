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
                    <li><a href="#" class="feedback_button" data-toggle="modal" data-target="#messageModal" >Report Issue</a></li>
                </logic:present>
            </ul>
        </div>
    </div>
</div>

<logic:present specialty="renal">
    <!-- Feedback dialog -->
    <div id="messageModal" class="modal hide fade">
        <form action="/send-message.do" class="js-message-form">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3>New message</h3>
            </div>
            <div class="modal-body-message">
                <fieldset>
                    <input type="hidden" class="js-message-redirect" value="/patient/conversation.do" />

                    <logic:present name="units_for_messaging">
                        <div class="control-group">
                            <label class="control-label">Unit</label>

                            <div class="controls">
                                <select name="recipientId" class="js-message-unit-code">
                                    <option value="">Select</option>

                                    <logic:iterate name="units_for_messaging" id="unit" indexId="index">
                                        <option value="<bean:write name="unit" property="unitcode" />"><bean:write name="unit" property="name" /></option>
                                    </logic:iterate>
                                </select>
                                <span class="js-message-unit-loading" style="display: none">Finding recipients from unit ...</span>
                            </div>

                            <div class="alert alert-error js-message-unit-recipient-errors" style="display: none"></div>
                        </div>
                    </logic:present>


                    <div class="control-group js-recipient-container" <logic:present name="units_for_messaging">style="display: none"</logic:present>>

                        <label class="control-label">To</label>
                        <input type="text" class="search-query" placeholder="Filter" id="search" name="search" size="8" style="display: none"/>
                        <button class="js-filter-button control-group">...</button>

                        <div class="controls">
                            <select name="recipientId" class="js-message-recipient-id">
                                <option value="">Select</option>

                                <logic:notEmpty name="unitAdminRecipients">
                                    <option></option>

                                    <!-- display the first recipient's unit shortcode. not sure how they used to get the first item of a list in 80s -->
                                    <logic:iterate name="unitAdminRecipients" id="recipient" indexId="index">
                                        <logic:equal name="index" value="0">
                                            <optgroup label="Unit Admins - <bean:write name="recipient" property="unit.shortname" />">">
                                        </logic:equal>
                                    </logic:iterate>

                                    <logic:iterate name="unitAdminRecipients" id="recipient" indexId="index">
                                        <option value="<bean:write name="recipient" property="user.id" />"><bean:write name="recipient" property="user.name" /></option>
                                    </logic:iterate>
                                    </optgroup>
                                </logic:notEmpty>

                                <logic:notEmpty name="unitStaffRecipients">
                                    <option></option>

                                    <logic:iterate name="unitStaffRecipients" id="recipient" indexId="index">
                                        <logic:equal name="index" value="0">
                                            <optgroup label="Unit Staff - <bean:write name="recipient" property="unit.shortname" />">">
                                        </logic:equal>
                                    </logic:iterate>

                                    <logic:iterate name="unitStaffRecipients" id="recipient" indexId="index">
                                        <option value="<bean:write name="recipient" property="user.id" />"><bean:write name="recipient" property="user.name" /></option>
                                    </logic:iterate>
                                    </optgroup>
                                </logic:notEmpty>

                                <logic:notEmpty name="unitPatientRecipients">
                                    <option></option>

                                    <logic:iterate name="unitStaffRecipients" id="recipient" indexId="index">
                                        <logic:equal name="index" value="0">
                                            <optgroup label="Patients - <bean:write name="recipient" property="unit.shortname" />">">
                                        </logic:equal>
                                    </logic:iterate>

                                    <logic:iterate name="unitPatientRecipients" id="recipient" indexId="index">
                                        <option value="<bean:write name="recipient" property="user.id" />"><bean:write name="recipient" property="user.name" /></option>
                                    </logic:iterate>
                                    </optgroup>
                                </logic:notEmpty>
                            </select>
                            <span class="js-message-filtered" style="display: none"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">Subject</label>
                        <div class="controls">
                            <input type="text" name="subject" class="js-message-subject" />
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">Message</label>
                        <div class="controls">
                            <textarea rows="6" cols="3" name="content" class="new-message js-message-content"></textarea>
                        </div>
                    </div>

                    <div class="alert alert-error js-message-errors" style="display: none">
                        <strong>You do not have any messages.</strong>
                    </div>
                </fieldset>
            </div>
            <div class="modal-footer">
                <a href="#" class="btn" data-dismiss="modal">Close</a>
                <input type="submit" value="Send" class="btn btn-primary  js-message-submit-btn" />
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
