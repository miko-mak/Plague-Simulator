package pl.mikomak.plaguesimulator.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import pl.mikomak.plaguesimulator.model.Agent;
import pl.mikomak.plaguesimulator.model.Grid;

public class GridView implements Renderable{
    public static final int CELL_SIZE = 10;

    private final Canvas canvas;
    private Grid grid;

    public GridView(Canvas canvas, Grid grid) {
        this.canvas = canvas;
        this.grid = grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    @Override
    public void display() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        int gridWidth = grid.getWidth();
        int gridHeight = grid.getHeight();
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                Agent agent = grid.getAgentAtCoordinates(x, y);
                if (agent == null) {
                    continue;
                }

                graphicsContext.setFill(agent.getState().getColor());
                graphicsContext.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
            }
        }
    }

}
