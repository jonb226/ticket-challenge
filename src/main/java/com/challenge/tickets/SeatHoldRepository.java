package com.challenge.tickets;

public interface SeatHoldRepository {
    void insert(Email email, SeatHold hold);
    void removeExpiration(SeatHoldId id);
}
