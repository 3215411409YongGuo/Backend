package com.rabbiter.bms.exception;

/**
 * Inventory shortage anomaly
 * That is to say, the books have already been borrowed.
 */
public class NotEnoughException extends RuntimeException{
    public NotEnoughException() {
        super();
    }

    public NotEnoughException(String message) {
        super(message);
    }
}
