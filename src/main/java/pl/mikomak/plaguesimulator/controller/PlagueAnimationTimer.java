package pl.mikomak.plaguesimulator.controller;

import javafx.animation.AnimationTimer;

public class PlagueAnimationTimer extends AnimationTimer {

    private final SimulationController simulationController;

    public PlagueAnimationTimer(SimulationController simulationController) {
        this.simulationController = simulationController;
    }

    @Override
    public void handle(long now) {
        simulationController.handleTimer(now);
    }

}
