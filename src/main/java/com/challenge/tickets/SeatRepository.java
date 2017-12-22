package com.challenge.tickets;

import java.util.Collection;

public interface SeatRepository {
    Collection<Seat> findAndHoldBestSeats(int numSeats);
    void removeHold(Collection<Seat> hold);
    int numSeatsAvailable();
}
