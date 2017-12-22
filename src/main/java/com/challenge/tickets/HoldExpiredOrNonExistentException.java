package com.challenge.tickets;

public class HoldExpiredOrNonExistentException extends RuntimeException {
    public HoldExpiredOrNonExistentException(SeatHoldId id){
        super("hold for the given id expired or never existed id=" + id);
    }
}
