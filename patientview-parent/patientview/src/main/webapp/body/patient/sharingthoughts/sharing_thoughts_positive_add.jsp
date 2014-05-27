<%@ page import="org.patientview.patientview.sharingthoughts.SharingThoughts" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<script type="text/javascript">
    $("#sharingThoughtsForm").parent().css("background-color","red");

</script>

<div class="page-header">
    <h1>Share Positive Comment</h1>
    <p class="pull-right largeTick">&#10004;</p>
    <p align="left">Please complete the questions and statements below by either clicking the appropriate box or adding your comments in the space provided</p>
</div>
<html:form action="/patient/sharingThoughtSave">
    <html:hidden property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" value="1" />
    <div class="divPositive">
    <table border="0" cellspacing="1" cellpadding="3" class="table table-bordered table-striped tablePositive">

    <logic:present name="<%=SharingThoughts.ERRORS_PARAM%>">
        <logic:notEmpty name="<%=SharingThoughts.ERRORS_PARAM%>">
            <p><h4 class="pError">Please correct the following in your submission:</h4></p>
            <logic:iterate id="error" name="<%=SharingThoughts.ERRORS_PARAM%>">
                <p class="pError"><font color="red"><bean:write name="error" /></font></p>
            </logic:iterate>
        </logic:notEmpty>
    </logic:present>

    <tr >
      <td width="300">Are you the patient on this PatientView login? <span class="required">*</span></td><td>
        Yes: <html:radio property="<%=SharingThoughts.IS_PATIENT%>" name="sharingThoughtsForm" value="true" />&nbsp;&nbsp;&nbsp;&nbsp;
        No: <html:radio property="<%=SharingThoughts.IS_PATIENT%>" name="sharingThoughtsForm" value="false" />&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>

    <tr >
      <td width="300">If no, which of these are you?<br />(You may tick more than one)</td><td>
         Carer: <html:checkbox property="<%=SharingThoughts.IS_PRINCIPAL_CARER%>" name="sharingThoughtsForm" />&nbsp;&nbsp;&nbsp;&nbsp;
         Relative: <html:checkbox property="<%=SharingThoughts.IS_RELATIVE%>" name="sharingThoughtsForm" />&nbsp;&nbsp;&nbsp;&nbsp;
         Friend: <html:checkbox property="<%=SharingThoughts.IS_FRIEND%>" name="sharingThoughtsForm" /></br>
        Other: <html:checkbox property="<%=SharingThoughts.IS_OTHER%>" name="sharingThoughtsForm" />&nbsp;&nbsp;&nbsp;&nbsp;
        (please specify) <html:text property="<%=SharingThoughts.IS_OTHER_MORE%>" name="sharingThoughtsForm"/>
      </td>
    </tr>

    <tr >
      <td width="300">Who is this feedback form about? <span class="required">*</span><br />(You may tick more than one)</td><td>
        Me: <html:checkbox property="<%=SharingThoughts.IS_ABOUT_ME%>" name="sharingThoughtsForm"/>&nbsp;&nbsp;&nbsp;&nbsp;
        Another patient: <html:checkbox property="<%=SharingThoughts.IS_ABOUT_OTHER%>" name="sharingThoughtsForm"/>&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>

    <tr >
      <td width="300">Would you prefer to remain anonymous? <span class="required">*</span></td><td>
         Yes: <html:radio property="<%=SharingThoughts.IS_ANONYMOUS%>" value="true" name="sharingThoughtsForm"/>&nbsp;&nbsp;&nbsp;&nbsp;
          No: <html:radio property="<%=SharingThoughts.IS_ANONYMOUS%>" value="false" name="sharingThoughtsForm" />&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>

    <tr >
        <td>When did this positive experience happen?</td><td><html:text name="sharingThoughtsForm" property="<%=SharingThoughts.WHEN%>"/></td>
    </tr>

    <tr >
      <td width="300">Is this positive experience a regular part of your care? <span class="required">*</span></td><td>
          Yes: <html:radio property="<%=SharingThoughts.IS_ONGOING%>" value="true" name="sharingThoughtsForm"/>&nbsp;&nbsp;&nbsp;&nbsp;
          No: <html:radio property="<%=SharingThoughts.IS_ONGOING%>" value="false" name="sharingThoughtsForm"/>&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>

    <tr >
      <td>Where did this happen? <span class="required">*</span></td><td><html:text name="sharingThoughtsForm" property="<%=SharingThoughts.LOCATION%>"/></td>
    </tr>

    <tr >
      <td>Which unit does this relate to?</td><td><html:select property="<%=SharingThoughts.UNIT_ID%>"><html:options collection="units" property="id" labelProperty="name"/></html:select></td>
    </tr>

    <tr >
      <td width="300">Please describe what was good about the care that you or others have received <span class="required">*</span></td>
      <td><html:textarea property="<%=SharingThoughts.DESCRIPTION%>" name="sharingThoughtsForm" rows="10" styleClass="textareaSharingThoughts"/></td>
    </tr>

    <tr >
        <td width="300">Do you think that this positive experience has happened to you or other patients before?</td><td>
        Definitely yes: <html:radio property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="sharingThoughtsForm" value="1" />&nbsp;&nbsp;&nbsp;&nbsp;
        Probably yes: <html:radio property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="sharingThoughtsForm" value="2" />&nbsp;&nbsp;&nbsp;&nbsp;
        Probably not:  <html:radio property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="sharingThoughtsForm" value="3" />&nbsp;&nbsp;&nbsp;&nbsp;
        Definitely not: <html:radio property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="sharingThoughtsForm" value="4" />&nbsp;&nbsp;&nbsp;&nbsp;
        Don't know: <html:radio property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="sharingThoughtsForm" value="5" /><br/>
        If you want to add any further comment, please do so below:<br/>
        <html:textarea property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE_MORE%>" name="sharingThoughtsForm" rows="3" styleClass="textareaSharingThoughts"/>
    </td>
    </tr>


    <tr >
      <td width="300">What can be done to make sure that patients always receive this quality of care? <span class="required">*</span></td>
      <td><html:textarea property="<%=SharingThoughts.SUGGESTED_ACTION%>" name="sharingThoughtsForm" rows="10" styleClass="textareaSharingThoughts"/></td>
    </tr>

    </table>
    </div>
    <p>You can save your comments to complete later, or if you are happy that you have said all that you want, you can submit your comments now.</p>
    <html:link action="/patient/sharingThoughts" styleClass="btn">Home</html:link> &nbsp;&nbsp;
    <input type="submit" value="Save Draft" label="Save Draft" name="Save Draft" class="btn"/> &nbsp;&nbsp;
    <input type="submit" value="<%=SharingThoughts.SUBMIT%>" name="<%=SharingThoughts.SUBMIT%>" class="btn btn-primary"/>
</html:form>
