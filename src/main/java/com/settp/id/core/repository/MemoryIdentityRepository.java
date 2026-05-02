package com.settp.id.core.repository;

import com.settp.id.core.model.DigitalID;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryIdentityRepository implements IdentityRepository{
    private final Map<String, DigitalID> storage = new HashMap<>();

    @Override
    public void save(DigitalID id) {
        storage.put(id.getUuid(), id);
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
    public Iterable<DigitalID> findAll() {
        return storage.values();
    }
}
