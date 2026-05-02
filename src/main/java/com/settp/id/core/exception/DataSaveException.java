package com.settp.id.core.exception;

public class DataSaveException extends DataAccessException{
    public DataSaveException(String fileName, Throwable cause) {
        super("Failed to persist data to: " + fileName, cause);
    }
}
