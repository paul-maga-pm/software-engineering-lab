package com.salesagents.business.administrator.services.exceptions;

import com.salesagents.exceptions.ExceptionBaseClass;

public class InvalidEntityException extends ExceptionBaseClass {
    public InvalidEntityException() {
    }

    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEntityException(Throwable cause) {
        super(cause);
    }

    public InvalidEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
