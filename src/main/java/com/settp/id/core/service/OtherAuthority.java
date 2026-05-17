package com.settp.id.core.service;

import com.settp.id.core.exception.IdentityNotFoundException;
import com.settp.id.core.model.DigitalID;
import com.settp.id.core.model.IdentityStatus;
import com.settp.id.core.model.Organisation;
import com.settp.id.core.repository.IdentityRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
                String dobStr = identity.getAttribute("date_of_birth");
                try {
                    LocalDate dob = LocalDate.parse(dobStr);
                    LocalDate today = LocalDate.now();
                    int age = Period.between(dob, today).getYears();
                    permittedData.put("over_18", Boolean.toString(age >= 18));
                } catch (Exception e) {
                    permittedData.put("over_18", "Unknown DOB format.");
                }
                break;

            case TAX_SERVICE:
                safeFetch(permittedData, identity, "residency_status");
                safeFetch(permittedData, identity, "ni_number");
                checkTaxReportingPeriod(permittedData, identity, Year.now());
                break;

            case DRIVING_LICENSE_AUTHORITY:
                safeFetch(permittedData, identity, "driving_restriction");
                safeFetch(permittedData, identity, "driving_license_category");
                safeFetch(permittedData, identity, "driving_penalty_points");

                String restriction = identity.getAttribute("driving_restriction");
                if (restriction != null) {
                    if (restriction.toLowerCase().contains("temp") || restriction.matches(".*\\d.*")) {
                        permittedData.put("Driving Eligibility", "Temporary restriction in place");
                    } else {
                        permittedData.put("Driving Eligibility", "Permanent restriction in place");
                    }
                } else {
                    permittedData.put("Driving Eligibility", "No restrictions");
                }
                break;

            case BANK:
                boolean isValid = identity.getStatus() == IdentityStatus.ACTIVE;
                permittedData.put("ID valid?", String.valueOf(isValid));
                break;

            case CENTRAL_AUTHORITY:
                permittedData.put("Admin Access", "FULL RECORD");

                Map<String, String> allAttributes = identity.getAttributes();
                if (allAttributes != null) {
                    permittedData.putAll(allAttributes);
                }
                break;
        }
        return permittedData;
    }

    public boolean statusValidityCheck(String uuid) {
        DigitalID id = repository.findByUuid(uuid).orElseThrow(() -> new IdentityNotFoundException(uuid));
        return id.getStatus() == IdentityStatus.ACTIVE;
    }

    private void safeFetch(Map<String, String> permittedData, DigitalID identity, String key) {
        String value = identity.getAttribute(key);
        permittedData.put(key, Objects.requireNonNullElse(value, "Not set"));
    }

    private void checkTaxReportingPeriod(Map<String, String> data, DigitalID identity, Year currentYear) {
        IdentityStatus status = identity.getStatus();

        if ((status == IdentityStatus.SUSPENDED) | status == IdentityStatus.REVOKED) {
            LocalDate changeDate = identity.getStatusChangedAt();
            if (changeDate != null && changeDate.getYear() == currentYear.getValue()) {
                data.put("Tax Compliance", "WARNING: ID was " + status + " on " + changeDate + ". Audit required for " + currentYear);
            } else {
                data.put("Tax Compliance", "WARNING: ID was " + status + ". Audit required for " + currentYear);
            }
        } else {
            data.put("Tax Compliance", "No status conflicts found for " + currentYear);
        }
    }
}
