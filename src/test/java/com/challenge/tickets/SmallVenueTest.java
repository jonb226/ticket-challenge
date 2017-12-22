package com.challenge.tickets;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SmallVenueTest {

    SmallVenue venue;

    @Before
    public void setup(){
        venue = new SmallVenue();
    }

    @Test
    public void hasSeats(){
        assertThat(venue.seatsInFavoredOrder()).hasSize(400);
        assertThat(venue.seatsInFavoredOrder().get(0)).isEqualTo(Seat.from("1A"));
        assertThat(venue.seatsInFavoredOrder().get(199)).isEqualTo(Seat.from("10T"));
    }

}
