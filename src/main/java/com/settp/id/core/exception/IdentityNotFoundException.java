package com.settp.id.core.exception;

public class IdentityNotFoundException extends RuntimeException{
    public IdentityNotFoundException(String uuid) {
        super("Identity with UUID " + uuid + "could not be found.");
    }
}
