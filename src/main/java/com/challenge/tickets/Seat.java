package com.challenge.tickets;

public class Seat extends OpaqueStringId{
    private Seat(String id) {
        super(id);
    }

    public static Seat from(String seatNum){
        return new Seat(seatNum);
    }
}
