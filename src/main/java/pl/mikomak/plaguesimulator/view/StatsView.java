package pl.mikomak.plaguesimulator.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pl.mikomak.plaguesimulator.model.AgentState;
import pl.mikomak.plaguesimulator.model.Simulation;

import java.util.HashMap;

public class StatsView implements Renderable {
    private final HashMap<AgentState, Label> counterMap = new HashMap<>();
    private Simulation simulation;
    private HBox statsBar;
    private Label daysCounter;

    public StatsView(Simulation simulation) {
        this.simulation = simulation;
        this.statsBar = new HBox(10);
        this.daysCounter = new Label("0");

        VBox dayTile = createTile("Day", daysCounter, Color.WHITE);
        statsBar.getChildren().addFirst(dayTile);
        simulation.countInitialStats();

        for (AgentState state : AgentState.values()) {
            int value = simulation.getCount(state);
            Label label = new Label(String.valueOf(value));
            counterMap.put(state, label);
            statsBar.getChildren().add(createTile(state.name(), label, state.getColor()));
        }
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public void display() {
        daysCounter.setText(String.valueOf(simulation.getCurrentDay()));

        for (AgentState state : AgentState.values()) {
            Label label = counterMap.get(state);
            label.setText(String.valueOf(simulation.getCount(state)));
        }
    }

    private VBox createTile(String labelText, Label valueLabel, Color color) {
        Label title = new Label(labelText);
        title.setStyle("-fx-font-size: 11px; -fx-text-fill: #8892a4; -fx-font-family: sans-serif;");

        valueLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        valueLabel.setTextFill(color);

        VBox tile = new VBox(3, title, valueLabel);
        tile.setStyle("-fx-background-color: #1a2535; -fx-padding: 8 14 8 14; -fx-background-radius: 8;");
        tile.setPrefWidth(110);
        return tile;
    }

    public HBox getStatsBar() {
        return this.statsBar;
    }

}
