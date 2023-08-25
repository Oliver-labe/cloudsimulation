package multiagent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.MessageTemplate;

public class BrokerJadeAgent extends Agent {
    @Override
    protected void setup() {
    	System.out.println("Broker agent has started.");
    	 addBehaviour(new CyclicBehaviour() {
             public void action() {
                 MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                 ACLMessage msg = myAgent.receive(mt);
                 if (msg != null) {
                     String content = msg.getContent();
                     System.out.println("Broker received: " + content);

                     // 这里可以添加其他代码，根据收到的任务要求，找到合适的虚拟机或数据中心。
                 } else {
                     block();
                 }
             }
         });
     }
 }
