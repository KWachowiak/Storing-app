package com.klaudiusz_wachowiak.storingapp.util.exception;

public class DataBaseDuplicateException extends RuntimeException {
    public DataBaseDuplicateException() {
        super("ERROR - Entity is already in database - change url address or name");
    }
}
