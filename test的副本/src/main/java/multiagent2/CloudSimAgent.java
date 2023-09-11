package multiagent2;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;


public class CloudSimAgent extends Agent {
    private List<Cloudlet> cloudletList;
    private List<Vm> vmList;
    private DatacenterBroker broker;
    private int taskCount = 0;

    @Override
    protected void setup() {
    	
        cloudletList = new ArrayList<>();
        vmList = new ArrayList<>();

        initCloudSim();

        System.out.println("CloudSimAgent " + getAID().getName() + " is ready.");

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    if ("RUN_SIMULATION".equals(content)) {
                        // If received RUN_SIMULATION message, run the simulation
                        runSimulation();
                    } else {
                        // Otherwise, process the task parameters
                        System.out.println("Received task parameters: " + content);
                        processTaskParameters(content);
                    }
                }
                block();
            }
        });

        
    }
    private void processTaskParameters(String content) {
        String[] keyValuePairs = content.split(",");
        long length = 0;
        long fileSize = 0;
        long outputSize = 0;
        int pesNumber = 0;
        for (String keyValuePair : keyValuePairs) {
            String[] keyValue = keyValuePair.split(":");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            switch (key) {
                case "length":
                    length = Long.parseLong(value);
                    break;
                case "fileSize":
                    fileSize = Long.parseLong(value);
                    break;
                case "outputSize":
                    outputSize = Long.parseLong(value);
                    break;
                case "pesNumber":
                    pesNumber = Integer.parseInt(value);
                    break;
                default:
                    // Unknown key, can choose to throw exception or ignore
                    break;
            }
        }
        createCloudlet(length, fileSize, outputSize, pesNumber);
    }

    // ... All the other functions from your original CloudSimAgent code ...









    private void initCloudSim() {
        // Initialize the CloudSim library
        int num_user = 1;
        Calendar calendar = Calendar.getInstance();
        boolean trace_flag = false;
        CloudSim.init(num_user, calendar, trace_flag);

        // Create a Datacenter
        Datacenter datacenter = createDatacenter("Datacenter_1");

        // Create a DatacenterBroker
        broker = createBroker("Broker_1");
     // 创建虚拟机
        createVms(3);
        
        // 提交虚拟机列表给 broker
        broker.submitVmList(vmList);
    }

    private void createVms(int numberOfVms) {
        // VM Parameters
        int mips = 1000;
        long size = 10000; // image size (MB)
        int ram = 512; // vm memory (MB)
        long bw = 1000;
        int pesNumber = 1; // number of cpus
        String vmm = "Xen"; // VMM name

        for (int i = 0; i < numberOfVms; i++) {
            Vm vm = new Vm(i, broker.getId(), mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
            vmList.add(vm);
        }
    }


    private static Datacenter createDatacenter(String name) {

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		// our machine
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		List<Pe> peList = new ArrayList<Pe>();

		int mips = 1000;

		 // 3. Create PEs and add these into a list.
	    int numPEs = 10;  // New: Increase number of PEs to 5
	    for (int i = 0; i < numPEs; i++) {
	        peList.add(new Pe(i, new PeProvisionerSimple(mips))); 
	    }
	    // 4. Create Host with its id and list of PEs and add them to the list
		// of machines
	    // 4. Create Host with its id and list of PEs and add them to the list of machines
	    int hostId = 0;
	    int ram = 2048 * numPEs; // New: Adjust RAM according to number of PEs
	    long storage = 1000000;  // Adjust storage if needed
	    int bw = 10000 * numPEs;  // New: Adjust bandwidth according to number of PEs

		hostList.add(
			new Host(
				hostId,
				new RamProvisionerSimple(ram),
				new BwProvisionerSimple(bw),
				storage,
				peList,
				new VmSchedulerTimeShared(peList)
			)
		); // This is our machine

		// 5. Create a DatacenterCharacteristics object that stores the
		// properties of a data center: architecture, OS, list of
		// Machines, allocation policy: time- or space-shared, time zone
		// and its price (G$/Pe time unit).
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
													// devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

    private DatacenterBroker createBroker(String name) {
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }


    private static List<Pe> getPes(int mips) {
        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(mips)));
        return peList;
    }
    
    private void createCloudlet(long length, long fileSize, long outputSize, int pesNumber) {
        int id = cloudletList.size();
        UtilizationModel utilizationModel = new UtilizationModelFull();
        Cloudlet cloudlet = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
        cloudlet.setUserId(broker.getId());
        
        cloudletList.add(cloudlet);
        taskCount++;
    }

    private void runSimulation() {
        broker.submitCloudletList(cloudletList);

        CloudSim.startSimulation();
        CloudSim.stopSimulation();

        List<Cloudlet> resultList = broker.getCloudletReceivedList();
        printCloudletList(resultList);
    }


    private void printCloudletList(List<Cloudlet> list) {
        System.out.println("======= OUTPUT =======");
        for (Cloudlet cloudlet : list) {
            System.out.println("Cloudlet ID: " + cloudlet.getCloudletId() +
                               ", Status: " + cloudlet.getCloudletStatusString() +
                               ", Data center ID: " + cloudlet.getResourceId() +
                               ", VM ID: " + cloudlet.getVmId() +
                               ", Time taken: " + cloudlet.getActualCPUTime() +
                               ", Start time: " + cloudlet.getExecStartTime() +
                               ", Finish time: " + cloudlet.getFinishTime());
        }
    }

}
