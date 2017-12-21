package com.challenge.tickets;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class StandardTicketServiceTest {

    StandardTicketService service;

    @Mock
    SeatHoldRepository seatHoldRepository;

    @Mock
    SeatRepository seatRepository;

    @Before
    public void setup(){
        initMocks(this);
        service = new StandardTicketService(seatRepository, seatHoldRepository);
    }

    @Test
    public void canGetNumAvailableSeats(){
        when(seatRepository.numSeatsAvailable()).thenReturn(10);
        assertThat(service.numSeatsAvailable()).isEqualTo(10);
    }

    @Test
    public void canFindAndHoldSeats(){
        String customerEmail = "test-hold@test.com";
        Email email = Email.from(customerEmail);
        List<Seat> seats = ImmutableList.of(Seat.from("1A"), Seat.from("1B"));
        when(seatRepository.findAndHoldBestSeats(anyInt())).thenReturn(seats);

        SeatHold hold = service.findAndHoldSeats(10, customerEmail);

        verify(seatHoldRepository, times(1)).insert(email, hold);
        assertThat(hold.getEmail()).isEqualTo(email);
        assertThat(hold.getSeats()).isEqualTo(seats);
    }

    @Test
    public void canReserveSeats(){
        int seatHoldId = 1;
        String reservationCode = service.reserveSeats(seatHoldId, "test-reserve@test.com");
        verify(seatHoldRepository, times(1)).removeExpiration(ImmutableList.of(SeatHoldId.from(seatHoldId)));
        assertThat(reservationCode).isNotBlank();
    }

    @Test
    public void canRemoveExpiredHolds(){
        Collection<Seat> seats = ImmutableList.of(Seat.from("1A"));
        SeatHold hold = SeatHold.from(seats, Email.from("test-expire@test.com"));
        when(seatHoldRepository.getExpiredHolds()).thenReturn(ImmutableList.of(hold));
        service.removeExpiredHolds();
        verify(seatRepository, times(1)).removeHold(seats);
        verify(seatHoldRepository, times(1)).removeExpiration(ImmutableList.of(hold.getId()));
    }

}
