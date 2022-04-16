package com.salesagents.business.administrator.services.exceptions;

import com.salesagents.exceptions.ExceptionBaseClass;

public class InvalidAgentException extends ExceptionBaseClass {
    public InvalidAgentException() {
    }

    public InvalidAgentException(String message) {
        super(message);
    }

    public InvalidAgentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAgentException(Throwable cause) {
        super(cause);
    }

    public InvalidAgentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
