package com.challenge.tickets;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StandardTicketService implements TicketService{

    private SeatRepository seatRepository;
    private SeatHoldRepository seatHoldRepository;

    public StandardTicketService(SeatRepository seatRepository, SeatHoldRepository seatHoldRepository){
        this.seatRepository = seatRepository;
        this.seatHoldRepository = seatHoldRepository;
    }

    @Override
    public int numSeatsAvailable() {
        return seatRepository.numSeatsAvailable();
    }

    @Override
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        Collection<Seat> seats = seatRepository.findAndHoldBestSeats(numSeats);
        Email email = Email.from(customerEmail);
        SeatHold hold = SeatHold.from(seats, email);
        seatHoldRepository.insert(email, hold);
        return hold;
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        return null;
    }
}
