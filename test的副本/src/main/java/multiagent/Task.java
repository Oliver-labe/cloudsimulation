package multiagent;

public class Task {
    private long taskLength;
    private int processingElements;
    private long fileSize;
    private long outputSize;
    private int cloudTaskId;
    private int priority;
    private int mips;
    private int ram;
    private int bw;
    private int size;
    // ...其他属性和方法...

    public Task(int mips,int ram, int bw, int size, int priority, long taskLength,int processingElements,long fileSize,long outputSize,int cloudTaskId) {
        this.taskLength = taskLength;
        this.processingElements = processingElements;
        this.fileSize = fileSize;
        this.outputSize = outputSize;
        this.priority = priority;
        this.mips=mips;
        this.ram = ram;
        this.bw = bw;
        this.size = size;
    }
    public int getcloudTaskId() {
        return cloudTaskId;
    }
    public int getMIPS() {
        return mips;
    }
    public int getram() {
        return ram;
    }
    public int getbw() {
        return bw;
    }
    public int getsize() {
        return size;
    }
    // Getter and setter methods...
}
