package com.yjkim.badword.exceptions;

import java.io.Serial;

public class BadWordNotFoundException extends BadWordRuntimeException
{
    @Serial
    private static final long serialVersionUID = -7692604466290737502L;

    public BadWordNotFoundException (String message)
    {
        super(message);
    }

    public BadWordNotFoundException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public BadWordNotFoundException (Throwable cause)
    {
        super(cause);
    }

    protected BadWordNotFoundException (String message, Throwable cause,
                                        boolean enableSuppression,
                                        boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
