package pl.mikomak.plaguesimulator.model;


public class Agent {
    private AgentState state;
    private int daysInfected;
    private int immunityDays;
    private int x;
    private int y;

    public Agent(AgentState state, int daysInfected, int x, int y) {
        this.state = state;
        this.daysInfected = daysInfected;
        this.x = x;
        this.y = y;
        this.immunityDays = 5;
    }

    public AgentState getState() {
        return this.state;
    }

    public void setState(AgentState state) {
        this.state = state;
    }

    public int getDaysInfected() {
        return this.daysInfected;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void decreaseImmunity() {
        if (this.immunityDays > 0) {
            this.immunityDays -= 1;
        }
    }

    public int getImmunityDays() {
        return this.immunityDays;
    }

    public void incrementDays(int days) {
        this.daysInfected += days;
    }

}
