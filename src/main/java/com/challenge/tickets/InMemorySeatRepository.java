package com.challenge.tickets;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class InMemorySeatRepository implements SeatRepository {

    private final int totalNumSeats;
    private SeatSelectionStrategy strategy;
    private final Set<Seat> heldSeats = new ConcurrentSkipListSet<>();

    public InMemorySeatRepository(Venue venue, SeatSelectionStrategy strategy){
        this.strategy = strategy;
        List<Seat> seatsInOrder = venue.seatsInFavoredOrder();
        this.totalNumSeats = seatsInOrder.size();
        strategy.cacheSeats(seatsInOrder);
    }

    /**
     * Since we are only ever finding the best seats, there will be a lot of contention at the "head of the
     * "list of seats", so a pessimistic locking strategy seems like an appropriate initial approach
     * @param numSeats
     * @return
     */
    @Override
    public synchronized Collection<Seat> findAndHoldBestSeats(int numSeats) {
        Collection<Seat> nextBestSeats = strategy.selectAndRemoveNextBestSeats(numSeats, heldSeats);
        heldSeats.addAll(nextBestSeats);
        return nextBestSeats;
    }

    /**
     * It is ok if a removed hold is not immediately visible, so as long as the seat collection is eventually consistent
     * here, that should be ok
     * @param seats
     */
    @Override
    public void removeHold(Collection<Seat> seats) {
        heldSeats.removeAll(seats);
    }

    /**
     * Depending on the product requirements, may need to synchronize this and the remove method, but assuming this
     * number is more of an estimate, so need to synchronize. Note this is NOT a constant time operation due to the
     * set not having a constant time `size` method
     * @return
     */
    @Override
    public int numSeatsAvailable() {
        return totalNumSeats - heldSeats.size();
    }
}
