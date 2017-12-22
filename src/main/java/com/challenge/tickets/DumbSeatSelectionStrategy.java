package com.challenge.tickets;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * cacheSeats must be called before any selection takes place. selectAndRemoveNextBestSeats is only thread-safe within
 * itself, and cannot be called concurrently with cacheSeats
 */
public class DumbSeatSelectionStrategy implements SeatSelectionStrategy {
    private final List<Seat> allSeats = new ArrayList<>();

    @Override
    public void cacheSeats(List<Seat> favoredSeats) {
        this.allSeats.addAll(favoredSeats);
    }

    @Override
    public Collection<Seat> selectAndRemoveNextBestSeats(int numSeats, Set<Seat> takenSeats) {
        ArrayList<Seat> remainingSeats = Lists.newArrayList(allSeats);
        remainingSeats.removeAll(takenSeats);
        return remainingSeats.subList(0, numSeats);
    }
}
