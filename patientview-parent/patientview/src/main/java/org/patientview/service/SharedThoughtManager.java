package org.patientview.service;

import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.User;

import java.util.List;

public interface SharedThoughtManager {

    List<SharedThought> getAll();

    SharedThought get(Long sharedThoughtId);

    List<SharedThought> getUsersThoughts(Long userId, boolean isSubmitted);

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
}
