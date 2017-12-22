package com.challenge.tickets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;
import static java.util.concurrent.CompletableFuture.runAsync;
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
    private final int MAX_SEATS_IN_VENUE = 400;
    private final String EMAIL = "test@test.com";

    @Before
    public void waitForAllHoldsToExpire(){
        eventually(() -> assertThat(service.numSeatsAvailable()).isEqualTo(MAX_SEATS_IN_VENUE));
    }

    @Test
    public void testFindingSeats() {
        List<CompletableFuture> holdPromises = IntStream.range(0, NUM_REQUESTS)
                .mapToObj((i) -> supplyAsync(() -> service.findAndHoldSeats(SEATS_PER_REQUEST, EMAIL)))
                .collect(toList());
        holdPromises.forEach((p) -> p.join());
        List<SeatHold> holds = holdPromises.stream().map(p -> getSeatHold(p)).collect(toList());
        // Push the results into a set to auto-remove duplicates
        Set<Seat> seats = holds.stream().flatMap(h -> h.getSeats().stream()).collect(toSet());
        assertThat(seats).hasSize(NUM_REQUESTS * SEATS_PER_REQUEST);
        assertThat(seats).contains(Seat.from("1A"), Seat.from("2F"));
        // assuming the num seats requirement is it is eventually consistent
        eventually(() -> assertThat(service.numSeatsAvailable()).isEqualTo(MAX_SEATS_IN_VENUE - NUM_REQUESTS * SEATS_PER_REQUEST));
    }

    @Test
    public void testExpiringHolds() {
        IntStream.range(0, NUM_REQUESTS).forEach((i) -> runAsync(() -> service.findAndHoldSeats(SEATS_PER_REQUEST, EMAIL)));
        eventually(() -> assertThat(service.numSeatsAvailable()).isLessThan(MAX_SEATS_IN_VENUE));
        eventually(() -> assertThat(service.numSeatsAvailable()).isEqualTo(MAX_SEATS_IN_VENUE));
    }

    @Test
    @DirtiesContext
    public void testReservedSeatsDontExpire() throws InterruptedException {
        IntStream.range(0, NUM_REQUESTS).forEach((i) -> runAsync(() -> {
            SeatHold hold = service.findAndHoldSeats(SEATS_PER_REQUEST, EMAIL);
            service.reserveSeats(hold.getId().value(), EMAIL);
        }));
        eventually(() -> assertThat(service.numSeatsAvailable()).isLessThan(MAX_SEATS_IN_VENUE));
        sleep(10000);
        assertThat(service.numSeatsAvailable()).isEqualTo(MAX_SEATS_IN_VENUE - NUM_REQUESTS * SEATS_PER_REQUEST);
    }

    enum Actions {
        HOLD,
        HOLD_AND_RESERVE
    }

    @Test
    @DirtiesContext
    public void stressTest(){
        // TODO
    }

    private SeatHold getSeatHold(CompletableFuture p) {
        try {
            return (SeatHold) p.get();
        } catch (Exception e) {
            logger.error("Exception when collecting results.", e);
            throw new RuntimeException("Exception when collecting results.", e);
        }
    }

    private void eventually(Callable o){
        AssertionError lastException = null;
        for(int i=0 ; i<200 ; i++){
            try {
                o.call();
                return;
            }catch(AssertionError e){
                lastException = e;
                try {
                    sleep(10);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw lastException;
    }

}
