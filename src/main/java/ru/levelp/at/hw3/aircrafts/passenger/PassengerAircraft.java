package ru.levelp.at.hw3.aircrafts.passenger;

import ru.levelp.at.hw3.aircrafts.Aircraft;

public abstract class PassengerAircraft extends Aircraft {

    private final Integer seats;

    public PassengerAircraft(Double capacity, Integer maxSpeed, Integer maxRange, Integer seats) {
        super(capacity, maxSpeed, maxRange);
        this.seats = seats;
    }

    public Integer getSeats() {
        return seats;
    }
}
