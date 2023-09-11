package multiagent2;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import java.util.Scanner;

public class UserAgent extends Agent {

    @Override
    protected void setup() {
        addBehaviour(new GetUserInputBehaviour());
    }

    private class GetUserInputBehaviour extends CyclicBehaviour {
        Scanner scanner = new Scanner(System.in);

        @Override
        public void action() {
            StringBuilder taskInfoBuilder = new StringBuilder();

            System.out.print("Please enter the length: ");
            long length = scanner.nextLong();
            taskInfoBuilder.append(length).append(",");

            System.out.print("Please enter the fileSize: ");
            long fileSize = scanner.nextLong();
            taskInfoBuilder.append(fileSize).append(",");

            System.out.print("Please enter the outputSize: ");
            long outputSize = scanner.nextLong();
            taskInfoBuilder.append(outputSize).append(",");

            System.out.print("Please enter the pesNumber: ");
            int pesNumber = scanner.nextInt();
            taskInfoBuilder.append(pesNumber);

            // Now the taskInfo string contains all the user input.
            String taskInfo = taskInfoBuilder.toString();

            // Send this information to CloudSimAgent.
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("CloudSimAgent", AID.ISLOCALNAME));
            msg.setContent("length:" + length + ",fileSize:" + fileSize + ",outputSize:" + outputSize + ",pesNumber:" + pesNumber);
            send(msg);
            
            // Send this information to TaskAgent.
            ACLMessage msgToTaskAgent = new ACLMessage(ACLMessage.INFORM);
            msgToTaskAgent.addReceiver(new AID("TaskAgent", AID.ISLOCALNAME));
            msgToTaskAgent.setContent(taskInfo);
            send(msgToTaskAgent);


            // Ask the user if they want to add more tasks
            System.out.println("Do you want to add another task? (yes/no)");
            String response = scanner.next().trim().toLowerCase();
            if (!response.equals("yes")) {
            	 ACLMessage simulationMsg = new ACLMessage(ACLMessage.INFORM);
            	 simulationMsg.addReceiver(new AID("CloudSimAgent", AID.ISLOCALNAME));
            	 simulationMsg.setContent("RUN_SIMULATION");
            	 send(simulationMsg);
                scanner.close();
                myAgent.doDelete();  // End the agent after input is done
            }
        }
    }
}

