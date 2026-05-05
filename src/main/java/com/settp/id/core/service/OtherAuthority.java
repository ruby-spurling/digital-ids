package com.settp.id.core.service;

import com.settp.id.core.exception.IdentityNotFoundException;
import com.settp.id.core.model.DigitalID;
import com.settp.id.core.model.Organisation;
import com.settp.id.core.repository.IdentityRepository;

import java.util.HashMap;
import java.util.Map;

public class OtherAuthority {
    private final IdentityRepository repository;

    public OtherAuthority(IdentityRepository repository) {
        this.repository = repository;
    }

    public Map<String, String> verifyIdentity(String uuid, Organisation requester) {
        DigitalID identity = repository.findByUuid(uuid).orElseThrow(() -> new IdentityNotFoundException(uuid));
        SecurityLogger.logAccess(uuid, requester.name());

        Map<String, String> permittedData = new HashMap<>();
        permittedData.put("Status", identity.getStatus().name());

        switch (requester) {
            case EMPLOYER:
                safeFetch(permittedData, identity, "right_to_work");
                safeFetch(permittedData, identity, "over_18");
                break;

            case TAX_SERVICE:
                safeFetch(permittedData, identity, "residency_status");
                safeFetch(permittedData, identity, "ni_number");
                break;

            case DRIVING_LICENSE_AUTHORITY:
                safeFetch(permittedData, identity, "driving_restriction");
                safeFetch(permittedData, identity, "driving_license_category");
                safeFetch(permittedData, identity, "driving_penalty_points");
                break;

            case CENTRAL_AUTHORITY:
                permittedData.put("SYSTEM MESSAGE", "Use central authority service for management");
                break;
        }
        return permittedData;
    }

    private void safeFetch(Map<String, String> permittedData, DigitalID identity, String key) {
        String value = identity.getAttribute(key);
        if (value != null) {
            permittedData.put(key, value);
        } else {
            permittedData.put(key, "Not set");
        }
    }
}
