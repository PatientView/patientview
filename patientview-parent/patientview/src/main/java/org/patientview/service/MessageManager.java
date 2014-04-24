/*
 * PatientView
 *
 * Copyright (c) Worth Solutions Limited 2004-2013
 *
 * This file is part of PatientView.
 *
 * PatientView is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * PatientView is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with PatientView in a file
 * titled COPYING. If not, see <http://www.gnu.org/licenses/>.
 *
 * @package PatientView
 * @link http://www.patientview.org
 * @author PatientView <info@patientview.org>
 * @copyright Copyright (c) 2004-2013, Worth Solutions Limited
 * @license http://www.gnu.org/licenses/gpl-3.0.html The GNU General Public License V3.0
 */

package org.patientview.service;

import org.patientview.model.Unit;
import org.patientview.patientview.model.Conversation;
import org.patientview.patientview.model.ConversationStatus;
import org.patientview.patientview.model.Message;
import org.patientview.patientview.model.MessageRecipient;
import org.patientview.patientview.model.User;

import javax.servlet.ServletContext;
import java.util.List;

public interface MessageManager {

    Conversation getConversation(Long conversationId, User user);

    /**
     * This will get the conversation but applied to the current user
     * So will include number of number of unread messages etc and other users will be set
     * @param conversationId Long
     * @param participantId Long
     * @return Conversation
     */
    Conversation getConversationForUser(Long conversationId, Long participantId);

    List<Conversation> getConversations(Long participantId);

    void saveConversation(Conversation conversation);

    void deleteConversation(Long conversationId);

    void deleteConversation(Conversation conversation);

    List<ConversationStatus> getConversationStatus();

    ConversationStatus getConversationStatus(Long id);

    List<Message> getMessages(Long conversationId);

    /**
     * Create a new conversation between two participants and creates first message
     * @param context The current session servlet context
     * @param subject Subject of the conversation
     * @param content Content of the first message in the conversation
     * @param sender Sending user
     * @param recipient User the message is being sent to
     * @return
     * @throws Exception
     */
    Message createMessage(ServletContext context, String subject, String content, User sender, User recipient)
            throws Exception;

    /**
     * Create a new conversation with between two participants, including image data and creates first message
     * @param context The current session servlet context
     * @param subject Subject of the conversation
     * @param content Content of the first message in the conversation
     * @param sender Sending user
     * @param recipient User the message is being sent to
     * @param imageData Image data, usually a screenshot
     * @return
     * @throws Exception
     */
    Message createMessage(ServletContext context, String subject, String content, User sender, User recipient
            , String imageData, Boolean isFeedback) throws Exception;

    Message createGroupMessage(ServletContext context, String subject, String content, User sender,
                               String groupName, String type, Unit unit) throws Exception;

    Message replyToMessage(ServletContext context, String content, Long conversationId, User sender) throws Exception;

    int getTotalNumberUnreadMessages(Long recipientId);

    /**
     * Marks the conversation as read only if logged in user is the recipient
     */
    void markMessagesAsReadForConversation(Long loggedInUserId, Long conversationId);

    List<MessageRecipient> getUnitAdminRecipients(List<Unit> units, User requestingUser);

    List<User> getUnitAdminRecipients(Unit unit, User requestingUser);

    List<MessageRecipient> getUnitStaffRecipients(List<Unit> units, User requestingUser);

    List<User> getUnitStaffRecipients(Unit unit, User requestingUser);

    List<MessageRecipient> getUnitPatientRecipients(List<Unit> units, User requestingUser);

    List<User> getUnitPatientRecipients(Unit unit, User requestingUser);

    List<User> getUnitPatientRecipients(Unit unit, String name, User requestingUser);

    List<Unit> getMessagingEnabledUnitsForLoggedInUser();

    /**
     * Get list of suitable recipients for feedback based on current user
     * @param user
     * @return
     */
    List<User> getFeedbackRecipients(User user);
}
