package com.challenge.tickets;

public class OutOfSeatsException extends RuntimeException {
    public OutOfSeatsException(){
        super("Ran out of seats!");
    }
}
