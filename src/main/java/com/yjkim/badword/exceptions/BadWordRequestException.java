package com.yjkim.badword.exceptions;

import java.io.Serial;

public class BadWordRequestException extends BadWordRuntimeException
{
    @Serial
    private static final long serialVersionUID = -5916340390557743173L;

    public BadWordRequestException (String message)
    {
        super(message);
    }

    public BadWordRequestException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public BadWordRequestException (Throwable cause)
    {
        super(cause);
    }

    protected BadWordRequestException (String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
