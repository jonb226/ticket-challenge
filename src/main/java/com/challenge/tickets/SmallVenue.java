package com.challenge.tickets;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class SmallVenue implements Venue{

    private final ArrayList<Seat> seats;

    public SmallVenue(){
        this.seats = Lists.newArrayList();
        IntStream.range(1, 21).forEach((i) ->
                IntStream.range(0, 10).forEach((a) ->
                        seats.add(Seat.from("" + i + ((char)('A' + a))))
                )
        );
    }

    @Override
    public List<Seat> seatsInFavoredOrder() {
        return seats;
    }
}
