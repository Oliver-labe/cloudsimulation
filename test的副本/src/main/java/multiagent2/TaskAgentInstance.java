package multiagent2;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class TaskAgentInstance extends Agent {
    long length;
    long fileSize;
    long outputSize;
    int pesNumber;
    static int lastVMAgentContacted = 0; // Keep track of the last VMAgent contacted

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 5) {
            length = (long) args[0];
            fileSize = (long) args[1];
            outputSize = (long) args[2];
            pesNumber = (int) args[3];
            lastVMAgentContacted = (int) args[4];

            // Print the task info to demonstrate that this agent is working correctly
            System.out.println("TaskAgentInstance created with the following task info:");
            System.out.println("length: " + length);
            System.out.println("fileSize: " + fileSize);
            System.out.println("outputSize: " + outputSize);
            System.out.println("pesNumber: " + pesNumber);

            // Add behaviour to send the task info to a VMAgent
            addBehaviour(new SendTaskInfoBehaviour());
        }
    }

    private class SendTaskInfoBehaviour extends OneShotBehaviour {
        @Override
        public void action() {
            // Create a new message to send the task info to a VMAgent
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new jade.core.AID("VMAgent", jade.core.AID.ISLOCALNAME));
            msg.setContent(length + "," + pesNumber);

            
            // Send the message to the next VMAgent in a round-robin fashion
            if(lastVMAgentContacted < 2) {
                lastVMAgentContacted++;
            } else {
                lastVMAgentContacted = 0;
            }
            msg.addReceiver(new jade.core.AID("VMAgent" + lastVMAgentContacted, jade.core.AID.ISLOCALNAME));

            // Send the message
            send(msg);
        }
    }
}
