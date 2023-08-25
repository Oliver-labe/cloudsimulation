	package multiagent;
	import jade.core.Agent;
	import jade.lang.acl.ACLMessage;
	import java.util.Scanner;
	import jade.core.AID;
	
	public class UserRequestAgent extends Agent {
	
	    @Override
	    protected void setup() {
	        System.out.println("User request agent has started.");
	
	        Scanner scanner = new Scanner(System.in);
	        String continueInput = "1";  // 初始化为1，以确保循环至少执行一次
	        while ("1".equals(continueInput)) {
	        // 请求用户输入MIPS
	        System.out.println("Please enter the MIPS (Million Instructions Per Second) for the task:");
	        String mips = scanner.nextLine();
	
	        // 请求用户输入RAM
	        System.out.println("Please enter the RAM (in MB) for the task:");
	        String ram = scanner.nextLine();
	
	        // 请求用户输入BW (Bandwidth)
	        System.out.println("Please enter the BW (Bandwidth in Mbps) for the task:");
	        String bw = scanner.nextLine();
	
	        // 请求用户输入任务大小
	        System.out.println("Please enter the size (in bytes) of the task:");
	        String size = scanner.nextLine();
	
	        // 请求用户输入任务优先级
	        System.out.println("Please enter the priority of the task:");
	        String priority = scanner.nextLine();
	
	        // 请求用户输入任务长度
	        System.out.println("Please enter the length of the task:");
	        String taskLength = scanner.nextLine();
	
	        // 请求用户输入处理元素数量
	        System.out.println("Please enter the number of processing elements for the task:");
	        String processingElements = scanner.nextLine();
	
	        // 请求用户输入文件大小
	        System.out.println("Please enter the file size (in bytes) for the task:");
	        String fileSize = scanner.nextLine();
	
	        // 请求用户输入输出大小
	        System.out.println("Please enter the output size (in bytes) for the task:");
	        String outputSize = scanner.nextLine();
	
	        // 请求用户输入云任务ID
	        System.out.println("Please enter the cloud task ID:");
	        String cloudTaskId = scanner.nextLine();
	
	        // 打印用户输入的任务需求
	        System.out.println("\nReceived task requirement:");
	        System.out.println("MIPS: " + mips);
	        System.out.println("RAM: " + ram);
	        System.out.println("BW: " + bw);
	        System.out.println("Size: " + size);
	        System.out.println("Priority: " + priority);
	        System.out.println("Length of the task: " + taskLength);
	        System.out.println("Number of processing elements: " + processingElements);
	        System.out.println("File Size: " + fileSize);
	        System.out.println("Output Size: " + outputSize);
	        System.out.println("Cloud Task ID: " + cloudTaskId);
	
	        // 创建并发送请求消息到SchedulerAgent
	        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
	        msg.addReceiver(new jade.core.AID("SchedulerAgent", AID.ISLOCALNAME));
	        msg.setContent(mips + "," + ram + "," + bw + "," + size + "," + priority + "," + taskLength + "," + processingElements + "," + fileSize + "," + outputSize + "," + cloudTaskId);
	        send(msg);
	        System.out.println("Do you want to add another task? Enter 1 for YES or 0 for NO:");
            continueInput = scanner.nextLine();
        }
	        System.out.println("The task requirement has been sent to the scheduling agent.");
	        scanner.close();
	    }
	}
