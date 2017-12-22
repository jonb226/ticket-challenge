package com.challenge.tickets;

import java.util.Collection;

public interface SeatHoldRepository {
    void insert(Email email, SeatHold hold);
    void removeExpiration(Collection<SeatHoldId> id);
    Collection<SeatHold> getExpiredHolds();
    void delete(SeatHoldId id);
}
