package com.challenge.tickets;

import com.google.common.collect.ImmutableList;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Service
public class StandardTicketService implements TicketService {

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
        SeatHold hold = SeatHold.from(seats, email, 1);
        seatHoldRepository.insert(email, hold);
        return hold;
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        seatHoldRepository.removeExpiration(ImmutableList.of(SeatHoldId.from(seatHoldId)));
        return seatHoldId + "";
    }

    @Scheduled(fixedDelay = 10000)
    public void removeExpiredHolds(){
        Collection<SeatHold> holds = seatHoldRepository.getExpiredHolds();
        seatRepository.removeHold(holds.stream().flatMap(h -> h.getSeats().stream()).collect(toList()));
        seatHoldRepository.removeExpiration(holds.stream().map(h -> h.getId()).collect(toList()));
    }
}
