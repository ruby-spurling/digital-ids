package com.settp.id.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    public void setAttribute(String key, String value) { this.attributes.put(key, value); }

    public void setStatusChangedAt(LocalDate date) { this.statusChangedAt = date; }
}


