package com.settp.id.core.service;

import com.settp.id.core.exception.IdentityNotFoundException;
import com.settp.id.core.exception.IllegalStatusChangeException;
import com.settp.id.core.model.IdentityStatus;
import com.settp.id.core.model.Organisation;
import com.settp.id.core.repository.IdentityRepository;
import com.settp.id.core.repository.MemoryIdentityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CentralAuthorityTest {
    private IdentityRepository testRepo;
    private CentralAuthority service;

    @BeforeEach
    void set_up() {
        testRepo = new MemoryIdentityRepository();
        service = new CentralAuthority(testRepo);
    }

    @Test
    @DisplayName("Create and save new identity")
    void testCreateIdentity() {
        String uuid = service.createIdentity(Organisation.CENTRAL_AUTHORITY);

        assertNotNull(uuid);
        assertTrue(testRepo.exists(uuid));
    }

    @Test
    @DisplayName("Successfully update attribute of an active id")
    void testActiveIdAttributes() {
        String uuid = service.createIdentity(Organisation.CENTRAL_AUTHORITY);
        service.updateAttribute(uuid, "driving_penalty_points", "5", Organisation.CENTRAL_AUTHORITY);

        var id = testRepo.findByUuid(uuid).get();
        assertEquals("5", id.getAttribute("driving_penalty_points"));
    }

    @Test
    @DisplayName("Fail to update attribute of a revoked id")
    void testRevokedIdAttributes(){
        String uuid = service.createIdentity(Organisation.CENTRAL_AUTHORITY);
        service.statusUpdate(uuid, IdentityStatus.REVOKED, Organisation.CENTRAL_AUTHORITY);

        assertThrows(IllegalStatusChangeException.class, () -> {
            service.updateAttribute(uuid, "driving_penalty_points", "5", Organisation.CENTRAL_AUTHORITY);
        });
    }

    @Test
    @DisplayName("Successfully update status of an active id")
    void testActiveIdStatus() {
        String uuid = service.createIdentity(Organisation.CENTRAL_AUTHORITY);
        service.statusUpdate(uuid, IdentityStatus.SUSPENDED, Organisation.CENTRAL_AUTHORITY);

        var updatedId = testRepo.findByUuid(uuid).get();
        assertEquals(IdentityStatus.SUSPENDED, updatedId.getStatus());
    }

    @Test
    @DisplayName("Fails to update status of a revoked id")
    void testRevokedIdStatus() {
        String uuid = service.createIdentity(Organisation.CENTRAL_AUTHORITY);
        service.statusUpdate(uuid, IdentityStatus.REVOKED, Organisation.CENTRAL_AUTHORITY);

        assertThrows(IllegalStatusChangeException.class, () -> {
            service.statusUpdate(uuid, IdentityStatus.ACTIVE, Organisation.CENTRAL_AUTHORITY);
        });
    }

    @Test
    @DisplayName("Throws exception if id doesnt exist")
    void testUpdateNonExistentId() {
        assertThrows(IdentityNotFoundException.class, () -> {
            service.statusUpdate("fake-uuid", IdentityStatus.REVOKED, Organisation.CENTRAL_AUTHORITY);
        });
    }
}
