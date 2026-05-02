package com.settp.id.core.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.settp.id.core.exception.DataLoadException;
import com.settp.id.core.exception.DataSaveException;
import com.settp.id.core.model.DigitalID;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonIdentityRepository implements IdentityRepository{
    private final String filePath = "identities.json";
    private final ObjectMapper objectMapper;
    private Map<String, DigitalID> storage = new HashMap<>();

    public JsonIdentityRepository(){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) return;

        try { storage = objectMapper.readValue(file, new TypeReference<Map<String, DigitalID>>() {});}
        catch (IOException e) {throw new DataLoadException(filePath, e);}
    }

    private void saveToFile() {
        try {objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), storage);}
        catch (IOException e) {throw new DataSaveException(filePath, e);}
    }

    @Override
    public void save(DigitalID identity) {
        storage.put(identity.getUuid(), identity);
        saveToFile();
    }

    @Override
    public Optional<DigitalID> findByUuid(String uuid) {
        return Optional.ofNullable(storage.get(uuid));
    }

    @Override
    public boolean exists(String uuid) {
        return storage.containsKey(uuid);
    }

    @Override
    public Iterable<DigitalID> findAll(){
        return storage.values();
    }
}
