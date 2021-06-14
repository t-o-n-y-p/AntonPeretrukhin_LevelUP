package ru.levelp.at.hw3;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import ru.levelp.at.hw3.aircrafts.Drone;
import ru.levelp.at.hw3.aircrafts.passenger.Helicopter;
import ru.levelp.at.hw3.aircrafts.passenger.HelicopterCategory;
import ru.levelp.at.hw3.aircrafts.passenger.PassengerPlane;

public class AirlineApplication {

    public static void main(String[] args) {
        AirlineApplication airlineApplication = new AirlineApplication();
        airlineApplication.start();
    }

    public void start() {
        Airline airline = new Airline(
            new Drone(0.1, 100, 5, Duration.ofHours(8)),
            new Drone(0.25, 80, 7, Duration.ofHours(5)),
            new Helicopter(50.0, 250, 500, 5, HelicopterCategory.B),
            new Helicopter(75.0, 240, 400, 9, HelicopterCategory.A),
            new PassengerPlane(300.0, 900, 5000, 200, 4),
            new PassengerPlane(400.0, 1200, 8000, 250, 2)
        );
        airline.addAircraft(
            new PassengerPlane(600.0, 1100, 12000, 350, 3)
        );
        System.out.println("Общая грузоподъемность: " + airline.getTotalCapacity());
        System.out.println("Общая вместимость: " + airline.getTotalSeats());

        System.out.println("Дальность полетов: ");
        airline.getFleetSortedByMaxRangeDesc()
               .forEach(a -> System.out.println(a.getMaxRange()));
        System.out.println();

        List<PassengerPlane> planes = airline.getPassengerPlanes().stream()
                .filter(p -> p.getMaxSpeed() >= 1000 && p.getEngines() >= 3)
                .collect(Collectors.toList());
        System.out.println("Самолетов по фильтру: " + planes.size());
    }

}
