package pl.mikomak.plaguesimulator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.mikomak.plaguesimulator.controller.SimulationController;
import pl.mikomak.plaguesimulator.model.Grid;
import pl.mikomak.plaguesimulator.model.Simulation;
import pl.mikomak.plaguesimulator.view.StatsView;

import java.io.InputStream;

import static pl.mikomak.plaguesimulator.view.GridView.CELL_SIZE;

public class PlagueApplication extends Application {

    private SimulationController controller;

    @Override
    public void start(Stage stage) {
        BorderPane borderPane = new BorderPane();
        Canvas canvas = new Canvas(600, 600);
        borderPane.setCenter(canvas);

        int width = (int) canvas.getWidth() / CELL_SIZE;
        int height = (int) canvas.getHeight() / CELL_SIZE;

        VBox sidePanel = new VBox(10);
        sidePanel.setPadding(new Insets(10));

        Slider occupancySlider = new Slider(0.1, 1, 0.6);
        double occupancyRate = occupancySlider.getValue();
        Label occupancyLabel = new Label("Occupancy rate: " + (int) (occupancyRate * 100) + "%");

        Slider infectionSlider = new Slider(0, 1, 0.3);
        double infectionRate = infectionSlider.getValue();
        Label infectionLabel = new Label("Infection chance: " + (int) (infectionRate * 100) + "%");

        Slider vaccinationSlider = new Slider(0, 1, 0.05);
        double vaccinationRate = vaccinationSlider.getValue();
        Label vaccinationLabel = new Label("Vaccination rate " + (int) (vaccinationRate * 100) + "%");

        Slider recoverySlider = new Slider(1, 30, 7);
        int recoveryTimeInDays = (int) recoverySlider.getValue();
        String unit = recoverySlider.getValue() > 1 ? " days" : " day";
        Label recoveryLabel = new Label("Recovery time: " + recoveryTimeInDays + unit);

        Slider deathSlider = new Slider(0, 1, 0.1);
        double deathChance = deathSlider.getValue();
        Label deathLabel = new Label("Death chance: " + (int) (deathChance * 100) + "%");

        occupancySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            int value = (int) (newValue.doubleValue() * 100);
            occupancyLabel.setText("Occupancy rate: " + value + "%");
        });

        infectionSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            int value = (int) (newValue.doubleValue() * 100);
            infectionLabel.setText("Infection chance: " + value + "%");
        });

        vaccinationSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            int value = (int) (newValue.doubleValue() * 100);
            vaccinationLabel.setText("Vaccination rate: " + value + "%");
        });

        deathSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            int value = (int) (newValue.doubleValue() * 100);
            deathLabel.setText("Death chance: " + value + "%");
        });

        recoverySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            int value = (int) newValue.doubleValue();
            String newUnit = value > 1 ? " days" : " day";
            recoveryLabel.setText("Recovery time: " + value + newUnit);
        });

        Button startButton = new Button("Start");
        Button resetButton = new Button("Reset");

        startButton.setStyle("-fx-font-size: 14px; -fx-min-width: 150px; -fx-min-height: 40px;");
        resetButton.setStyle("-fx-font-size: 14px; -fx-min-width: 150px; -fx-min-height: 40px;");
        resetButton.setDisable(true);

        recoverySlider.setMinWidth(200);
        occupancySlider.setMinWidth(200);
        infectionSlider.setMinWidth(200);
        vaccinationSlider.setMinWidth(200);

        infectionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        occupancyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        recoveryLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        deathLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        vaccinationLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        sidePanel.getChildren().addAll(
                occupancyLabel, occupancySlider,
                infectionLabel, infectionSlider,
                vaccinationLabel, vaccinationSlider,
                recoveryLabel, recoverySlider,
                deathLabel, deathSlider,
                startButton, resetButton
        );

        borderPane.setRight(sidePanel);

        Scene scene = new Scene(borderPane, 900, 600);
        borderPane.setBackground(new Background(new BackgroundFill(Color.rgb(26, 32, 48), null, null)));

        InputStream iconStream = getClass().getResourceAsStream("icon.png");
        if (iconStream != null) {
            stage.getIcons().add(new Image(iconStream));
        }

        stage.setTitle("Plague Simulator");
        stage.setScene(scene);
        stage.show();

        startButton.setOnAction(e -> {
            double currentOccupancy = occupancySlider.getValue();
            double currentInfection = infectionSlider.getValue();
            double currentVaccination = vaccinationSlider.getValue();
            int currentRecovery = (int) recoverySlider.getValue();
            double currentDeath = deathSlider.getValue();

            Grid grid = new Grid(width, height, currentOccupancy, currentVaccination);
            Simulation simulation = new Simulation(grid, currentInfection, currentRecovery, currentDeath);
            StatsView statsView = new StatsView(simulation);

            borderPane.setTop(statsView.getStatsBar());
            statsView.getStatsBar().setPadding(new Insets(8, 12, 8, 12));
            statsView.getStatsBar().setBackground(new Background(new BackgroundFill(Color.rgb(33, 40, 64), null, null)));

            infectionSlider.setDisable(true);
            recoverySlider.setDisable(true);
            occupancySlider.setDisable(true);
            vaccinationSlider.setDisable(true);
            recoverySlider.setDisable(true);
            deathSlider.setDisable(true);

            startButton.setDisable(true);
            resetButton.setDisable(false);

            this.controller = new SimulationController(canvas, grid, statsView, simulation);
            this.controller.start();
        });

        resetButton.setOnAction(e -> {
            this.controller.reset();

            borderPane.setTop(null);
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            resetButton.setDisable(true);
            startButton.setDisable(false);

            infectionSlider.setDisable(false);
            recoverySlider.setDisable(false);
            occupancySlider.setDisable(false);
            vaccinationSlider.setDisable(false);
            deathSlider.setDisable(false);
        });
    }

}

