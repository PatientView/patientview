package org.patientview.service;

import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.User;

import java.util.List;

public interface SharedThoughtManager {

    List<SharedThought> getAll();

    SharedThought get(Long sharedThoughtId);

    List<SharedThought> getUsersThoughts(Long userId, boolean isSubmitted);

    void save(SharedThought thought);

    void delete(Long sharedThoughtId);

    /**
     * Get list of responders suitable for a shared thought, ignores those already attached
     *
     * @param SharedThought the shared thought to add responders to
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
}
