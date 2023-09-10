package multiagent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MainContainerLauncher {

    public static void main(String[] args) {
        // 获取JADE运行时实例
        Runtime runtime = Runtime.instance();

        // 创建主容器的配置文件
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.CONTAINER_NAME, "Main-Container");
        profile.setParameter(Profile.GUI, "true"); // 启用图形用户界面

        // 创建主容器
        AgentContainer mainContainer = runtime.createMainContainer(profile);

        try {
            // 创建并启动 UserRequestAgent
            AgentController userRequestAgent = mainContainer.createNewAgent(
                    "UserRequestAgent", "multiagent.UserRequestAgent", null);
            userRequestAgent.start();

            // 创建并启动 SchedulerAgent
            AgentController schedulerAgent = mainContainer.createNewAgent(
                    "SchedulerAgent", "multiagent.SchedulerAgent", null);
            schedulerAgent.start();

            // 创建并启动 BrokerAgent
            AgentController brokerAgent = mainContainer.createNewAgent(
                    "BrokerAgent", "multiagent.BrokerAgent", new Object[]{});
            brokerAgent.start();

            // 创建并启动 DatacenterAgent
            AgentController datacenterAgent = mainContainer.createNewAgent(
                    "DatacenterAgent", "multiagent.DatacenterAgent", new Object[]{});
            datacenterAgent.start();

            // 创建并启动 VMAgent
            AgentController vmAgent = mainContainer.createNewAgent(
                    "VMAgent", "multiagent.VMAgent", new Object[]{});
            vmAgent.start();
            
            AgentController runAgent = mainContainer.createNewAgent(
                    "RunAgent", "multiagent.runagent", new Object[]{});
            runAgent.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
