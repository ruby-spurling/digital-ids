package com.settp.id.core.service;

import com.settp.id.core.exception.IdentityNotFoundException;
import com.settp.id.core.model.DigitalID;
import com.settp.id.core.model.IdentityStatus;
import com.settp.id.core.model.Organisation;
import com.settp.id.core.repository.MemoryIdentityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OtherAuthorityTest {
    private MemoryIdentityRepository repository;
    private OtherAuthority otherAuthority;
    private DigitalID id;
    private String uuid;

    @BeforeEach
    void setUp() {
        repository = new MemoryIdentityRepository();
        otherAuthority = new OtherAuthority(repository);
        uuid = "test1234";
        id = new DigitalID(uuid);
        repository.save(id);
    }

    @Test
    @DisplayName("Throws exception when uuid is not in repository")
    void testIdentityNotFound() {
        assertThrows(IdentityNotFoundException.class, () -> otherAuthority.verifyIdentity("doesNotExist", Organisation.EMPLOYER));
    }

    @Test
    @DisplayName("Bank must only receive status validity info")
    void testBankMinimisation() {
        id.setStatus(IdentityStatus.ACTIVE);
        id.setAttribute("name", "Test");
        id.setAttribute("driving_penalty_points", "0");

        Map<String, String> result = otherAuthority.verifyIdentity(uuid, Organisation.BANK);

        assertEquals("ACTIVE", result.get("Status"));
        assertEquals("true", result.get("ID valid?"));

        assertNull(result.get("name"), "Bank should not access name");
        assertNull(result.get("driving_penalty_points"), "Bank should not access driving penalty points");
    }

    @Test
    @DisplayName("Authorities should safely fetch attributes even when they have no value")
    void testAttributeFetch() {
        id.setAttribute("right_to_work", "Verified");

        Map<String, String> result = otherAuthority.verifyIdentity(uuid, Organisation.EMPLOYER);

        assertEquals("Verified", result.get("right_to_work"));
        assertEquals("Not set", result.get("over_18"));
    }

    @Test
    @DisplayName("Tax compliance shows warning when status is not active for this year")
    void testTaxCompliance() {
        id.setStatus(IdentityStatus.SUSPENDED);

        Map<String, String> result = otherAuthority.verifyIdentity(uuid, Organisation.TAX_SERVICE);

        assertTrue(result.get("Tax Compliance").contains("WARNING"));
    }
}
