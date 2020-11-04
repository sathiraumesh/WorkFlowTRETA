package org.research;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.workflowsim.CondorVM;
import org.workflowsim.WorkflowDatacenter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Infastructure {

    public static List<CondorVM> createHomogenousVMCondor(int userId, int vms) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<CondorVM> list = new LinkedList<>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        CondorVM[] vm = new CondorVM[vms];
        for (int i = 0; i < vms; i++) {
            double ratio = 1.0;
            vm[i] = new CondorVM(i, userId, mips * ratio, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            list.add(vm[i]);
        }
        return list;
    }

    public static int getRandomInteger(int maximum, int minimum){
        return ((int) (Math.random()*(maximum - minimum))) + minimum;
    }


    public static List<CondorVM> createHetrogenousVMCondor(int userId, int vms) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<CondorVM> list = new LinkedList<>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        CondorVM[] vm = new CondorVM[vms];


        for (int i = 0; i < vms; i++) {
            double ratio = 1.0;
            vm[i] = new CondorVM(i, userId,getRandomInteger(3000,1000)  * ratio, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            list.add(vm[i]);
        }
        return list;
    }


    public static List<CondorVM> createHetrogenousVMCondorStatic(int userId, int vms) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<CondorVM> list = new LinkedList<>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        CondorVM vm ;

        vm = new CondorVM(0, userId,1000, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
        vm.getCost();
        list.add(vm);


        vm = new CondorVM(1, userId,1000, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
        list.add(vm);


        vm = new CondorVM(2, userId,2000, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
        list.add(vm);


        vm = new CondorVM(3, userId,2000, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
        list.add(vm);
        mips = 2000;
//
//        vm = new CondorVM(3, userId,mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
//        list.add(vm);
//        vm = new CondorVM(4, userId,mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
//        list.add(vm);
//        vm = new CondorVM(5, userId,mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
//        list.add(vm);
//        mips = 3000;
//        vm = new CondorVM(6, userId,mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
//        list.add(vm);
//
//        vm = new CondorVM(7, userId,mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
//       list.add(vm);
//
//        vm = new CondorVM(8, userId,mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
//        list.add(vm);

        return list;
    }




    public static WorkflowDatacenter createDatacenter(String name) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        //    Machines
        List<Host> hostList = new ArrayList<>();
        List<Pe> peList1 = new ArrayList<>();
        int mips = 6000;
        // 3. Create PEs and add these into the list.
        //for a quad-core machine, a list of 4 PEs is required:
        peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
        peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
        peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
        peList1.add(new Pe(4, new PeProvisionerSimple(mips)));



        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        //    create a list to store these PEs before creating
        //    a Machine.
        int hostId = 0;
        for (int i = 0; i < 20; i++) {


            int ram = 2048; //host memory (MB)
            long storage = 1000000; //host storage
            int bw = 1000000;
            hostList.add(
                    new Host(
                            hostId,
                            new RamProvisionerSimple(ram),
                            new BwProvisionerSimple(bw),
                            storage,
                            peList1,
                            new VmSchedulerSpaceShared(peList1))); // This is our first machine
            hostId++;
        }

        // 4. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.1;	// the cost of using storage in this resource
        double costPerBw = 0.1;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<>();	//we are not adding SAN devices by now
        WorkflowDatacenter datacenter = null;

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        // 5. Finally, we need to create a storage object.
        /**
         * The bandwidth within a data center in MB/s.
         */
        int maxTransferRate = 15;// the number comes from the futuregrid site, you can specify your bw

        try {
            // Here we set the bandwidth to be 15MB/s
            HarddriveStorage s1 = new HarddriveStorage(name, 1e12);
            s1.setMaxTransferRate(maxTransferRate);
            storageList.add(s1);
            datacenter = new WorkflowDatacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datacenter;
    }


    public static List<CondorVM> createRequiredVms(int brokerId ,int reqVms){

        ArrayList<CondorVM> vmlist = new ArrayList<CondorVM>();

        //VM description
        int vmid = 0;
        //int mips = 1000;
        int[] mips={
                1000, 1000, 1000, 1000, 1000,
                2000, 2000, 2000, 2000, 2000,
                3000, 3000, 3000, 3000, 3000,
                4000, 4000, 4000, 4000, 4000,
                5000, 5000, 5000, 5000, 5000
        };
        long size = 1000; //image size (MB)
        int ram = 512; //vm memory (MB)
        long bw = 1000;
        int[] pesNumber = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}; //number of cpus
        String vmm = "Xen"; //VMM name



        for(vmid=0;vmid<reqVms;vmid++){
            //add the VMs to the vmListn

            vmlist.add(new CondorVM(vmid, brokerId, mips[vmid], pesNumber[vmid], ram, bw,
                    size, vmm, new CloudletSchedulerSpaceShared()));
        }

        System.out.println("VmsCreator function Executed... SUCCESS:)");
        return vmlist;

    }



}
