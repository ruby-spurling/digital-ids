package com.settp.id.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.settp.id.core.exception.ImmutableChangeException;
import com.settp.id.core.service.SecurityLogger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DigitalID {
    private final String uuid;
    private final LocalDateTime createdAt;

    private IdentityStatus status;
    @JsonProperty("attributes")
    private Map<String, String> attributes;
    private LocalDate statusChangedAt;

    @JsonCreator
    public DigitalID(@JsonProperty("uuid") String uuid) {
        this.uuid = uuid;
        this.createdAt = LocalDateTime.now();
        this.status = IdentityStatus.ACTIVE;
        this.attributes = new HashMap<>();
    }

    private static final Set<String> immutable_attributes = Set.of(
            "name",
            "date_of_birth",
            "ni_number"
    );

    public String getUuid() {
        return uuid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public IdentityStatus getStatus() {
        return status;
    }

    public String getAttribute(String key) { return this.attributes.get(key); }

    public Map<String, String> getAttributes() { return this.attributes; }

    public LocalDate getStatusChangedAt() {return statusChangedAt; }

    public void setStatus(IdentityStatus status) { this.status = status; }

    public void setAttribute(String key, String value) {
        if (immutable_attributes.contains(key.toLowerCase())) {
            String existingValue = this.attributes.get(key);
            if (existingValue != null && !existingValue.equals(value)) {
                SecurityLogger.logUnauthorisedAttempt(uuid, Organisation.CENTRAL_AUTHORITY.name(), "Attempted to change the immutable attribute: " + key);
                throw new ImmutableChangeException(key);
            }
        }
        this.attributes.put(key, value);
    }

    public void setStatusChangedAt(LocalDate date) { this.statusChangedAt = date; }
}


