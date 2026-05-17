package com.settp.id.core.exception;
import com.settp.id.core.model.IdentityStatus;

public class IllegalStatusChangeException extends RuntimeException{
    public IllegalStatusChangeException(IdentityStatus current, String action) {
        super("Can't perform '" + action + "' because identity is currently " + current);
    }
}
