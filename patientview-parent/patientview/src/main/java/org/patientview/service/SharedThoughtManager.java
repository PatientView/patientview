package org.patientview.service;

import org.patientview.patientview.model.SharedThought;

import java.util.List;

public interface SharedThoughtManager {

    List<SharedThought> getAll();

    SharedThought getSharedThought(Long sharedThoughtId);

    List<SharedThought> getUsersThoughts(Long userId, boolean isSubmitted);

    void save(SharedThought thought);

    void delete(Long sharedThoughtId);
}
