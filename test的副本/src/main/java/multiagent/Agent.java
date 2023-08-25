package multiagent;
import java.util.List;
import java.util.ArrayList;

public abstract class Agent {
    private int id;
    private List<Agent> knownAgents = new ArrayList<>();

    public Agent(int id) {
        this.id = id;
    }

    public void sendMessageTo(Agent agent, String message, Task task) {
        agent.receiveMessage(this, message, task);
    }

    public abstract void receiveMessage(Agent sender, String message, Task task);

    public void addKnownAgent(Agent agent) {
        knownAgents.add(agent);
    }
    public List<Agent> getKnownAgents() {
        return knownAgents;
    }


    // Getters and setters...
}

