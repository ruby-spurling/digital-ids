package com.settp.id.core.exception;

import java.io.IOException;

public class DataLoadException extends DataAccessException{
    public DataLoadException(String filename, Throwable cause) {
        super("Could not load data from: " + filename, cause);
    }
}
