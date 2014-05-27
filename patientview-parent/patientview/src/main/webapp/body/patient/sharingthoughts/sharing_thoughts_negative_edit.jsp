<%@ page import="org.patientview.patientview.sharingthoughts.SharingThoughts" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<div class="page-header-noline">
    <h1>Edit Quality or Safety Concern</h1>
    <p align="left">Please complete the questions and statements below by either clicking the appropriate box or adding your comments in the space provided</p>
</div>
<html:form action="/patient/sharingThoughtSave" styleClass="formNegative">

    <html:hidden name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.ID%>" />
    <html:hidden property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" value="-1" />
    <div class="divNegative">
    <table border="0" cellspacing="1" cellpadding="3" class="table table-bordered table-striped tableNegative">
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
            Yes: <html:radio property="<%=SharingThoughts.IS_PATIENT%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>" value="true" />&nbsp;&nbsp;&nbsp;&nbsp;
            No: <html:radio property="<%=SharingThoughts.IS_PATIENT%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>" value="false" />&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
        </tr>

        <tr >
            <td width="300">If no, which of these are you?<br />(You may tick more than one)</td><td>
            Carer: <html:checkbox property="<%=SharingThoughts.IS_PRINCIPAL_CARER%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>" />&nbsp;&nbsp;&nbsp;&nbsp;
            Relative: <html:checkbox property="<%=SharingThoughts.IS_RELATIVE%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>" />&nbsp;&nbsp;&nbsp;&nbsp;
            Friend: <html:checkbox property="<%=SharingThoughts.IS_FRIEND%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>" /></br>
            Other: <html:checkbox property="<%=SharingThoughts.IS_OTHER%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" />&nbsp;&nbsp;&nbsp;&nbsp;
            (please specify) <html:text property="<%=SharingThoughts.IS_OTHER_MORE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>"/>
        </td>
        </tr>

        <tr >
            <td width="300">Who is this feedback form about? <span class="required">*</span><br />(You may tick more than one)</td><td>
            Me: <html:checkbox property="<%=SharingThoughts.IS_ABOUT_ME%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>"/>&nbsp;&nbsp;&nbsp;&nbsp;
            Another patient: <html:checkbox property="<%=SharingThoughts.IS_ABOUT_OTHER%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>"/>&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
        </tr>

        <tr >
            <td width="300">Would you prefer to remain anonymous? <span class="required">*</span></td><td>
            Yes: <html:radio property="<%=SharingThoughts.IS_ANONYMOUS%>" value="true"  name="<%=SharingThoughts.THOUGHT_PARAM%>"/>&nbsp;&nbsp;&nbsp;&nbsp;
            No: <html:radio property="<%=SharingThoughts.IS_ANONYMOUS%>" value="false"  name="<%=SharingThoughts.THOUGHT_PARAM%>"/>&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
        </tr>

        <tr >
            <td>When did this quality or safety concern happen?</td><td><html:text name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.WHEN%>"/></td>
        </tr>

        <tr >
          <td width="300">Is this quality or safety concern a regular part of your care? <span class="required">*</span></td><td>
            Yes: <html:radio property="<%=SharingThoughts.IS_ONGOING%>" value="true" name="<%=SharingThoughts.THOUGHT_PARAM%>"/>&nbsp;&nbsp;&nbsp;&nbsp;
            No: <html:radio property="<%=SharingThoughts.IS_ONGOING%>" value="false" name="<%=SharingThoughts.THOUGHT_PARAM%>"/>&nbsp;&nbsp;&nbsp;&nbsp;
          </td>
        </tr>

        <tr >
            <td>Where did this happen? <span class="required">*</span></td><td><html:text name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.LOCATION%>"/></td>
        </tr>

        <tr >
            <td>Which unit does this relate to?</td><td><html:select name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.UNIT_ID%>"><html:options collection="units" property="id" labelProperty="name" /></html:select></td>
        </tr>

        <tr >
            <td width="300">Please tell us what happened <span class="required">*</span></td>
            <td><html:textarea property="<%=SharingThoughts.DESCRIPTION%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" rows="10" styleClass="textareaSharingThoughts"/></td>
        </tr>

        <tr >
            <td width="300">Why do you feel this was a concern for you? <span class="required">*</span></td>
            <td><html:textarea property="<%=SharingThoughts.CONCERN_REASON%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" rows="10" styleClass="textareaSharingThoughts"/></td>
        </tr>

        <tr>
            <td width="300">Do you think that this quality or safety concern has happened to you or other patients before? <span class="required">*</span></td><td>
            Definitely yes: <html:radio property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" value="1" />&nbsp;&nbsp;&nbsp;&nbsp;
            Probably yes: <html:radio property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" value="2" />&nbsp;&nbsp;&nbsp;&nbsp;
            Probably not:  <html:radio property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" value="3" />&nbsp;&nbsp;&nbsp;&nbsp;
            Definitely not: <html:radio property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" value="4" />&nbsp;&nbsp;&nbsp;&nbsp;
            Don't know: <html:radio property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" value="5" /><br/>
            If you want to add any further comment, please do so below:<br/>
            <html:textarea property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE_MORE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" rows="3" styleClass="textareaSharingThoughts"/>
          </td>
        </tr>

        <tr >
            <td width="300">What could be done to stop this from happening again to you or other patients? <span class="required">*</span></td>
            <td><html:textarea property="<%=SharingThoughts.SUGGESTED_ACTION%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" rows="10" styleClass="textareaSharingThoughts"/></td>
        </tr>

        <tr>
            <td width="300">On a scale of 1-5, how serious do you think your concern was? <span class="required">*</span></td><td>
            Not Serious&nbsp;&nbsp;1:<html:radio property="<%=SharingThoughts.HOW_SERIOUS%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>" value="1" />&nbsp;&nbsp;
            2: <html:radio property="<%=SharingThoughts.HOW_SERIOUS%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>" value="2" />&nbsp;&nbsp;
            3:  <html:radio property="<%=SharingThoughts.HOW_SERIOUS%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>" value="3" />&nbsp;&nbsp;
            4: <html:radio property="<%=SharingThoughts.HOW_SERIOUS%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>" value="4" />&nbsp;&nbsp;
            5: <html:radio property="<%=SharingThoughts.HOW_SERIOUS%>"  name="<%=SharingThoughts.THOUGHT_PARAM%>" value="5" />&nbsp;&nbsp;Extremely Serious
        </td>
        </tr>
    </table>
    </div>
    <br/>
    <p>You can save your comments to complete later, or if you are happy that you have said all that you want, you can submit your comments now.</p>
    <html:link action="/patient/sharingThoughts" styleClass="btn">Home</html:link> &nbsp;&nbsp;
    <input type="submit" value="Save Draft" label="Save Draft" name="Save Draft" class="btn"/> &nbsp;&nbsp;
    <input type="submit" value="<%=SharingThoughts.SUBMIT%>" name="<%=SharingThoughts.SUBMIT%>" class="btn btn-primary"/>
</html:form>