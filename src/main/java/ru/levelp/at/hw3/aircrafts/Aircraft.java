package ru.levelp.at.hw3.aircrafts;

public abstract class Aircraft {

    private final Double capacity;
    private final Integer maxSpeed;
    private final Integer maxRange;

    public Aircraft(Double capacity, Integer maxSpeed, Integer maxRange) {
        this.capacity = capacity;
        this.maxSpeed = maxSpeed;
        this.maxRange = maxRange;
    }

    public Double getCapacity() {
        return capacity;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public Integer getMaxRange() {
        return maxRange;
    }
}
