package com.klaudiusz_wachowiak.storingapp.util.exception;

public class NoConnectionException extends RuntimeException {
    public NoConnectionException() {
        super("ERROR - Could not connect to the resource");
    }
}
