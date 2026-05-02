package com.settp.id.core.repository;

import com.settp.id.core.model.DigitalID;

import java.util.Optional;

public interface IdentityRepository {
    void save(DigitalID identity);

    Optional<DigitalID> findByUuid(String uuid);

    boolean exists (String uuid);

    Iterable<DigitalID> findAll();
}
