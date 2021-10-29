package com.finerio.api.exception;


public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 8330860863595220574L;
    /**
     * @param msg
     */
    public BadRequestException(String msg) {        
        super(msg);
    }
    
    /**
     * @param mensaje
     * @param exception
     */
    public BadRequestException(String mensaje, Throwable exception) {
        super(mensaje);
    }

}
