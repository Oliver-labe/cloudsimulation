package multiagent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.MessageTemplate;
import java.util.List;
import java.util.ArrayList;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class BrokerAgent extends Agent {
    private List<VMOrDataCenter> availableCenters;
    

    @Override
    protected void setup() {
        System.out.println("Broker agent has started.");

        // 初始化虚拟机或数据中心的列表
        availableCenters = new ArrayList<>();
        availableCenters.add(new VMOrDataCenter(1, 100, 2000, 100, 5,1000));
        availableCenters.add(new VMOrDataCenter(2, 200, 4000, 150, 7,1500));
        availableCenters.add(new VMOrDataCenter(3, 100, 1000, 80, 10,3000));
        availableCenters.add(new VMOrDataCenter(4, 200, 3000, 120, 7,500));
        availableCenters.add(new VMOrDataCenter(5, 100, 2000, 100, 2,1000));
        availableCenters.add(new VMOrDataCenter(6, 200, 5000, 200, 4,800)); 

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println("Broker received: " + content);

                    
                        // 使用convertToTask方法转换content为Task对象
                        Task receivedTask = convertToTask(content);

                        // 然后处理该任务，例如：
                        onTaskReceived(receivedTask);
                } else {
                    block();
                }
            }
            });
    }

    private Task convertToTask(String content) {
        String[] taskDetails = content.split(",");

        int mips = Integer.parseInt(taskDetails[0]);
        int ram = Integer.parseInt(taskDetails[1]);
        int bw = Integer.parseInt(taskDetails[2]);
        int sizeofvm = Integer.parseInt(taskDetails[3]);
        int priority = Integer.parseInt(taskDetails[4]);
        long length = Long.parseLong(taskDetails[5]);
        int processingElements = Integer.parseInt(taskDetails[6]);
        long fileSize = Long.parseLong(taskDetails[7]);
        long outputSize = Long.parseLong(taskDetails[8]);
        int cloudTaskID = Integer.parseInt(taskDetails[9]);

        return new Task(mips, ram, bw, sizeofvm, priority, length, processingElements,fileSize,outputSize,cloudTaskID);
    }


    private void onTaskReceived(Task task) {
        VMOrDataCenter selectedCenter = selectSuitableVMOrDataCenter(task, availableCenters);
        if (selectedCenter != null) {
            // 根据选择的数据中心或虚拟机调度任务
            scheduleTask(task, selectedCenter);
        } else {
            // 没有合适的数据中心或虚拟机
            System.out.println("No suitable VM or DataCenter found for the task!");
        }
    }

    private VMOrDataCenter selectSuitableVMOrDataCenter(Task task, List<VMOrDataCenter> availableCenters) {
        // Logic to select suitable VM or DataCenter based on task requirements
        for (VMOrDataCenter center : availableCenters) {
            if (isSuitable(task, center)) {
                return center;
            }
        }
        return null;
    }

    private boolean isSuitable(Task task, VMOrDataCenter center) {
        // Example: Check if center has sufficient MIPS for the task
    	System.out.println("Checking suitability for center ID: " + center.getId());
        if (task.getMIPS() > center.getAvailableMIPS()) {
        	System.out.println("Center ID " + center.getId() + " rejected due to insufficient MIPS.");
            return false;
        }
        // Add more conditions as per your requirements
        // Check if center has sufficient RAM for the task
        if (task.getram() > center.getTotalRAM()) {
        	System.out.println("Center ID " + center.getId() + " rejected due to insufficient RAM.");
            return false;
        }

        // Check if center has sufficient bandwidth for the task
        if (task.getbw() > center.getbw()) {
        	 System.out.println("Center ID " + center.getId() + " rejected due to insufficient bandwidth.");
            return false;
        }

        // Check if center has sufficient storage for the task
        if (task.getsize() > center.getAvailableStorage()) {
        	System.out.println("Center ID " + center.getId() + " rejected due to insufficient storage.");
            return false;
        }
        return true;
    }

    private void scheduleTask(Task task, VMOrDataCenter selectedCenter) {
        // Logic to schedule task to selected VM or DataCenter
        System.out.println("Scheduling task to " + selectedCenter.getId());

        // Create the ACLMessage
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("runagent", AID.ISLOCALNAME));
        msg.setLanguage("English");
        msg.setOntology("Cloud-Computing-Ontology");

        // Convert the task to a string (or serialize it into a format you prefer)
        String taskAsString = task.toString(); // Assume Task class has a valid toString method
        System.out.println("Sending message content: " + taskAsString);
        msg.setContent(taskAsString);

        // Send the message
        send(msg);
    }

}
