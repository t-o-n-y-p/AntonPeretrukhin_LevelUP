package ru.levelp.at.hw3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import ru.levelp.at.hw3.aircrafts.Aircraft;
import ru.levelp.at.hw3.aircrafts.passenger.PassengerAircraft;
import ru.levelp.at.hw3.aircrafts.passenger.PassengerPlane;

public class Airline {

    private final List<Aircraft> fleet;

    public Airline(Aircraft... fleet) {
        this.fleet = new ArrayList<>(Arrays.asList(fleet));
    }

    public boolean addAircraft(Aircraft aircraft) {
        return fleet.add(aircraft);
    }

    public Double getTotalCapacity() {
        return fleet.stream()
                    .mapToDouble(Aircraft::getCapacity)
                    .sum();
    }

    public Integer getTotalSeats() {
        return fleet.stream()
                    .filter(a -> a instanceof PassengerAircraft)
                    .mapToInt(a -> ((PassengerAircraft) a).getSeats())
                    .sum();
    }

    public List<PassengerPlane> getPassengerPlanes() {
        return fleet.stream().filter(a -> a instanceof PassengerPlane)
                    .map(p -> (PassengerPlane) p)
                    .collect(Collectors.toList());
    }

    public List<Aircraft> getFleetSortedByMaxRangeDesc() {
        return fleet.stream()
                    .sorted(Comparator.comparing(Aircraft::getMaxRange).reversed())
                    .collect(Collectors.toList());
    }

}
