package com.settp.id.core.exception;

public class UnauthorisedAccessException extends RuntimeException{
    public UnauthorisedAccessException(String organisation, String action) {
        super("Organisation '" + organisation + "' is not authorised to perform: " + action);
    }
}
