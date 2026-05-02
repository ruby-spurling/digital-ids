package com.settp.id.core.service;

import com.settp.id.core.exception.IdentityNotFoundException;
import com.settp.id.core.exception.IllegalStatusChangeException;
import com.settp.id.core.model.IdentityStatus;
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
        String uuid = service.createIdentity();

        assertNotNull(uuid);
        assertTrue(testRepo.exists(uuid));
    }

    @Test
    @DisplayName("Successfully update attribute of an active id")
    void testActiveIdAttributes() {
        String uuid = service.createIdentity();
        service.updateAttribute(uuid, "clearance", "Level 5");

        var id = testRepo.findByUuid(uuid).get();
        assertEquals("Level 5", id.getAttribute("clearance"));
    }

    @Test
    @DisplayName("Fail to update attribute of a revoked id")
    void testRevokedIdAttributes(){
        String uuid = service.createIdentity();
        service.statusUpdate(uuid, IdentityStatus.REVOKED);

        assertThrows(IllegalStatusChangeException.class, () -> {
            service.updateAttribute(uuid, "clearance", "Level 5");
        });
    }

    @Test
    @DisplayName("Successfully update status of an active id")
    void testActiveIdStatus() {
        String uuid = service.createIdentity();
        service.statusUpdate(uuid, IdentityStatus.SUSPENDED);

        var updatedId = testRepo.findByUuid(uuid).get();
        assertEquals(IdentityStatus.SUSPENDED, updatedId.getStatus());
    }

    @Test
    @DisplayName("Fails to update status of a revoked id")
    void testRevokedIdStatus() {
        String uuid = service.createIdentity();
        service.statusUpdate(uuid, IdentityStatus.REVOKED);

        assertThrows(IllegalStatusChangeException.class, () -> {
            service.statusUpdate(uuid, IdentityStatus.ACTIVE);
        });
    }

    @Test
    @DisplayName("Throws exception if id doesnt exist")
    void testUpdateNonExistentId() {
        assertThrows(IdentityNotFoundException.class, () -> {
            service.statusUpdate("fake-uuid", IdentityStatus.REVOKED);
        });
    }
}
