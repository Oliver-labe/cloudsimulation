package multiagent2;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class TaskAgent extends Agent {
    static int lastVMAgentContacted = 0; // Keep track of the last VMAgent contacted

    @Override
    protected void setup() {
        addBehaviour(new ReceiveTaskInfoBehaviour());
    }

    private class ReceiveTaskInfoBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                // Parse the task info from the message content
                String content = msg.getContent();
                String[] taskInfoArray = content.split(",");
                long length = Long.parseLong(taskInfoArray[0]);
                long fileSize = Long.parseLong(taskInfoArray[1]);
                long outputSize = Long.parseLong(taskInfoArray[2]);
                int pesNumber = Integer.parseInt(taskInfoArray[3]);

                // Create a new agent to manage this task
                try {
                    AgentController agentController = getContainerController().createNewAgent(
                            "TaskAgentInstance" + System.currentTimeMillis(), 
                            "multiagent2.TaskAgentInstance", 
                            new Object[]{length, fileSize, outputSize, pesNumber, lastVMAgentContacted});
                    agentController.start();

                    // Update the last VMAgent contacted for next agent
                    if (lastVMAgentContacted < 2) {
                        lastVMAgentContacted++;
                    } else {
                        lastVMAgentContacted = 0;
                    }
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            } else {
                block();
            }
        }
    }
}