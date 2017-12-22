package com.challenge.tickets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class InMemorySeatRepositoryTest {

    InMemorySeatRepository repo;

    @Mock
    Venue venue;

    SeatSelectionStrategy strategy;

    class TestStrategy implements SeatSelectionStrategy {
        private List<Seat> allSeats;

        @Override
        public void cacheSeats(List<Seat> favoredSeats) {
            this.allSeats = favoredSeats;
        }

        @Override
        public Collection<Seat> selectAndRemoveNextBestSeats(int numSeats, Set<Seat> takenSeats) {
            ArrayList<Seat> remainingSeats = Lists.newArrayList(allSeats);
            remainingSeats.removeAll(takenSeats);
            return remainingSeats.subList(0, numSeats);
        }
    }

    @Before
    public void setup() {
        initMocks(this);
        List<Seat> allSeats = ImmutableList.of(Seat.from("1A"), Seat.from("2B"), Seat.from("2C"), Seat.from("3A"));
        when(venue.seatsInFavoredOrder()).thenReturn(allSeats);
        strategy = new TestStrategy();
        repo = new InMemorySeatRepository(venue, strategy);
    }

    @Test
    public void canFindAndHoldTheBestSeats() {
        Collection<Seat> bestSeats = repo.findAndHoldBestSeats(1);
        Collection<Seat> nextBestSeats = repo.findAndHoldBestSeats(1);
        assertThat(nextBestSeats).isNotEqualTo(bestSeats);
    }

    @Test
    public void canRemoveAHold() {
        Collection<Seat> bestSeats = repo.findAndHoldBestSeats(1);
        repo.removeHold(bestSeats);
        Collection<Seat> nextBestSeats = repo.findAndHoldBestSeats(1);
        assertThat(nextBestSeats).isEqualTo(bestSeats);
    }

    @Test
    public void canGetNumSeatsAvailable() {
        assertThat(repo.numSeatsAvailable()).isEqualTo(4);
        Collection<Seat> seats = repo.findAndHoldBestSeats(3);
        assertThat(repo.numSeatsAvailable()).isEqualTo(1);
        repo.removeHold(Lists.newArrayList(seats).subList(0, 1));
        assertThat(repo.numSeatsAvailable()).isEqualTo(2);
    }

}
