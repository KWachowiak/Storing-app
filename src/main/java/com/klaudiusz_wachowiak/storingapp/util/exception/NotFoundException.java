package com.klaudiusz_wachowiak.storingapp.util.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException() {
        super("ERROR - Content not found");
    }
}
