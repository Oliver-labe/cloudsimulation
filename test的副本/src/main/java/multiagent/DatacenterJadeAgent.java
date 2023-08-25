package multiagent;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class DatacenterJadeAgent extends Agent {
    
    private int mips = 1000;  // Sample value
    private int ram = 4096;   // in MB, Sample value
    private int storage = 10000;  // in MB, Sample value
    
    @Override
    protected void setup() {
        addBehaviour(new jade.core.behaviours.CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    String content = msg.getContent();
                    
                    if (AgentMessages.QUERY_RESOURCES.equals(content)) {
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent("MIPS:" + mips + ",RAM:" + ram + ",STORAGE:" + storage);
                        send(reply);
                    }
                    
                    else if (AgentMessages.SUBMIT_TASK.equals(content)) {
                        // Check if resources are sufficient, else send back a failure or look for another VMAgent
                        // For simplicity, assuming that the task requires 100 MIPS, 100MB RAM, and 100MB storage
                        if(mips >= 100 && ram >= 100 && storage >= 100) {
                            mips -= 100;
                            ram -= 100;
                            storage -= 100;
                            
                            // Send the task to VMAgent for execution
                            ACLMessage vmMessage = new ACLMessage(ACLMessage.INFORM);
                            vmMessage.addReceiver(new AID("VMAgent", AID.ISLOCALNAME));
                            vmMessage.setContent(AgentMessages.EXECUTE_TASK);
                            send(vmMessage);
                        } else {
                            // Handle insufficient resources, maybe send back a refusal to `BrokerAgent`
                            ACLMessage refuseMessage = msg.createReply();
                            refuseMessage.setPerformative(ACLMessage.REFUSE);
                            refuseMessage.setContent(AgentMessages.INSUFFICIENT_RESOURCES);
                            send(refuseMessage);
                        }
                    }
                } else {
                    block();
                }
            }
        });
    }
}

