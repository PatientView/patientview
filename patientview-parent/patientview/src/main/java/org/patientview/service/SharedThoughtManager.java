package org.patientview.service;

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
     * Get a single shared thought, auditing view if necessary
     * @param sharedThoughtId Id of the shared thought to get
     * @param auditEnabled true if audit required
     * @param staffUser true if staff user, false otherwise (used for auditing purposes)
     * @return
     */
    SharedThought get(Long sharedThoughtId, boolean auditEnabled, boolean staffUser);

    List<SharedThought> getUsersThoughts(User user, boolean isSubmitted);

    /**
     * Get list of shared thoughts that this staff user can view as a responder
     * @param user Staff user
     * @return List of authorised shared thoughts
     */
    List<SharedThought> getStaffThoughtList(User user);

    /**
     * Save shared thought, with assignment to sharing thoughts administrators if submitted
     * @param thought Shared Thought to save
     * @param isSubmitted Boolean for submission true/false
     */
    void save(SharedThought thought, boolean isSubmitted);

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
}
