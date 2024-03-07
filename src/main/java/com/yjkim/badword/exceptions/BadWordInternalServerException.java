package com.yjkim.badword.exceptions;

import java.io.Serial;

public class BadWordInternalServerException extends BadWordRuntimeException
{
    @Serial
    private static final long serialVersionUID = 2793860120222657278L;

    public BadWordInternalServerException (String message)
    {
        super(message);
    }

    public BadWordInternalServerException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public BadWordInternalServerException (Throwable cause)
    {
        super(cause);
    }

    protected BadWordInternalServerException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
