<%@ page import="org.patientview.patientview.sharingthoughts.SharingThoughts" %>
<%@ page import="org.patientview.patientview.model.enums.SharedThoughtAuditAction" %>
<%@ page import="org.patientview.patientview.model.User" %>
<%@ page import="org.patientview.patientview.model.SharedThoughtAudit" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:xhtml/>
<%-- Message to patient dialog --%>

<div id="messageBackground"></div>
<div id="messageModalSharingThoughts" class="boxShadow1">
    <form id="js-message-form">
        <h2>Send Message to Patient</h2>
        <p id="messageSubtitle"/><br/>
        <div class="alert alert-error" id="js-message-error-found"></div>
        <div class="control-group">
            <label class="control-label">Subject</label>
            <div class="controls"><input name="subject" id="js-message-subject"/></div>
            <div class="alert alert-error" id="js-message-subject-error"></div>
        </div>
        <div class="control-group">
            <label class="control-label">Message</label>
            <div class="controls"><textarea name="message" id="js-message-message"></textarea></div>
            <div class="alert alert-error" id="js-message-message-error"></div>
        </div>
        <div class="control-group">
            <div class="controls">
                <input type="submit" value="Send message" id="js-message-submit-btn" class="btn btn-primary"/>&nbsp;
                <input type="submit" value="Cancel" id="js-message-cancel-btn" class="btn"/>
            </div>
        </div>
    </form>
    <div id="js-message-loading">
        <h2>Sending message</h2>
        <p>Please wait for your message to be sent</p>
    </div>
    <div id="js-message-success">
        <h2>Message Sent</h2>
        <br/>
        <div class="alert alert-success">Your message has been sent</div>
        <input type="submit" value="Close" id="js-message-close-btn" class="btn"/>
    </div>
</div>

<div class="span9">
<div class="page-header-noline">
    <logic:equal value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
        <h1>Positive Comment</h1>
    </logic:equal>
    <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
        <h1>Quality or Safety Concern</h1>
    </logic:notEqual>
    <div class="sharedThoughtSubmitDate pull-left">
        Submitted <bean:write name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.SUBMIT_DATE%>"/>
    </div>
    <div class="pull-right">
        <html:submit value="Send Message to Patient" styleClass="btn btn-primary formbutton" styleId="btnSendMessage"/>
    </div>
</div>

<html:hidden name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.ID%>" styleId="sharedThoughtId"/>
<html:hidden name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.IS_ANONYMOUS%>" styleId="sharedThoughtIsAnonymous"/>

<logic:equal value="false" property="<%=SharingThoughts.IS_ANONYMOUS%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
    <table border="0" cellspacing="1" cellpadding="3" class="table table-bordered table-striped">
        <tr><th colspan="2">Patient Details</th></tr>
        <tr>
            <td width="300">Name</td><td>
            <bean:write name="<%=SharingThoughts.THOUGHT_PARAM%>" property="user.firstName"/>
            <bean:write name="<%=SharingThoughts.THOUGHT_PARAM%>" property="user.lastName"/>
        </td>
        </tr>
        <tr>
            <td>NHS Number</td><td>
            <bean:define id="userMappings" name="<%=SharingThoughts.THOUGHT_PARAM%>" property="user.userMappings"/>
            <logic:iterate name="userMappings" id="userMapping" length="1">
                <bean:write name="userMapping" property="nhsno"/>
            </logic:iterate>
        </td>
        </tr>
    </table>
</logic:equal>

<table border="0" cellspacing="1" cellpadding="3" class="table table-bordered table-striped">
    <tr>
        <th width="300">Question asked</th>
        <th>Response</th>
    </tr>

    <tr>
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

    <tr>
        <td width="300">If no, which of these are you?<br />(You may tick more than one)</td><td>
        <logic:equal value="true" property="<%=SharingThoughts.IS_PRINCIPAL_CARER%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            Carer
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

    <tr>
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

    <tr>
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

    <tr>
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

    <tr><td>Where did this happen?</td><td><bean:write name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.LOCATION%>"/></td></tr>
    <tr><td>Which unit does this relate to?</td><td><bean:write name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.UNIT_NAME%>"/></td></tr>

    <tr>
        <logic:equal value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">Please describe what was good about the care that you or others have received</td>
        </logic:equal>
        <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">Please tell us what happened</td>
        </logic:notEqual>
        <td><bean:write property="<%=SharingThoughts.DESCRIPTION%>" name="<%=SharingThoughts.THOUGHT_PARAM%>"  /></td>
    </tr>

    <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
        <tr>
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

    <tr>
        <logic:equal value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">What can be done to make sure that patients always receive this quality of care?</td>
        </logic:equal>
        <logic:notEqual value="1" property="<%=SharingThoughts.POSITIVE_NEGATIVE%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
            <td width="300">What could be done to stop this from happening again to you or other patients?</td>
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

<h2>Responders</h2>
<p>Staff must be members of the unit referred to in the Shared Thought and either a Sharing Thoughts Administrator or
    Responder. Sharing Thoughts Administrators are added as Responders to all new Shared Thoughts in their units.
    Responders can only see the Shared Thought if listed below.</p>
<br/>
<table border="0" cellspacing="1" cellpadding="3" class="table table-bordered table-striped" id="tableOtherSharedThoughtResponders">
    <tbody>
    <logic:notEmpty name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.RESPONDERS%>">
        <bean:define id="responders" name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.RESPONDERS%>"/>
        <logic:iterate name="responders" id="responder">
            <tr>
                <td><bean:write name="responder" property="user.name"/></td>
                <logic:equal value="true" name="user" property="<%=SharingThoughts.SHARED_THOUGHT_ADMINISTRATOR%>" >
                    <td class='tdUserSharedThought'><a class="btn removeUserSharedThought" data-userId="<bean:write name="responder" property="user.id"/>">Remove</a></td>
                </logic:equal>
            </tr>
        </logic:iterate>
    </logic:notEmpty>
    <logic:equal value="true" name="user" property="<%=SharingThoughts.SHARED_THOUGHT_ADMINISTRATOR%>" >
        <tr id="trOtherSharedThoughtResponders"><td colspan="2">
            <select id="selectOtherSharedThoughtResponders"></select>
            <html:submit value="Add Responder" styleClass="btn formbutton" styleId="btnAddOtherSharedThoughtResponder"/> &nbsp;&nbsp;
            <span id="messageAddOtherSharedThoughtResponder"></span>
        </td></tr>
    </logic:equal>
    </tbody>
</table>

<h2>Conversation</h2>
<br/>
<table border="0" cellspacing="1" cellpadding="3" class="table table-bordered table-striped" id="tableOtherSharedThoughtResponders">
    <tbody>
    <logic:notEmpty name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.CONVERSATION%>">
        <bean:define id="messages" name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.CONVERSATION_MESSAGES%>"/>
        <logic:iterate name="messages" id="message">
            <tr>
                <td colspan="2">
                    <span class="sharingThoughtsMessageAuthor"><bean:write name="message" property="sender.name"/>: </span>
                    <span class="sharingThoughtsMessageContent"><bean:write name="message" property="content"/></span>
                    <span class="pull-right sharingThoughtsMessageDate"><bean:write name="message" property="formattedDate"/></span>
                </td>
            </tr>
        </logic:iterate>
    </logic:notEmpty>
    <logic:empty name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.CONVERSATION%>">
        <tr id="trNoComments"><td colspan="2">No messages from responding staff yet</td></tr>
    </logic:empty>
    <logic:equal value="false" name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.CLOSED%>" >
        <tr>
            <td><textarea id="textareaMessage"></textarea></td>
            <td><html:submit value="Add Message" styleClass="btn formbutton" styleId="btnAddMessage"/></td>
            <input type="hidden" id="userFullName" value="<bean:write name="user" property="name"/>"/>
        </tr>
    </logic:equal>
    <logic:equal value="true" name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.CLOSED%>" >
        <tr>
            <td colspan="2" class="sharedThoughtClosed">This Shared Thought has been closed, no more messages can be added</td>
        </tr>
    </logic:equal>
    </tbody>
</table>

<logic:equal value="true" name="user" property="<%=SharingThoughts.SHARED_THOUGHT_ADMINISTRATOR%>" >
    <h2>Audit Events</h2>
    <br/>
    <table border="0" cellspacing="1" cellpadding="3" class="table table-bordered table-striped" id="tableAuditEvents">
        <thead>
            <th>Date</th>
            <th>User</th>
            <th>Action</th>
            <th>Extra Info</th>
        </thead>
        <tbody>
        <logic:notEmpty name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.AUDITS%>">
            <bean:define id="audits" name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.AUDITS%>"/>
            <logic:iterate name="audits" id="audit" type="org.patientview.patientview.model.SharedThoughtAudit">
                <tr>
                    <td class="auditDate"><bean:write name="audit" property="dateFormatted"/></td>
                    <td class="auditUser">
                        <%-- for PATIENT_VIEW, SAVE, SUBMIT only show name if thought is not anonymous --%>
                        <logic:equal value="true" property="<%=SharingThoughts.IS_ANONYMOUS%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                            <logic:equal value="<%=SharedThoughtAuditAction.PATIENT_VIEW.toString()%>" name="audit" property="action">
                                Anonymous
                            </logic:equal>
                            <logic:equal value="<%=SharedThoughtAuditAction.SAVE.toString()%>" name="audit" property="action">
                                Anonymous
                            </logic:equal>
                            <logic:equal value="<%=SharedThoughtAuditAction.SUBMIT.toString()%>" name="audit" property="action">
                                Anonymous
                            </logic:equal>

                            <%-- both patients (at creation) and admins can add responder --%>
                            <logic:equal value="<%=SharedThoughtAuditAction.ADD_RESPONDER.toString()%>" name="audit" property="action">
                                <% if (audit.getUser().equals(audit.getSharedThought().getUser())) { %>
                                    Anonymous
                                <% } else { %>
                                    <bean:write name="audit" property="user.name" />
                                <% } %>
                            </logic:equal>

                            <logic:equal value="<%=SharedThoughtAuditAction.STAFF_VIEW.toString()%>" name="audit" property="action">
                                <bean:write name="audit" property="user.name"/>
                            </logic:equal>
                            <logic:equal value="<%=SharedThoughtAuditAction.ADD_MESSAGE.toString()%>" name="audit" property="action">
                                <bean:write name="audit" property="user.name"/>
                            </logic:equal>
                            <logic:equal value="<%=SharedThoughtAuditAction.REMOVE_RESPONDER.toString()%>" name="audit" property="action">
                                <bean:write name="audit" property="user.name"/>
                            </logic:equal>
                            <logic:equal value="<%=SharedThoughtAuditAction.OPEN.toString()%>" name="audit" property="action">
                                <bean:write name="audit" property="user.name"/>
                            </logic:equal>
                            <logic:equal value="<%=SharedThoughtAuditAction.CLOSE.toString()%>" name="audit" property="action">
                                <bean:write name="audit" property="user.name"/>
                            </logic:equal>
                        </logic:equal>
                        <logic:equal value="false" property="<%=SharingThoughts.IS_ANONYMOUS%>" name="<%=SharingThoughts.THOUGHT_PARAM%>" >
                            <bean:write name="audit" property="user.name"/>
                        </logic:equal>


                    </td>
                    <td class="auditAction"><bean:write name="audit" property="action"/></td>
                    <td class="auditExtraInfo">
                        <logic:notEmpty name="audit" property="message">
                            '<bean:write name="audit" property="message.content"/>'
                        </logic:notEmpty>
                        <logic:notEmpty name="audit" property="responder">
                            <bean:write name="audit" property="responder.name"/>
                        </logic:notEmpty>
                    </td>
                </tr>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.AUDITS%>">
            <tr id="trNoAuditEvents"><td colspan="4">No events recorded</td></tr>
        </logic:empty>
        </tbody>
    </table>

    <logic:notEmpty name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.AUDITS%>">
        <script type="text/javascript" src="/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript">
            $('#tableAuditEvents').dataTable({
                "iDisplayLength": 10
            });
        </script>
    </logic:notEmpty>

</logic:equal>

<br/>

<html:link action="/control/sharingThoughts"><html:submit value="Back" styleClass="btn formbutton" /></html:link> &nbsp;

<logic:equal value="true" name="user" property="<%=SharingThoughts.SHARED_THOUGHT_ADMINISTRATOR%>" >
    <logic:equal value="false" name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.CLOSED%>" >
        <html:submit value="Close Shared Thought" styleClass="btn formbutton" styleId="btnOpenCloseSharedThought"/>
    </logic:equal>
    <logic:equal value="true" name="<%=SharingThoughts.THOUGHT_PARAM%>" property="<%=SharingThoughts.CLOSED%>" >
        <html:submit value="Open Shared Thought" styleClass="btn formbutton" styleId="btnOpenCloseSharedThought"/>
    </logic:equal>
</logic:equal>

</div>

<script type="text/javascript" src="/js/sharedThought.js"></script>
