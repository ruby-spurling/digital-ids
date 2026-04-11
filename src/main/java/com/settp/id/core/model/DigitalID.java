package com.settp.id.core.model;

import com.settp.id.core.exception.AttributeDoesNotExistException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DigitalID {
    private final String uuid;
    private final LocalDateTime createdAt;

    private IdentityStatus status;
    private final Map<String, String> attributes;

    public DigitalID(String uuid) {
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

    public String getAttribute(String key) {
        if (attributes.containsKey(key)) {
            return attributes.get(key);
        } else {
            throw new AttributeDoesNotExistException(key);
        }
    }

    public void setStatus(IdentityStatus status) {
        this.status = status;
    }

    public void setAttribute(String key, String value) {
        this.attributes.put(key, value);
    }
}


