package com.challenge.tickets;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SeatSelectionStrategy {
    void cacheSeats(List<Seat> favoredSeats);
    Collection<Seat> selectAndRemoveNextBestSeats(int numSeats, Set<Seat> takenSeats);
}
