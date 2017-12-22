package com.challenge.tickets;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The definitive state of seats being claimed or not exists in a SeatRepository, so everything here just needs to be
 * eventually consistent, so can just use the semantics in the ConcurrentHashMap
 */
@Repository
public class InMemorySeatHoldRepository implements SeatHoldRepository {

    private static class CompleteHoldInfo {
        private final SeatHold seatHold;
        private final Email email;

        public CompleteHoldInfo(SeatHold seatHold, Email email){
            this.seatHold = seatHold;
            this.email = email;
        }
    }

    private final ConcurrentHashMap<SeatHoldId, CompleteHoldInfo> holds = new ConcurrentHashMap<>();

    @Override
    public void insert(Email email, SeatHold hold) {
        holds.put(hold.getId(), new CompleteHoldInfo(hold, email));
    }

    @Override
    public void removeExpiration(Collection<SeatHoldId> ids) {
        ids.forEach((id) -> holds.get(id).seatHold.removeExpiration());
    }

    @Override
    public Collection<SeatHold> getExpiredHolds() {
        Collection<SeatHold> expired = Lists.newArrayList();
        holds.forEach((k, v) -> {
            if(v.seatHold.isExpired()){
                expired.add(v.seatHold);
            }
        });
        return expired;
    }

    @Override
    public void delete(Collection<SeatHoldId> ids) {
        ids.forEach((id) -> holds.remove(id));
    }
}
