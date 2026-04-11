package com.settp.id.core.model;

import com.settp.id.core.exception.AttributeDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DigitalIDTest {
    private DigitalID digitalID;
    private final String testUUID = "1234-5678-9000";

    @BeforeEach
    void setup() {
        digitalID = new DigitalID(testUUID);
    }

    @Test
    @DisplayName("Initialise a Digital ID with correct UUID and ACTIVE status")
    void testInitialisation(){
        assertEquals(testUUID, digitalID.getUuid());
        assertEquals(IdentityStatus.ACTIVE, digitalID.getStatus());
        assertNotNull(digitalID.getCreatedAt());
    }

    @Test
    @DisplayName("Update status")
    void testStatusUpdate(){
        digitalID.setStatus(IdentityStatus.SUSPENDED);
        assertEquals(IdentityStatus.SUSPENDED, digitalID.getStatus());

        digitalID.setStatus(IdentityStatus.REVOKED);
        assertEquals(IdentityStatus.REVOKED, digitalID.getStatus());
    }

    @Test
    @DisplayName("Stores and retrieves attributes")
    void testAttributes(){
        digitalID.setAttribute("fullName", "Bob Smith");
        digitalID.setAttribute("dateOfBirth", "12-12-2000");

        assertEquals("Bob Smith", digitalID.getAttribute("fullName"));
        assertEquals("12-12-2000", digitalID.getAttribute("dateOfBirth"));
    }

    @Test
    @DisplayName("Returns exception when attribute does not exist.")
    void testAttributeDoesNotExist(){
        assertThrows(AttributeDoesNotExistException.class, () -> digitalID.getAttribute("nonExistentKey"));
    }
}
