package multiagent2;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class MainContainerLauncher {
    public static void main(String[] args) {
        // 获取JADE运行时环境
        Runtime runtime = Runtime.instance();

        // 创建一个默认的Profile
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.MAIN_PORT, "1099");
        profile.setParameter(Profile.CONTAINER_NAME, "MainContainer");

        // 创建一个新的非主容器，连接到本地主容器
        ContainerController container = runtime.createMainContainer(profile);

        try {
            // 创建并启动UserAgent
            AgentController user_agent = container.createNewAgent("userAgent", "multiagent2.UserAgent", new Object[]{});
            user_agent.start();

            // 创建并启动CloudSimAgent
            AgentController cloudSim_agent = container.createNewAgent("cloudSimAgent", "multiagent2.CloudSimAgent", new Object[]{});
            cloudSim_agent.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

