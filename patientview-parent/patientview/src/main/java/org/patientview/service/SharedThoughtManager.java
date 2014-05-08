package org.patientview.service;

import org.patientview.model.Unit;
import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.User;

import java.util.List;

public interface SharedThoughtManager {

    /**
     * Get list of shared thoughts, ordering by submit date if required
     * @param orderBySubmitDate true if ordering by submit date
     * @return list of SharedThought
     */
    List<SharedThought> getAll(boolean orderBySubmitDate);

    /**
     * Get list of submitted shared thoughts, ordering by submit date if required
     * @param orderBySubmitDate true if ordering by submit date
     * @return list of SharedThought
     */
    List<SharedThought> getSubmitted(boolean orderBySubmitDate);

    /**
     * Get a single shared thought, auditing view if necessary
     * @param sharedThoughtId Id of the shared thought to get
     * @param auditEnabled true if audit required
     * @param staffUser true if staff user, false otherwise (used for auditing purposes)
     * @return
     */
    SharedThought get(Long sharedThoughtId, boolean auditEnabled, boolean staffUser);

    List<SharedThought> getUsersThoughts(User user, boolean isSubmitted);

    /**
     * Get list of shared thoughts that this staff user can view as a responder or administrator
     * @param user Staff user
     * @param unViewedOnly true if only retrieving unviewed shared thoughts
     * @return List of authorised shared thoughts
     */
    List<SharedThought> getStaffThoughtList(User user, boolean unViewedOnly);

    /**
     * Save shared thought, with assignment to sharing thoughts administrators if submitted
     * @param thought Shared Thought to save
     * @param isSubmitted Boolean for submission true/false
     */
    void save(SharedThought thought, boolean isSubmitted);

    /**
     * Set all responders (except current user) view status to false for this shared thought (used when updating)
     * @param sharedThought Shared Thought to update viewed status
     */
    void setUnviewed(SharedThought sharedThought);

    /**
     * Set shared thought as viewed by a user (user in responder list)
     * @param sharedThought Shared Thought to set as viewed
     * @param user User viewing
     */
    void setViewed(SharedThought sharedThought, User user);

    void delete(Long sharedThoughtId);

    /**
     * Get list of responders suitable for a shared thought, ignores those already attached
     * @param sharedThought the shared thought to add responders to
     * @return List of User responders
     */
    List<User> getOtherResponders(SharedThought sharedThought);

    /**
     * Add a responder to a shared thought
     * @param sharedThoughtId Id of the shared thought to add to
     * @param responderId Id of the responder to add
     * @return true or false based on success
     */
    boolean addResponder(Long sharedThoughtId, Long responderId);

    /**
     * Remove a responder from a shared thought
     * @param sharedThoughtId Id of the shared thought to remove from
     * @param responderId Id of the responder to remove
     * @return true or false based on success
     */
    boolean removeResponder(Long sharedThoughtId, Long responderId);

    /**
     * Add a message to a shared thought conversation
     * @param sharedThoughtId Id of the shared thought to remove from
     * @param message Text of the message to add
     * @return true or false based on success
     */
    boolean addMessage(Long sharedThoughtId, String message);

    /**
     * Check if patient has access to Sharing Thoughts (member of unit with unit.sharedThoughtEnabled = true)
     * @param user User to check if they have access
     * @return True if user can use Sharing Thoughts, false if not
     */
    boolean checkAccessSharingThoughts(User user);

    /**
     * Change shared thought between open/closed
     * @param sharedThoughtId Id of shared thought to change between open/closed
     * @return True if success, else false
     */
    boolean openCloseSharedThought(Long sharedThoughtId);

    /**
     * Get a list of units the current logged in user is a member of that are also shared thoughts enabled
     * @return List of Unit that have shared thoughts enabled
     */
    List<Unit> getLoggedInUsersUnits();

    /**
     * Send a message from the current logged in user to the patient, anonymously if shared thought is anonymous
     * @param sharedThoughtId Id of shared thought
     * @param subject Subject of message
     * @param message Message body
     * @return True if success, else false
     */
    boolean sendMessageToPatient(Long sharedThoughtId, String subject, String message);
}
