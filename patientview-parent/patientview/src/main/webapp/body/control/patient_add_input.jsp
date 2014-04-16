<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page isELIgnored ="false" %>
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
<div class="span9">
<div class="page-header">
    <h1>Patient</h1>
</div>

    <form type="hidden" value="js-specialty-form">
        <input type="hidden" name="specialty" class="js-user-specialty-id" value="${specialty.id}"/>
    </form>

<html:errors />

<logic:present name="invalidNhsno" >
  <p><font color="red">The NHS number <b><bean:write name="invalidNhsno" /></b> is invalid.</font></p>
</logic:present>

<logic:present name="userAlreadyExists" >
  <p><font color="red">The username <b><bean:write name="userAlreadyExists" /></b> you entered is already being used by another user. Please pick another.</font></p>
</logic:present>

<logic:present name="patientAlreadyInUnit" >
  <p><font color="red">The patient with NHS number <b><bean:write name="patientAlreadyInUnit" /></b> already exists in your unit. You can't add another patient with the same NHS no to your unit. Please check the NHS number.</font></p>
</logic:present>

<logic:present name="radarGroupPatient" >
    <p><font color="red">Can't add a patient to <b><bean:write name="radarGroupPatient" /></b> unit.</font></p>
</logic:present>


<form action="/web/patient/add" name="patientInput" class="js-patient-form" styleClass="form-horizontal">

    <div class="alert alert-error js-message-errors" style="display: none">
        <strong>You do not have any messages.</strong>
    </div>

    <table cellpadding="3" >
    <tr>
      <td><b>User Name</b></td>
      <td><input name="username" class="js-patient-username"h/></td>
    </tr>
    <tr>
      <td><b>First Name</b></td>
        <td><input name="firstname" class="js-patient-firstname"h/></td>
    </tr>
    <tr>
        <td><b>Last Name</b></td>
        <td><input name="lastname" class="js-patient-lastname"h/></td>
    </tr>
    <tr>
      <td><b>NHS Number</b></td>
      <td><input name="nhsno" class="js-patient-nhsno"/></td>
        <logic:present name="offerToAllowInvalidNhsno" >
        <td><b>Add patient with invalid NHS number</b></td>
        <td><checkbox name="override-nhsno" class="js-patient-override-nhsno"/></td>
      </logic:present>
    </tr>
    <tr>
      <td><b>Email Address</b></td>
      <td><input name="email" class="js-patient-email"/></td>
    </tr>
    <tr>
      <td><b>
          <logic:present specialty="renal">Renal Unit</logic:present><logic:present specialty="ibd">IBD Unit</logic:present>
          <logic:present specialty="diabetes">Unit</logic:present>
      </b></td>
      <td><select property="units" class="js-patient-units">
            <option value="-1" selected="selected">-- Select your unit --</option>
      </select></td>
    </tr>
    <tr>
      <td>
          <b>Dummy Patient</b>
      </td>
      <td>
          <input type="checkbox" name="dummypatient" class="js-patient-dummy" value="true" />
      </td>
    </tr>

    <logic:present specialty="ibd">
        <tr>
            &NonBreakingSpace;
        </tr>
        <tr>
            <td>
                <b>IBD Details</b>
            </td>
        </tr>

        <tr>
            <td>
                <b>Primary Diagnosis</b>
            </td>
            <td>
                <select name="primaryDiagnosis" class="js-ibd-primary-diagnosis">
                    <option value="-1" selected="selected">Please select</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <b>Disease Extend</b>
            </td>
            <td>
                <select name="diseaseExtent" class="js-ibd-disease-extent">
                    <option value="-1" selected="selected">Please select</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <b>Date of Diagnosis</b>
            </td>
            <td>
                <div class="date datePicker controls">
                    <input name="yearOfDiagnosis" class="js-ibd-diagnosis-year" size="16" type="text">
                    <span class="add-on"><i class="icon-th"></i></span>
                    <span class="help-inline">dd-mm-yyyy</span>
                </div>
            </td>
        </tr>

        <tr>
            <td>
                <b>Complications</b>
            </td>
            <td>
                <select name="complications" class="js-ibd-complications"  multiple="18">
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <b>Extra-intestinal manifestations</b>
            </td>
            <td>
                <select name="manifestations" class="js-ibd-manifestations"  multiple="18">
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <b>Smoking History</b>
            </td>
            <td>
                <select name="smokingHistory" class="js-ibd-smoking-history">
                    <option value="-1" selected="selected">Please select</option>
                </select>

            </td>
        </tr>
        <tr>
            <td>
                <b>Other parts affected</b>
            </td>
            <td>
                <input name="otherPartsAffected" class="js-ibd-parts-affected">
            </td>
        </tr>
        </tr>
        <tr>
            <td>
                <b>Vaccination Record</b>
            </td>
            <td>
                <input name="vaccinationRecord" class="js-ibd-vaccination-record">
            </td>
        </tr>
        <tr>
            <td>
                <b>Colonoscopy surveillance</b>
            </td>
            <td>
                <input name="surveilanceColonoscopy" class="js-ibd-surveillance-colonoscopy">
            </td>
        </tr>
        <tr>
            <td>
                <b>Current Medications</b>
            </td>
            <td>
                <select name="currentMedications" class="js-ibd-current-medications">
                    <option value="-1" selected="selected">None</option>
                </select>

            </td>
        </tr>
        <tr>
            <td>
                <b>IBD Nurses</b>
            </td>
            <td>
                <input name="namedConsultant" class="js-ibd-named-consultant">
            </td>
        </tr>
        <tr>
            <td>
                <b>IBD Consultant</b>
            </td>
            <td>
                <input name="namedConsultant" class="js-ibd-named-consultant">
            </td>
        </tr>
</form>

<form action="/" class="js-ibd-surgical-history-input" styleClass="form-horizontal">

<tr>
    <td>
        <b>Surgical History</b>
    </td>
</tr>
<tr>
    <td colspan="2">
        <table class="table table-bordered table-striped js-ibd-surgical-history-display">
            <thead>
            <tr>
                <th class="tableheader">Surgery</th>
                <th class="tableheader">Date</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td class="tablecell">OUch</td>
                <td class="tablecell">08/09/080</td>
                <td><button type="submit" styleClass="btn">Delete</button></td>
            </tr>
            </tbody>
        </table>
    </td>
</tr>

    <tr>
        <td>
            <b>Surgery</b>
        </td>
        <td>
            <select name="surgicalHistory" class="js-ibd-surgical-history">
                <option value="-1" selected="selected">Please select</option>
            </select>
        </td>
    </tr>
    <tr>
        <td>
            <b>Surgery Date</b>
        </td>
        <td>
            <input name="surgicalDate" class="js-ibd-surgical-history-date" size="16" type="text">
            <span class="add-on"><i class="icon-th"></i></span>
            <span class="help-inline">dd-mm-yyyy</span>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <button type="submit" styleClass="btn" class="js-ibd-surgical-history-add">Add Surgery</button>
        </td>
    </tr>

</form>

    </logic:present>


 </table>



<div>
    <form styleClass="form-horizontal">
    <table>

    </table>
    </form>

    <table>
        <tr align="right">
            <td><html:submit value="Add" styleClass="btn" /></td>
        </tr>
    </table>
</div>



<script src="/js/patient.js" type="text/javascript"></script>

