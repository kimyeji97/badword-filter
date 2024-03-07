package com.yjkim.badword.exceptions;

import java.io.Serial;

public class BadWordRuntimeException extends RuntimeException
{
    @Serial
    private static final long serialVersionUID = 1165216106959214911L;

    public BadWordRuntimeException (String message)
    {
        super(message);
    }

    public BadWordRuntimeException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public BadWordRuntimeException (Throwable cause)
    {
        super(cause);
    }

    protected BadWordRuntimeException (String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
