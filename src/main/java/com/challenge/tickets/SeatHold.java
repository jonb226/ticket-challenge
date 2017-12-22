package com.challenge.tickets;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Random;

public class SeatHold {
    private SeatHoldId id;
    private Collection<Seat> seats;
    private Email email;
    private LocalDateTime expiration;

    private SeatHold(int id, Collection<Seat> seats, Email email, int ttl) {
        this.id = SeatHoldId.from(id);
        this.seats = seats;
        this.email = email;
        this.expiration = LocalDateTime.now().plusSeconds(ttl);
    }

    public static SeatHold from(Collection<Seat> seats, Email email, int ttl) {
        int id = new Random().nextInt();
        return new SeatHold(id, seats, email, ttl);
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

    public void removeExpiration() {
        this.expiration = LocalDateTime.MAX;
    }

    public boolean isExpired(){
        return expiration.isBefore(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "SeatHold{" +
                "seats=" + seats +
                ", email=" + email +
                ", expiration=" + expiration +
                '}';
    }
}
