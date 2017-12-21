package com.challenge.tickets;

import java.util.Collection;
import java.util.Random;

public class SeatHold {
    private SeatHoldId id;
    private Collection<Seat> seats;
    private Email email;

    private SeatHold(int id, Collection<Seat> seats, Email email) {
        this.id = SeatHoldId.from(id);
        this.seats = seats;
        this.email = email;
    }

    public static SeatHold from(Collection<Seat> seats, Email email) {
        int id = new Random().nextInt();
        return new SeatHold(id, seats, email);
    }

    public SeatHoldId getId() {
        return id;
    }

    public Collection<Seat> getSeats() {
        return seats;
    }

    public Email getEmail() {
        return email;
    }
}
