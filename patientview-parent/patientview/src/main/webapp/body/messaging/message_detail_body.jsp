<%@ page import="org.patientview.patientview.model.User" %>
<%@ page import="org.patientview.utils.LegacySpringUtils" %>
<%@ page import="org.patientview.patientview.model.enums.ConversationType" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

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

<%
    String actionPrefix = "patient";

    try {
        actionPrefix = request.getParameter("actionPrefix");
    } catch (Exception e) {
        // Birds the word
    }

    User user = LegacySpringUtils.getUserManager().getLoggedInUser();
%>

<div class="row">
    <div class="<%= (actionPrefix.equals("patient") ? "span12" : "span9") %>">
        <logic:notPresent name="conversation">
            <div class="alert alert-error">
                <strong>Conversation not found.</strong>
            </div>
        </logic:notPresent>
        <logic:present name="conversation">
            <div class="page-header">
                <div>
                    <a href="/<%=actionPrefix%>/conversations.do" class="btn">Back to Messages</a>
                </div>

                <h1><br />Subject: <bean:write name="conversation" property="subject" /></h1>
                <h4 class="author">With: <bean:write name="conversation" property="otherUser.name" /></h4>

                <logic:equal name="conversation" property="participant2Anonymous" value="true">
                    <logic:equal name="conversation" property="participant2.id" value="<%= Long.toString(user.getId()) %>">
                        <br/><h3 align="center">Anonymity</h3>
                        <p>By receiving or replying to this message, staff from your renal unit and the research team,
                        are still unable to identify you. If you decide to respond, you do not need to say who you are
                        or provide any information that may reveal your identify. However, you may reveal who you are
                        in any response you provide.</p>
                    </logic:equal>
                </logic:equal>

                <logic:equal name="conversation" property="participant1Anonymous" value="true">
                    <logic:equal name="conversation" property="participant1.id" value="<%= Long.toString(user.getId()) %>">
                        <br/><h3 align="center">Anonymity</h3>
                        <p>By receiving or replying to this message, staff from your renal unit and the research team,
                        are still unable to identify you. If you decide to respond, you do not need to say who you are
                        or provide any information that may reveal your identify. However, you may reveal who you are
                        in any response you provide.</p>
                    </logic:equal>
                </logic:equal>

                <logic:equal value="<%=ConversationType.FEEDBACK.toString()%>" name="conversation" property="type">
                    <!-- only shown for conversations with type ConversationType.FEEDBACK.toString() -->
                    <logic:present role="patient">
                        <logic:present name="conversation" property="conversationStatus">
                            <logic:equal value="true" name="conversation" property="clinicianClosed">
                                <div id="conversationStatusText">Status: Closed</div>
                            </logic:equal>
                            <logic:equal value="false" name="conversation" property="clinicianClosed">
                                <div id="conversationStatusText">Status: Open</div>
                            </logic:equal>
                        </logic:present>
                        <logic:notPresent name="conversation" property="conversationStatus">
                            <div id="conversationStatusText">Status: Open</div>
                        </logic:notPresent>
                        <logic:equal value="true" name="conversation" property="clinicianClosed">
                            <div id="conversationRating">
                                How would you rate this conversation? <div class="rateit" data-rateit-resetable="false" data-rateit-step="1" data-rateit-value="<bean:write name="conversation" property="rating" />"></div>
                                <span id="conversationRatingInfo"></span>
                            </div>
                        </logic:equal>
                    </logic:present>

                    <logic:notPresent role="patient">
                        <logic:notEmpty name="conversationStatusOptions">
                            <logic:present name="conversation" property="conversationStatus">
                                <input type="hidden" id="conversationStatusHidden" value="<bean:write name="conversation" property="conversationStatus.id" />"/>
                            </logic:present>
                            <div id="conversationStatus">Status: &nbsp;
                                <select id="selectConversationStatus">
                                    <option value="-1">Open</option>
                                    <logic:iterate name="conversationStatusOptions" id="statusOption">
                                        <option value="<bean:write name="statusOption" property="id" />"><bean:write name="statusOption" property="status"/></option>
                                    </logic:iterate>
                                </select>
                                <a href="#" class="btn" id="btnSetConversationStatus">Set Status</a> &nbsp;&nbsp;<span id="conversationStatusInfo"></span>
                            </div>
                        </logic:notEmpty>
                        <logic:present name="conversation" property="imageData">
                            <img class="imageData boxShadow1" src="<bean:write name="conversation" property="imageData" />"/>
                            <a href="../web/feedback/downloadImage?conversationId=<bean:write name="conversation" property="id"/>" target="_blank">Download Screenshot</a>
                        </logic:present>
                    </logic:notPresent>
                </logic:equal>
            </div>

            <section class="js-messages">
                <logic:present name="messages">
                    <logic:notEmpty name="messages">
                        <logic:iterate name="messages" id="message" indexId="index" type="org.patientview.patientview.model.Message">
                            <article class="message" id="message-<bean:write name="message" property="id" />">
                                <h4 class="author">
                                    <%
                                        // only show other persons name if not anonymous conversation
                                        User participant1 = message.getConversation().getParticipant1();
                                        User participant2 = message.getConversation().getParticipant2();

                                        if ((message.getConversation().isParticipant2Anonymous()
                                                && message.getSender().equals(participant2))
                                         || (message.getConversation().isParticipant1Anonymous()
                                                && message.getSender().equals(participant1))) {
                                    %>
                                        Anonymous User
                                    <% } else { %>
                                        <bean:write name="message" property="sender.name" />
                                    <% } %>

                                    <%
                                    // check to see if they are the recipient of this message and if they have seen before
                                    if (message.getType() == null && message.getRecipient().equals(user)) {
                                        if (!message.isHasRead()) {
                                        %>
                                        <span class="badge badge-important">
                                            New
                                        </span>
                                        <%
                                        }
                                    }
                                    %>

                                    <span class="label label-inverse pull-right date"><bean:write name="message" property="friendlyDate" /></span>
                                </h4>

                                <div class="content dull">
                                    <bean:write name="message" property="formattedContent" filter="false"/>
                                </div>
                            </article>
                        </logic:iterate>
                    </logic:notEmpty>
                    <logic:empty name="messages">
                        <div class="alert">
                            <strong>You do not have any messages.</strong>
                        </div>
                    </logic:empty>
                </logic:present>
            </section>

            <logic:notPresent name="isReaderTheRecipient">
                <section class="new-message-container" id="response">
                    <form action="/send-message.do" class="js-message-form">
                        <input type="hidden" class="js-message-redirect" value="/patient/conversation.do" />
                        <input type="hidden" class="js-message-conversation-id" value="<bean:write name="conversation" property="id" />" />
                        <logic:present name="isBulkMessage">
                            <div class="alert">
                            <strong>This message was sent to <bean:write name="bulk_message_recipient"/> in <bean:write name="recipient_unit"/>. It is not possible to reply to it.</strong>
                        </div>
                        </logic:present>
                        <logic:notPresent name="isBulkMessage">
                            <logic:equal value="false" name="conversation" property="clinicianClosed">
                                <textarea rows="6" cols="3" name="content" class="<%= (actionPrefix.equals("patient") ? "span12" : "span9") %> new-message js-message-content"></textarea>
                            </logic:equal>
                        </logic:notPresent>
                        <div class="alert alert-error js-message-errors" style="display: none">
                            <strong>You do not have any messages.</strong>
                        </div>
                        <logic:notPresent name="isBulkMessage">
                            <logic:equal value="false" name="conversation" property="clinicianClosed">
                                <input type="submit" value="Reply" class="pull-right btn btn-primary js-message-submit-btn" />
                            </logic:equal>
                        </logic:notPresent>
                    </form>
                </section>
            </logic:notPresent>
        </logic:present>
    </div>
</div>
<script src="/js/messages.js" type="text/javascript"></script>
<script src="/js/jquery.dataTables.min.js" type="text/javascript"></script>
