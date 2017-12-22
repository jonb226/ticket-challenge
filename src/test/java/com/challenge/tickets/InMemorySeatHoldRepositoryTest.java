package com.challenge.tickets;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemorySeatHoldRepositoryTest {

    InMemorySeatHoldRepository repo;
    Email email;
    List<Seat> seats;
    SeatHold expiredHold;

    @Before
    public void setup(){
        repo = new InMemorySeatHoldRepository();
        email = Email.from("test-remove@test.com");
        seats = ImmutableList.of(Seat.from("1A"));
        expiredHold = SeatHold.from(seats, email, -1);
        repo.insert(email, expiredHold);
    }

    @Test
    public void canInsertAndGetExpiredHolds(){
        SeatHold nonExpiredHold = SeatHold.from(seats, email, Integer.MAX_VALUE);
        repo.insert(email, nonExpiredHold);
        assertThat(repo.getExpiredHolds()).containsExactly(expiredHold);
    }

    @Test
    public void canRemoveExpiration(){
        assertThat(repo.getExpiredHolds()).containsExactly(expiredHold);
        repo.removeExpiration(ImmutableList.of(expiredHold.getId()));
        assertThat(repo.getExpiredHolds()).isEmpty();
    }

    @Test
    public void canDeleteAHold(){
        assertThat(repo.getExpiredHolds()).containsExactly(expiredHold);
        repo.delete(ImmutableList.of(expiredHold.getId()));
        assertThat(repo.getExpiredHolds()).isEmpty();
    }

    @Test(expected = HoldExpiredOrNonExistentException.class)
    public void canHandleExpiredNonExistentHolds(){
        repo.removeExpiration(ImmutableList.of(SeatHoldId.from(1000)));
    }

}
