package com.challenge.tickets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(TicketServiceTest.class);

    @Autowired
    TicketService service;

    private final int SEATS_PER_REQUEST = 3;
    private final int NUM_REQUESTS = 100;

    @Test
    public void testFindingSeats() {
        String email = "test@test.com";
        List<CompletableFuture> holdPromises = IntStream.range(0, NUM_REQUESTS)
                .mapToObj((i) -> supplyAsync(() -> service.findAndHoldSeats(SEATS_PER_REQUEST, email)))
                .collect(toList());
        holdPromises.forEach((p) -> p.join());
        List<SeatHold> holds = holdPromises.stream().map(p -> getSeatHold(p)).collect(toList());
        // Push the results into a set to auto-remove duplicates
        Set<Seat> seats = holds.stream().flatMap(h -> h.getSeats().stream()).collect(toSet());
        assertThat(seats).hasSize(NUM_REQUESTS * SEATS_PER_REQUEST);
        assertThat(seats).contains(Seat.from("1A"), Seat.from("2F"));
    }

    private SeatHold getSeatHold(CompletableFuture p) {
        try {
            return (SeatHold) p.get();
        } catch (Exception e) {
            logger.error("Exception when collecting results.", e);
            throw new RuntimeException("Exception when collecting results.", e);
        }
    }

}
