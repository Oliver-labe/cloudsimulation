package multiagent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.List;
import jade.core.AID;

public class SchedulerAgent extends Agent {
    private List<Task> priorityOneTasks = new ArrayList<>();
    private List<Task> priorityZeroTasks = new ArrayList<>();

    @Override
    protected void setup() {
        System.out.println("Scheduler agent has started.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null) {
                    String allTasksContent = msg.getContent();
                    System.out.println("Received task requirements: " + allTasksContent);

                    // 分割所有任务
                    String[] allTasks = allTasksContent.split(";");
                    for (String taskContent : allTasks) {
                        String[] params = taskContent.split(",");
                        int mips = Integer.parseInt(params[0]);
                        int ram = Integer.parseInt(params[1]);
                        int bw = Integer.parseInt(params[2]);
                        int size = Integer.parseInt(params[3]);
                        int priority = Integer.parseInt(params[4]);
                        long taskLength = Long.parseLong(params[5]);
                        int processingElements = Integer.parseInt(params[6]);
                        long fileSize = Long.parseLong(params[7]);
                        long outputSize = Long.parseLong(params[8]);
                        int cloudTaskId = Integer.parseInt(params[9]);

                        Task task = new Task(mips, ram, bw, size, priority, taskLength, processingElements, fileSize, outputSize, cloudTaskId);
                        if (priority == 1) {
                            priorityOneTasks.add(task);
                            System.out.println("Task added to priority 1 list.");
                            // 将任务发送给 BrokerAgent 进行进一步的处理
                            ACLMessage brokerMsg = new ACLMessage(ACLMessage.REQUEST);
                            brokerMsg.addReceiver(new AID("BrokerAgent", AID.ISLOCALNAME));
                            brokerMsg.setContent(taskContent);  // 将整个任务的详细信息发送给 Broker
                            send(brokerMsg);
                        } else if (priority == 0) {
                            priorityZeroTasks.add(task);
                            System.out.println("Task added to priority 0 list.");
                        } else {
                            System.out.println("Unknown task priority. Task not categorized.");
                        }
                    }
                } else {
                    block();
                }
            }
        });
    }
    // ...其他代码（例如 Task 类的定义）
}
