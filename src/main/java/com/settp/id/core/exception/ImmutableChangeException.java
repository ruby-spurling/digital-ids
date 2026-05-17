package com.settp.id.core.exception;

public class ImmutableChangeException extends RuntimeException {
    public ImmutableChangeException(String attribute) {
      super("Can't change immutable attribute '" + attribute + "'");
    }
}