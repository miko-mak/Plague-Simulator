package pl.mikomak.plaguesimulator.model;

import javafx.scene.paint.Color;

public enum AgentState {
    UNAFFECTED(Color.rgb(57, 227, 39)),
    INFECTED(Color.rgb(214, 26, 19)),
    RECOVERED(Color.rgb(148, 0, 211)),
    VACCINATED(Color.rgb(30, 223, 230)),
    DEAD(Color.rgb(123, 142, 143));

    private final Color color;

    AgentState(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

}