package multiagent;

public class VMOrDataCenter {
    private int id; // 唯一标识符
    private int availableMIPS; // 可用的MIPS
    private int totalRAM; // 总RAM
    private int availableStorage; // 可用的存储
    private int numberOfCores; // 核心数量
    private int bw;

    // 构造函数
    public VMOrDataCenter(int id, int availableMIPS, int totalRAM, int availableStorage, int numberOfCores,int bw) {
        this.id = id;
        this.availableMIPS = availableMIPS;
        this.totalRAM = totalRAM;
        this.availableStorage = availableStorage;
        this.numberOfCores = numberOfCores;
        this.bw = bw;
    }

    // 为了简化，我们只考虑MIPS的需求，当然你可以添加更多的检查，例如RAM、存储等
    public boolean canHandleTask(int requiredMIPS) {
        return this.availableMIPS >= requiredMIPS;
    }

    // 处理任务后，减少可用资源
    public void allocateResourcesForTask(int requiredMIPS) {
        this.availableMIPS -= requiredMIPS;
    }

    // 以下是一些getter和setter

    public int getId() {
        return id;
    }

    public int getAvailableMIPS() {
        return availableMIPS;
    }

    public void setAvailableMIPS(int availableMIPS) {
        this.availableMIPS = availableMIPS;
    }

    public int getTotalRAM() {
        return totalRAM;
    }

    public void setTotalRAM(int totalRAM) {
        this.totalRAM = totalRAM;
    }

    public int getAvailableStorage() {
        return availableStorage;
    }

    public void setAvailableStorage(int availableStorage) {
        this.availableStorage = availableStorage;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }

    public void setNumberOfCores(int numberOfCores) {
        this.numberOfCores = numberOfCores;
    }
    public int getbw() {
        return bw;
    }

  
}
