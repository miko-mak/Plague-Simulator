package pl.mikomak.plaguesimulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {
    private final Agent[][] grid;
    private final double occupancyRate;
    private final int width;
    private final int height;
    private final double vaccinationRate;

    public Grid(int width, int height, double occupancyRate, double vaccinationRate) {
        this.grid = new Agent[width][height];
        this.occupancyRate = occupancyRate;
        this.width = width;
        this.height = height;
        this.vaccinationRate = vaccinationRate;
        initialize();
    }

    private void initialize() {
        final List<Agent> unaffected = new ArrayList<>();
        final Random random = new Random();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (random.nextDouble() >= this.occupancyRate) {
                    grid[x][y] = null;
                    continue;
                }

                AgentState state = AgentState.UNAFFECTED;
                if (random.nextDouble() < this.vaccinationRate) {
                    state = AgentState.VACCINATED;
                }

                Agent agent = new Agent(state, 0, x, y);
                grid[x][y] = agent;

                if (state == AgentState.UNAFFECTED) {
                    unaffected.add(agent);
                }
            }
        }

        if (!unaffected.isEmpty()) {
            int randomIndex = random.nextInt(unaffected.size());
            Agent patientZero = unaffected.get(randomIndex);
            patientZero.setState(AgentState.INFECTED);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Agent getAgentAtCoordinates(int x, int y) {
        return grid[x][y];
    }

    public List<Agent> getNeighbours(int x, int y) {
        return getNeighbours(x, y, 1);
    }

    private List<Agent> getNeighbours(int x, int y, int radius) {
        List<Agent> neighbours = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }

                int nx = x + dx;
                int ny = y + dy;

                if (nx < 0 || nx >= width) {
                    continue;
                }

                if (ny < 0 || ny >= height) {
                    continue;
                }

                if (grid[nx][ny] != null) {
                    neighbours.add(grid[nx][ny]);
                }
            }
        }

        if (neighbours.isEmpty() && radius < 3) {
            return getNeighbours(x, y, radius + 1);
        }

        return neighbours;
    }

}
