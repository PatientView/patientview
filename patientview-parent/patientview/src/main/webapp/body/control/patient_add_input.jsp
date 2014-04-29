<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page isELIgnored ="false" %>

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




    <div class="alert alert-error js-message-errors" style="display: none">
        <strong>You do not have any messages.</strong>
    </div>

    <form action="/web/patient/add" name="patientInput" class="js-patient-form" styleClass="form-horizontal">
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


        <tr align="right">
            <td><html:submit value="Add" styleClass="btn" /></td>
        </tr>

      </table>

    </form>



<script src="/js/patient.js" type="text/javascript"></script>

