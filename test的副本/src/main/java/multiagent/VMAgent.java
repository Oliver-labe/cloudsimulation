package multiagent;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class VMAgent extends Agent {
    @Override
    protected void setup() {
        addBehaviour(new jade.core.behaviours.CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    String content = msg.getContent();
                    if (AgentMessages.EXECUTE_TASK.equals(content)) {
                        System.out.println("VMAgent is executing the task...");
                        
                        // Notify BrokerAgent after task execution
                        ACLMessage brokerMessage = new ACLMessage(ACLMessage.INFORM);
                        brokerMessage.addReceiver(new AID("BrokerJadeAgent", AID.ISLOCALNAME));
                        brokerMessage.setContent(AgentMessages.TASK_COMPLETED);
                        send(brokerMessage);
                    }
                } else {
                    block();
                }
            }
        });
    }
}
