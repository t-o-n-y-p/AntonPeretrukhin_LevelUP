package ru.levelp.at.hw4.task1;

import java.time.Duration;
import ru.levelp.at.hw3.aircrafts.Drone;
import ru.levelp.at.hw4.task1.exceptions.AircraftLoadException;
import ru.levelp.at.hw4.task1.exceptions.AircraftParamsException;
import ru.levelp.at.hw4.task1.exceptions.AircraftStateException;

public class AirlineApplication {

    public static void main(String[] args) {
        AirlineApplication airlineApplication = new AirlineApplication();
        airlineApplication.start();
    }

    public void start() {
        try {
            Drone drone = new Drone(-2.5, 100, 5, Duration.ofHours(8));
        } catch (AircraftParamsException e) {
            e.printStackTrace();
        }
        Drone drone = new Drone(2.5, 100, 5, Duration.ofHours(8));
        try {
            drone.startFlight();
            drone.load(2.0);
        } catch (AircraftStateException e) {
            e.printStackTrace();
        }
        try {
            drone.endFlight();
            drone.load(2.0);
            drone.load(2.0);
        } catch (AircraftLoadException e) {
            e.printStackTrace();
        }

    }

}
