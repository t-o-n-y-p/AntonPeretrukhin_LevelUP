package ru.levelp.at.hw3.aircrafts;

import java.time.Duration;

public class Drone extends Aircraft {

    private final Duration rechargeTime;

    public Drone(Double capacity, Integer maxSpeed, Integer maxRange, Duration rechargeTime) {
        super(capacity, maxSpeed, maxRange);
        this.rechargeTime = rechargeTime;
    }

    public Duration getRechargeTime() {
        return rechargeTime;
    }
}
