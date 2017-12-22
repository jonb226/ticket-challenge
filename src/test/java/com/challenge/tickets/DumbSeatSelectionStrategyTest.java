package com.challenge.tickets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class DumbSeatSelectionStrategyTest {

    DumbSeatSelectionStrategy strategy;

    @Before
    public void setup(){
        strategy = new DumbSeatSelectionStrategy();
        strategy.cacheSeats(ImmutableList.of(Seat.from("1A"), Seat.from("1B"), Seat.from("1C"), Seat.from("1D"), Seat.from("1E")));
    }

    @Test(expected = OutOfSeatsException.class)
    public void throwsAnExceptionWhenOutOfSeats(){
        strategy.selectAndRemoveNextBestSeats(Integer.MAX_VALUE, Sets.newHashSet());
    }

    @Test
    public void selectsSomeSeats(){
        Collection<Seat> seats = strategy.selectAndRemoveNextBestSeats(3, Sets.newHashSet());
        assertThat(seats).containsExactly(Seat.from("1A"), Seat.from("1B"), Seat.from("1C"));
    }

    @Test
    public void doesntSelectTakenSeats(){
        Collection<Seat> seats = strategy.selectAndRemoveNextBestSeats(3, ImmutableSet.of(Seat.from("1B")));
        assertThat(seats).containsExactly(Seat.from("1A"), Seat.from("1C"), Seat.from("1D"));
    }

}
