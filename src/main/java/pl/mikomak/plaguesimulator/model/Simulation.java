package pl.mikomak.plaguesimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Simulation implements Observable {
    private final HashMap<AgentState, Integer> stateMap = new HashMap<>();
    private final Grid grid;
    private int currentDay;
    private double infectionChance;
    private int recoveryTimeInDays;
    private double deathChance;

    public Simulation(Grid grid, double infectionChance, int recoveryTimeInDays, double deathChance) {
        for (AgentState state : AgentState.values()) {
            stateMap.put(state, 0);
        }

        this.grid = grid;
        this.currentDay = 0;
        this.infectionChance = infectionChance;
        this.recoveryTimeInDays = recoveryTimeInDays;
        this.deathChance = deathChance;
    }

    public int getCurrentDay() {
        return this.currentDay;
    }

    public void increaseDays() {
        int currentDay = this.currentDay;
        this.currentDay = currentDay +1;
    }

    public void step() {
        final Random random = new Random();
        final List<Agent> agents = new ArrayList<>();
        int width = this.grid.getWidth();
        int height = this.grid.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Agent agent = this.grid.getAgentAtCoordinates(x, y);
                if (agent == null) {
                    continue;
                }

                agents.add(agent);

                if (agent.getState() == AgentState.RECOVERED) {
                    agent.decreaseImmunity();
                }

                if (agent.getImmunityDays() == 0) {
                    agent.setState(AgentState.UNAFFECTED);
                }

                if (agent.getState() == AgentState.INFECTED) {
                    final List<Agent> neighbours = this.grid.getNeighbours(x, y);
                    agent.incrementDays(1);

                    if (agent.getDaysInfected() > 1) {
                        boolean shouldDie = random.nextDouble() < this.deathChance;
                        if (shouldDie) agent.setState(AgentState.DEAD);
                    }

                    if (agent.getDaysInfected() >= this.recoveryTimeInDays && agent.getState() != AgentState.DEAD) {
                        agent.setState(AgentState.RECOVERED);
                    }

                    for (Agent neighbour : neighbours) {
                        if (neighbour.getState() != AgentState.UNAFFECTED) {
                            continue;
                        }

                        if (random.nextDouble() < this.infectionChance) {
                            neighbour.setState(AgentState.INFECTED);
                        }
                    }
                }
            }
        }

        stateMap.replaceAll((state, count) -> 0);
        for (Agent agent : agents) {
            this.updateStatistic(agent.getState());
        }
    }

    @Override
    public void updateStatistic(AgentState state) {
        int currentCount = stateMap.get(state);
        stateMap.put(state, currentCount + 1);
    }

    @Override
    public int getCount(AgentState state) {
        return stateMap.get(state);
    }

    public void countInitialStats() {
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Agent agent = grid.getAgentAtCoordinates(x, y);
                if (agent != null) {
                    updateStatistic(agent.getState());
                }
            }
        }
    }

}