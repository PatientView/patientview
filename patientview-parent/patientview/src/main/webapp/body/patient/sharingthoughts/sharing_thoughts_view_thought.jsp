<%@ page import="org.patientview.patientview.sharingthoughts.SharingThoughts" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<div class="page-header-noline">
    <logic:equal value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
        <h1>Positive Comment</h1>
    </logic:equal>
    <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
        <h1>Quality or Safety Concern</h1>
    </logic:notEqual>
    <div class="sharedThoughtSubmitDate pull-right">Submitted <bean:write name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.SUBMIT_DATE%>"/></div>
</div>

<table border="0" cellspacing="1" cellpadding="3" class="table table-bordered table-striped">
    <tr >
        <th width="300">Question asked</th>
        <th>Response</th>
    </tr>

    <tr >
      <td width="300">Are you the patient on this PatientView login?</td>
      <td>
          <logic:equal value="true" property="<%=SharingThoughts.IS_PATIENT%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
              I am the patient
          </logic:equal>
          <logic:equal value="false" property="<%=SharingThoughts.IS_PATIENT%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
              I am not the patient
          </logic:equal>
      </td>
    </tr>

    <tr >
        <td width="300">If no, which of these are you?<br />(You may tick more than one)</td><td>
            <logic:equal value="true" property="<%=SharingThoughts.IS_PRINCIPAL_CARER%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                Principal carer
            </logic:equal>
            <logic:equal value="true" property="<%=SharingThoughts.IS_RELATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                <br />Relative
            </logic:equal>
            <logic:equal value="true" property="<%=SharingThoughts.IS_FRIEND%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                <br />Friend
            </logic:equal>
            <logic:equal value="true" property="<%=SharingThoughts.IS_OTHER%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                <br />Other
                <logic:notEmpty property="<%=SharingThoughts.IS_OTHER_MORE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                    (<bean:write property="<%=SharingThoughts.IS_OTHER_MORE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" />)
                </logic:notEmpty>
            </logic:equal>
        </td>
    </tr>

    <tr >
        <td width="300">Who is this feedback form about?<br />(You may tick more than one)</td>
        <td>
            <logic:equal value="true" property="<%=SharingThoughts.IS_ABOUT_ME%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                Me
            </logic:equal>
            <logic:equal value="true" property="<%=SharingThoughts.IS_ABOUT_OTHER%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                <br />Another patient
            </logic:equal>
        </td>
    </tr>

    <tr >
        <td width="300">Would you prefer to remain anonymous?</td>
        <td>
          <logic:equal value="true" property="<%=SharingThoughts.IS_ANONYMOUS%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
              Yes
          </logic:equal>
          <logic:equal value="false" property="<%=SharingThoughts.IS_ANONYMOUS%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
              No
          </logic:equal>
        </td>
    </tr>

    <tr>
        <logic:equal value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">When did this positive experience happen?</td>
        </logic:equal>
        <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">When did this quality or safety concern happen?</td>
        </logic:notEqual>
        <td><bean:write name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.WHEN%>"/></td></tr>

    <tr >
        <logic:equal value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">Is this positive experience a regular part of your care?</td>
        </logic:equal>
        <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">Is this quality or safety concern a regular part of your care?</td>
        </logic:notEqual>
        <td>
            <logic:equal value="true" property="<%=SharingThoughts.IS_ONGOING%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                Yes
            </logic:equal>
            <logic:equal value="false" property="<%=SharingThoughts.IS_ONGOING%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                No
            </logic:equal>
        </td>
    </tr>

    <tr >
        <td>Where did this happen?</td><td><bean:write name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.LOCATION%>"/></td>
    </tr>

    <tr >
        <td>Which unit does this relate to?</td><td><bean:write name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.UNIT_NAME%>"/></td>
    </tr>

    <tr >
        <logic:equal value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">Please describe what was good about the care that you or others have received</td>
        </logic:equal>
        <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">Please tell us what happened</td>
        </logic:notEqual>
        <td><bean:write property="<%=SharingThoughts.DESCRIPTION%>" name="<%=SharingThoughts.THOUGHT_PARAM%>"  /></td>
    </tr>

    <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
    <tr >
        <td width="300">Why do you feel this was a concern for you?</td>
        <td><bean:write property="<%=SharingThoughts.CONCERN_REASON%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" /></td>
    </tr>
    </logic:notEqual>

    <tr>
        <logic:equal value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">Do you think that this positive experience has happened to you or other patients before?</td>
        </logic:equal>
        <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">Do you think that this quality or safety concern has happened to you or other patients before?</td>
        </logic:notEqual>
        <td>
            <logic:equal value="1" property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                Definitely yes
            </logic:equal>
            <logic:equal value="2" property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                Probably yes
            </logic:equal>
            <logic:equal value="3" property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                Probably not
            </logic:equal>
            <logic:equal value="4" property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                Definitely not
            </logic:equal>
            <logic:equal value="5" property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                Don't know
            </logic:equal>

            <logic:notEmpty property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE_MORE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                <br/>Comment: '<bean:write property="<%=SharingThoughts.LIKELIHOOD_0F_RECURRENCE_MORE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" />'
            </logic:notEmpty>
      </td>
    </tr>

    <tr >
        <logic:equal value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">What can be done to make sure that patients always receive this quality of care?</td>
        </logic:equal>
        <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">What do you think could be done to stop this from happening again to you or other patients?</td>
        </logic:notEqual>
        <td><bean:write property="<%=SharingThoughts.SUGGESTED_ACTION%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" /></td>
    </tr>

    <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
        <tr>
            <td width="300">On a scale of 1-5, how serious do you think your concern was?<br />1 = Less Serious, 5 = More Serious</td>
            <td><bean:write property="<%=SharingThoughts.HOW_SERIOUS%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" /></td>
        </tr>
    </logic:notEqual>
</table>

<html:link action="/patient/sharingThoughts"><html:submit value="Home" styleClass="btn formbutton" /></html:link>

