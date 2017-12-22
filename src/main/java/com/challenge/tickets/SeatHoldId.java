package com.challenge.tickets;

public class SeatHoldId extends OpaqueId<Integer> {
    private SeatHoldId(Integer id) {
        super(id);
    }

    public static SeatHoldId from(int id) {
        return new SeatHoldId(id);
    }
}
