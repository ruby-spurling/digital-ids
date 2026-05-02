package com.settp.id.core.repository;

import com.settp.id.core.model.DigitalID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class JsonIdentityRepositoryTest {
    private JsonIdentityRepository repository;
    private final String TEST_FILE = "identities.json";

    @BeforeEach
    void set_up() {
        cleanup();
        repository = new JsonIdentityRepository();
    }

    @AfterEach
    void teardown() {
        cleanup();
    }

    private void cleanup() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("Creates JSON file on first save")
    void testFileCreate() {
        DigitalID id = new DigitalID("aaaa-0000-0000");
        repository.save(id);

        File file = new File(TEST_FILE);
        assertTrue(file.exists(), "JSON file created upon saving");
    }

    @Test
    @DisplayName("Persists data between repository instances")
    void testPersistance() {
        DigitalID id = new DigitalID("persist-0000-0000");
        id.setAttribute("name", "Test");
        repository.save(id);

        JsonIdentityRepository secondRepository = new JsonIdentityRepository();

        var fetched = secondRepository.findByUuid("persist-0000-0000");
        assertTrue(fetched.isPresent());
        assertEquals("Test", fetched.get().getAttribute("name"));
    }

    @Test
    @DisplayName("Handles missing or empty files")
    void testEmptyFile() {
        JsonIdentityRepository emptyRepository = new JsonIdentityRepository();
        assertFalse(emptyRepository.findByUuid("some-id").isPresent());
    }
}
