package com.example.demo;

import java.util.List;

public class CarHandler {
    private final Car car;
    private final List<Lights> lights;

    public CarHandler(Car car) {
        this.car = car;
        this.lights = List.of(car.getHalfLights(), car.getHighBeam(),
                car.getBackLights(), car.getBrakeLights(), car.getWarningLights());
    }

    public void setSpeed(int speed) {
        if (speed >= 0 && speed <= 180) {
            car.setSpeed(speed);
        } else {
            throw new IllegalArgumentException("Must be between 0 and 180!");
        }
    }

    public void changeSpeed(int change) {
        if (car.getBattery().getBatteryLevel() > 0) {
            car.setSpeed(car.getSpeed() + change);
        }
    }

    public void setGasOn() {
        if (car.getBattery().getBatteryLevel() > 0) {
            car.setGasOn();
        }
    }

    public void startEngine() {
        car.setIsRunningTrue();
        car.getHalfLights().putLightsOn();
    }

    public void drive(int distance) {
        if (!car.getIsRunning()) {
            startEngine();
        }
        if (car.getBattery().getBatteryLevel() >= distance) {
            car.getBattery().setBatteryLevel(car.getBattery().getBatteryLevel() - distance);
            shine();
        } else {
            car.getWarningLights().putLightsOn();
            deadBattery();
            throw new IllegalArgumentException("Car can't run on empty battery");
        }
    }

    public void deadBattery() {
        car.getBattery().setEmergencyActivated(true);
        for (Lights l : lights) {
            if (!l.equals(car.getWarningLights())) {
                l.putLightsOff();
            }
        }
    }

    public void shine() {
        for (Lights l : lights) {
            if (l.isLightsOn()) {
                if (l.equals(car.getWarningLights()) && car.getBattery().isEmergencyActivated()) {
                    car.getBattery().setEmergencyBatteryLevel(car.getBattery().getEmergencyBatteryLevel() - 1);
                } else {
                    car.getBattery().setBatteryLevel(car.getBattery().getBatteryLevel() - 1);
                }
            }
        }
    }

    public void stopEngine() {
        car.setIsRunningFalse();
        car.getHalfLights().putLightsOff();
        car.getHighBeam().putLightsOff();
        car.getBackLights().putLightsOff();
    }

    public void brake() {
        car.setBrakeOn();
        car.setGasOff();
        car.setSpeed(0);
        car.getBrakeLights().putLightsOn();
    }

    public void charger(int amount){
        if(car.isConnect()==true){

            //System.out.println();
            if(car.getBattery().getBatteryLevel() < 100){
                car.getBattery().chargeBattery(amount);
            }else if(car.getBattery().getBatteryLevel() == 100){
                throw new RuntimeException("Battery is already charged");
            }
        }else {
            throw new RuntimeException("The car is not connected");
        }
    }

    public Car getCar() {
        return car;
    }
}
