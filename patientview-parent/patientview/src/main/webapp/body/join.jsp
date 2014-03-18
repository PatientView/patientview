<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

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

<html:xhtml/>

<div class="page-header">
    <h1>Join Patient View</h1>
</div>

<p>Please enter your details. You need to be a patient of a renal unit.</p>
<p>Ensure they are accurate since they will be used by your unit's staff to verify who you are and to contact you to add you to PatientView.</p>
<p>If your local unit has PatientView, the message will go to the local PatientView admin.  If it doesn't, we'll send it to someone senior at the unit.  See if your unit has PatientView on the map at <a href ="http://bit.ly/rpvsites" target="_blank">bit.ly/rpvsites</a></p>
<br />

<div class="joinRequestModel">

    <form action="/web/joinRequestSubmit" class="js-joinrequest-form" styleClass="form-horizontal">

        <div class="alert alert-error js-message-errors" style="display: none">
            <strong>You do not have any messages.</strong>
        </div>

        <div class="control-group">
            <label class="control-label">First name</label>

            <div class="controls"><input name="firstName" class="js-joinrequest-firstname"/></div>
        </div>

        <div class="control-group">
            <label class="control-label">Last name</label>

            <div class="controls"><input name="lastName" class="js-joinrequest-lastname"/></div>
        </div>

        <div class="control-group">
            <label class="control-label">Date of birth</label>

            <div class="date datePicker controls">
                <input name="dateOfBirth" class="js-joinrequest-dob" size="16" type="text">
                <span class="add-on"><i class="icon-th"></i></span>
                <span class="help-inline">dd-mm-yyyy</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">NHS Number</label>

            <div class="controls"><input name="nhsNo" class="js-joinrequest-nhs-no"/> (optional but useful)</div>
        </div>

        <div class="control-group">
            <label class="control-label">Specialty</label>

            <div class="controls">
                <select id="specialty" class="js-joinrequest-specialty">
                    <option value="-1" selected="selected">-- Select your specialty --</option>
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">Unit</label>

            <div class="controls">
                <select id="unitcode" class="js-joinrequest-unit">
                    <option value="-1" selected="selected">-- Select your unit --</option>
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">Email address</label>

            <div class="controls"><input name="email" class="js-joinrequest-email"/></div>
        </div>

        <div class="control-group">
            <label class="control-label">Security Question</label>


            <div class="controls">
                <div class="help-block top-help-block"><%=request.getSession().getAttribute("antiSpamQuestion")%> = ?</div>

                <input property="antiSpamAnswer" class="js-joinrequest-security-question"/>
            </div>
        </div>

        <div class="control-group">
            <div class="controls"><input type="submit" value="Send join request" class="js-message-submit-btn" styleClass="btn"/></div>
        </div>

    </form>
</div>
<script src="/js/homejoinrequest.js" type="text/javascript"></script>

