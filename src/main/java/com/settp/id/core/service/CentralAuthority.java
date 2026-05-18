package com.settp.id.core.service;

import com.settp.id.core.exception.*;
import com.settp.id.core.model.DigitalID;
import com.settp.id.core.model.IdentityStatus;
import com.settp.id.core.model.Organisation;
import com.settp.id.core.repository.IdentityRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

public class CentralAuthority {
    private final IdentityRepository repository;

    public CentralAuthority(IdentityRepository repository) {
        this.repository = repository;
    }

    public static final String[] optionalAttributes = {
            "right_to_work",
            "residency_status",
            "ni_number",
            "driving_restriction",
            "driving_license_category",
            "driving_penalty_points"
    };

    public String createIdentity(Organisation requester) {
        if (requester != Organisation.CENTRAL_AUTHORITY) {
            SecurityLogger.logUnauthorisedAttempt("NULL", requester.name(), "attempted to create new ID");
            throw new UnauthorisedAccessException(requester.name(), "ID creation");
        }

        String uuid = UUID.randomUUID().toString();
        DigitalID newIdentity = new DigitalID(uuid);

        repository.save(newIdentity);
        SecurityLogger.logCreation(uuid);
        return uuid;
    }

    public void statusUpdate(String uuid, IdentityStatus newStatus, Organisation requester) {
        DigitalID identity = repository.findByUuid(uuid).orElseThrow(() -> new IdentityNotFoundException(uuid));
        validateStatusUpdate(identity, requester, uuid);

        identity.setStatus(newStatus);
        identity.setStatusChangedAt(LocalDate.now());
        repository.save(identity);
        SecurityLogger.logStatusUpdate(uuid, newStatus);
    }

    private void validateStatusUpdate(DigitalID identity, Organisation requester, String uuid) {
        if (requester != Organisation.CENTRAL_AUTHORITY) {
            SecurityLogger.logUnauthorisedAttempt(uuid, requester.name(), "attempted to change ID status");
            throw new UnauthorisedAccessException(requester.name(), "status update");
        }

        if (identity.getStatus() == IdentityStatus.REVOKED) {
            SecurityLogger.logUnauthorisedAttempt(uuid, requester.name(), "update the status of a REVOKED ID");
            throw new IllegalStatusChangeException(identity.getStatus(), "Status Update");
        }
    }

    public void updateAttribute(String uuid, String key, String value, Organisation requester) {
        DigitalID identity = repository.findByUuid(uuid).orElseThrow(() -> new IdentityNotFoundException(uuid));
        validateAttributeUpdate(requester, identity, key);

        try {
            identity.setAttribute(key, value);
            repository.save(identity);
            SecurityLogger.logAttributeUpdate(uuid, key);
        } catch (ImmutableChangeException | IllegalStatusChangeException | AttributeDoesNotExistException | UnauthorisedAccessException e) {
            SecurityLogger.logUnauthorisedAttempt(uuid, requester.name(), " violate state rules " + e.getMessage());
            throw e;
        }
    }

    private void validateAttributeUpdate(Organisation requester, DigitalID identity, String key) {
        if (requester != Organisation.CENTRAL_AUTHORITY) throw new UnauthorisedAccessException(requester.name(), "Attribute Update");
        if (!Arrays.stream(optionalAttributes).toList().contains(key.toLowerCase()) && !key.equals("name") && !key.equals("date_of_birth")) throw new AttributeDoesNotExistException(key);
        if (identity.getStatus() == IdentityStatus.REVOKED) throw new IllegalStatusChangeException(identity.getStatus(), "Attribute Update");
    }
}
