package com.settp.id.core.exception;

import java.util.Map;

public class AttributeDoesNotExistException extends RuntimeException{
    public AttributeDoesNotExistException(String key) {
        super("Attribute '" + key + "' could not be found.");
    }
}
