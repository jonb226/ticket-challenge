package com.challenge.tickets;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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
        List<Seat> seats = ImmutableList.of(seat(), seat());
        when(seatRepository.findAndHoldBestSeats(anyInt())).thenReturn(seats);

        SeatHold hold = service.findAndHoldSeats(10, customerEmail);

        verify(seatHoldRepository, times(1)).insert(email, hold);
        assertThat(hold.getEmail()).isEqualTo(email);
        assertThat(hold.getSeats()).isEqualTo(seats);
    }

    private Seat seat(){
        return Seat.from("1A");
    }

}
