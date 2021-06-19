package ru.levelp.at.hw3.aircrafts;

import ru.levelp.at.hw4.task1.exceptions.AircraftLoadException;
import ru.levelp.at.hw4.task1.exceptions.AircraftParamsException;
import ru.levelp.at.hw4.task1.exceptions.AircraftStateException;

public abstract class Aircraft {

    private final Double capacity;
    private final Integer maxSpeed;
    private final Integer maxRange;
    private Double load;
    protected boolean flightInProgress;

    public Aircraft(Double capacity, Integer maxSpeed, Integer maxRange) {
        if (capacity <= 0 || maxSpeed <= 0 || maxRange <= 0) {
            throw new AircraftParamsException("Aircraft params must be positive.");
        }
        this.capacity = capacity;
        this.maxSpeed = maxSpeed;
        this.maxRange = maxRange;
        this.load = 0.0;
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

    public Double getLoad() {
        return load;
    }

    public boolean isFlightInProgress() {
        return flightInProgress;
    }

    public void startFlight() {
        flightInProgress = true;
    }

    public void endFlight() {
        flightInProgress = false;
    }

    public void load(Double weight) {
        if (flightInProgress) {
            throw new AircraftStateException("Aircraft is in flight; can't load.");
        }
        if (capacity - load < weight) {
            throw new AircraftLoadException("No room for the load with the given weight.");
        }

        load += weight;
    }

    public void unload(Double weight) {
        if (flightInProgress) {
            throw new AircraftStateException("Aircraft is in flight; can't unload.");
        }
        if (load < weight) {
            throw new AircraftLoadException("Current load is less than the given weight.");
        }

        load -= weight;
    }


}
