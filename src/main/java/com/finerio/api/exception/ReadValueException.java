package com.finerio.api.exception;

public class ReadValueException  extends RuntimeException {

	
	  
	/**
 * 
 */
private static final long serialVersionUID = -1311019714084831927L;

	/**
     * @param msg
     */
    public ReadValueException(String msg) {
        super(msg);
    }
    
    /**
     * @param mensaje
     * @param exception
     */
    public ReadValueException(String mensaje, Throwable exception) {
        super(mensaje);
    }


}
