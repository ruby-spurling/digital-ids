package com.settp.id.core.service;

import com.settp.id.core.exception.IdentityNotFoundException;
import com.settp.id.core.exception.IllegalStatusChangeException;
import com.settp.id.core.model.DigitalID;
import com.settp.id.core.model.IdentityStatus;
import com.settp.id.core.repository.IdentityRepository;

import java.util.UUID;

public class CentralAuthority {
    private final IdentityRepository repository;

    public CentralAuthority(IdentityRepository repository) {
        this.repository = repository;
    }

    public String createIdentity() {
        String uuid = UUID.randomUUID().toString();
        DigitalID newIdentity = new DigitalID(uuid);

        repository.save(newIdentity);
        return uuid;
    }

    public void statusUpdate(String uuid, IdentityStatus newStatus) {
        DigitalID identity = repository.findByUuid(uuid).orElseThrow(() -> new IdentityNotFoundException(uuid));

        if (identity.getStatus() == IdentityStatus.REVOKED) {
            throw new IllegalStatusChangeException(identity.getStatus(), "Status Update");
        }

        identity.setStatus(newStatus);
        repository.save(identity);
    }

    public void updateAttribute(String uuid, String key, String value) {
        DigitalID identity = repository.findByUuid(uuid).orElseThrow(() -> new IdentityNotFoundException(uuid));

        if (identity.getStatus() == IdentityStatus.REVOKED) {
            throw new IllegalStatusChangeException(identity.getStatus(), "Attribute Update");
        }

        identity.setAttribute(key, value);
        repository.save(identity);
    }
}
