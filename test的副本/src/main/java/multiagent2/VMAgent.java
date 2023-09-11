package multiagent2;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

public class VMAgent extends Agent {
    
    // VM attributes
    private final int[] mips = {1000, 1000, 1000};
    private final long[] size = {10000, 10000, 10000};
    private final int[] ram = {512, 512, 512};
    private final long[] bw = {1000, 1000, 1000};
    private final int[] pesNumber = {1, 1, 1};
    private final boolean[] vmAvailability = {true, true, true};

    // VMAgent initialization
    @Override
    protected void setup() {
        addBehaviour(new ReceiveTaskInfoBehaviour());
    }

    // Behaviour to receive and process task info from TaskAgent
    private class ReceiveTaskInfoBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                try {
                    // Parse the task info from the message content
                    String content = msg.getContent();
                    String[] taskInfoArray = content.split(",");
                    long taskLength = Long.parseLong(taskInfoArray[0]);
                    int taskPesNumber = Integer.parseInt(taskInfoArray[1]);

                    // Find the first available VM to process the task and calculate the time to complete
                    for (int i = 0; i < mips.length; i++) {
                        if (vmAvailability[i] && pesNumber[i] >= taskPesNumber) {
                        	long timeToComplete = (long)((taskLength / (double) mips[i]) * 1000); // Converting time to milliseconds
// Converting time to milliseconds
                            System.out.println("VM " + i + " will complete the task in " + timeToComplete + " milliseconds.");

                            // Set VM to unavailable and add a behaviour to make it available after the task is complete
                            vmAvailability[i] = false;
                            addBehaviour(new MakeVMAvailableAgainBehaviour(i, (long) timeToComplete));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                block();
            }
        }
    }

    // Behaviour to make a VM available again after a specified amount of time
    private class MakeVMAvailableAgainBehaviour extends WakerBehaviour {
        private int vmIndex;

        public MakeVMAvailableAgainBehaviour(int vmIndex, long timeout) {
            super(VMAgent.this, timeout); // timeout is now in milliseconds
            this.vmIndex = vmIndex;
        }

        @Override
        protected void onWake() {
            vmAvailability[vmIndex] = true;
            System.out.println("VM " + vmIndex + " is now available again.");
        }
    }
}
