package multiagent2;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;

public class ContainerLauncher {

    public static void main(String[] args) {
        // 获取JADE运行时环境
        Runtime rt = Runtime.instance();

        // 创建主容器的配置文件
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.MAIN_PORT, "1099");

        // 创建一个新的非主容器，连接到主机上的主容器
        ContainerController container = rt.createMainContainer(profile);

        try {
            // 创建代理，并将其注册到运行时环境
            AgentController userAgent = container.createNewAgent("UserAgent", "multiagent2.UserAgent", new Object[]{});
            AgentController taskAgent = container.createNewAgent("TaskAgent", "multiagent2.TaskAgent", new Object[]{});
            AgentController vmAgent = container.createNewAgent("VMAgent", "multiagent2.VMAgent", new Object[]{});
            AgentController cloudSimAgent = container.createNewAgent("CloudSimAgent", "multiagent2.CloudSimAgent", new Object[]{}); // 新增这一行

            // 启动代理
            // 启动代理
            userAgent.start();
            taskAgent.start();
            vmAgent.start();
            cloudSimAgent.start(); // 新增这一行
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
