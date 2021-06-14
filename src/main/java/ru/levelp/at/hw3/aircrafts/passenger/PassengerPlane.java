package ru.levelp.at.hw3.aircrafts.passenger;

public class PassengerPlane extends PassengerAircraft {

    private final Integer engines;

    public PassengerPlane(Double capacity, Integer maxSpeed, Integer maxRange, Integer seats, Integer engines) {
        super(capacity, maxSpeed, maxRange, seats);
        this.engines = engines;
    }

    public Integer getEngines() {
        return engines;
    }
}
