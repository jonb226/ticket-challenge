package com.challenge.tickets;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SeatTest {

    @Test
    public void deduplicatesInASet(){
        Set<Seat> set = ImmutableSet.of(Seat.from("1A"), Seat.from("1A"));
        assertThat(set).hasSize(1);
    }

}
