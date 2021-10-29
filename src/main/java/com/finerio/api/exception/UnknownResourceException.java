package com.finerio.api.exception;


public class UnknownResourceException extends RuntimeException {

    private static final long serialVersionUID = 8330860863595220574L;

    /**
     * @param msg
     */
    public UnknownResourceException(String msg) {
        super(msg);
    }
    
    /**
     * @param mensaje
     * @param exception
     */
    public UnknownResourceException(String mensaje, Throwable exception) {
        super(mensaje);
    }
}