package ru.levelp.at.hw3.aircrafts.passenger;

public class Helicopter extends PassengerAircraft {

    private final HelicopterCategory category;

    public Helicopter(Double capacity, Integer maxSpeed, Integer maxRange, Integer seats, HelicopterCategory category) {
        super(capacity, maxSpeed, maxRange, seats);
        this.category = category;
    }

    public HelicopterCategory getCategory() {
        return category;
    }
}
