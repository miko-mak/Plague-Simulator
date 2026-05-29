package pl.mikomak.plaguesimulator.model;

public interface Observable {
    void updateStatistic(AgentState state);
    int getCount(AgentState state);
}
