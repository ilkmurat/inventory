package com.murat.oneamz.inventory.exceptions;

public class InvalidProcessException extends RuntimeException{

    public InvalidProcessException() {
        super("The transaction was not processed");
    }
}
