package pl.mikomak.plaguesimulator.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import pl.mikomak.plaguesimulator.model.Grid;
import pl.mikomak.plaguesimulator.model.Simulation;
import pl.mikomak.plaguesimulator.view.GridView;
import pl.mikomak.plaguesimulator.view.StatsView;

public class SimulationController {
    private final AnimationTimer animationTimer;
    private Grid grid;

    private Simulation simulation;
    private final GridView gridView;
    private final StatsView statsView;
    private long lastStepTime;

    public SimulationController(Canvas canvas, Grid grid, StatsView statsView, Simulation simulation) {
        this.grid = grid;
        this.simulation = simulation;
        this.gridView = new GridView(canvas, grid);
        this.statsView = statsView;
        this.lastStepTime = System.nanoTime();
        this.animationTimer = new PlagueAnimationTimer(this);
    }

    public void start() {
        animationTimer.start();
    }

    public void reset() {
        animationTimer.stop();
    }

    public void reset(Grid grid, Simulation simulation) {
        animationTimer.stop();
        this.grid = grid;
        this.simulation = simulation;
        this.gridView.setGrid(grid);
        this.statsView.setSimulation(simulation);
    }

    public GridView getGridView() {
        return this.gridView;
    }

    public StatsView getStatsView() {
        return this.statsView;
    }

    public void handleTimer(long now) {
        this.gridView.display();

        if (now - this.lastStepTime >= 10_000_000_000L) {
            this.simulation.step();
            this.statsView.display();
            this.simulation.increaseDays();
            this.lastStepTime = now;
        }
    }

}
