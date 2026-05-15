package com.settp.id.core.exception;

import com.settp.id.core.model.IdentityStatus;

public class ImmutableChangeException extends RuntimeException {
    public ImmutableChangeException(String attribute) {
      super("Can't change immutable attribute '" + attribute + "'");
    }
}