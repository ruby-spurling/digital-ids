package com.settp.id.core.exception;

public class AttributeDoesNotExistException extends RuntimeException{
    public AttributeDoesNotExistException(String key) {
        super("Attribute '" + key + "' could not be found.");
    }
}
