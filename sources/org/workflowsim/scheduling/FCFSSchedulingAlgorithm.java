/**
 * Copyright 2012-2013 University Of Southern California
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.workflowsim.scheduling;

import java.util.*;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Consts;
import org.research.Environment;
import org.workflowsim.*;
import org.workflowsim.utils.Parameters;

/**
 * The FCFS algorithm. 
 *
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.0
 * @date Apr 9, 2013
 */
public class FCFSSchedulingAlgorithm extends BaseSchedulingAlgorithm {
    static HashMap<CondorVM, Double> resourceUsage =null;
    static int averageBandwidth = 1000;
     private static Map<Task, Map<Task, Double>> transferCosts = null;


    /**
     * The main function
     */
    @Override
    public void run() {
        if (resourceUsage==null){
            resourceUsage= new HashMap<>();
            List<CondorVM> vms = getVmList();
            for (int i = 0; i < vms.size(); i++) {
                resourceUsage.put(vms.get(i), 0.0);

            }
            transferCosts = new HashMap<>();

        }

        calculateTransferCosts();
        timeAware();

//fcfs();



    }

    public void FCFS(){

        List<Cloudlet> cloudletList = getCloudletList();
        for (Iterator it = cloudletList.iterator(); it.hasNext();) {
            Cloudlet cloudlet = (Cloudlet) it.next();
            boolean stillHasVm = false;
            for (Iterator itc = getVmList().iterator(); itc.hasNext();) {

                CondorVM vm = (CondorVM) itc.next();
                if (vm.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
                    stillHasVm = true;
                    vm.setState(WorkflowSimTags.VM_STATUS_BUSY);
                    cloudlet.setVmId(vm.getId());
                    getScheduledList().add(cloudlet);
                    break;
                }
            }
            //no vm available
            if (!stillHasVm) {
                break;
            }
        }
    }

    public void fcfs(){
        for (Iterator it = getCloudletList().iterator(); it.hasNext();) {
            Cloudlet cloudlet = (Cloudlet) it.next();
            boolean stillHasVm = false;
            for (Iterator itc = getVmList().iterator(); itc.hasNext();) {

                CondorVM vm = (CondorVM) itc.next();
                if (vm.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
                    stillHasVm = true;
                    vm.setState(WorkflowSimTags.VM_STATUS_BUSY);
                    cloudlet.setVmId(vm.getId());
                    getScheduledList().add(cloudlet);
                    break;
                }
            }
            //no vm available
            if (!stillHasVm) {
                break;
            }
        }

    }
    public void timeAware() {
        System.out.println("Time aware scheduling");

        List<CondorVM> vms = getVmList();
        List<Task>  cloudlets = getCloudletList();
        Comparator<Cloudlet> compareById = new Comparator<Cloudlet>() {
            @Override
            public int compare(Cloudlet o1, Cloudlet o2) {
                return Double.compare(o1.getCloudletLength(),o2.getCloudletLength());
            }
        };


        for (int i =0; i<cloudlets.size(); i++) {
            Cloudlet cloulet = cloudlets.get(i);
            System.out.println(cloulet.getNumberOfPes());
            double max = Double.MAX_VALUE;
            CondorVM selectedVm = null;
            for (int j = 0; j < vms.size(); j++) {
                double temp = max;
                CondorVM vm = vms.get(j);


                double usage = resourceUsage.get(vm);
                double cpu = cloulet.getCloudletLength()/ vm.getMips();
                double transferCost = 0.0;
                for (Cloudlet parent  :cloudlets.get(i).getParentList() ){
                    transferCost+=transferCosts.get(parent).get(cloudlets.get(i));
                }
                max = Math.min(cpu +usage , max);
                if (max != temp) {
                    selectedVm = vm;
                }

            }

            if (selectedVm.getState()==WorkflowSimTags.VM_STATUS_IDLE){
                resourceUsage.put(selectedVm,max);
                cloulet.setVmId(selectedVm.getId());
                getScheduledList().add(cloulet);
                selectedVm.setState(WorkflowSimTags.VM_STATUS_BUSY);
            }

        }


    }

    private double calculateTransferCost(Task parent, Task child) {
        List<FileItem> parentFiles = parent.getFileList();
        List<FileItem> childFiles = child.getFileList();

        double acc = 0.0;

        for (FileItem parentFile : parentFiles) {
            if (parentFile.getType() != Parameters.FileType.OUTPUT) {
                continue;
            }

            for (FileItem childFile : childFiles) {
                if (childFile.getType() == Parameters.FileType.INPUT
                        && childFile.getName().equals(parentFile.getName())) {
                    acc += childFile.getSize();
                    break;
                }
            }
        }

        //file Size is in Bytes, acc in MB
        acc = acc / Consts.MILLION;
        // acc in MB, averageBandwidth in Mb/s
        return acc * 8 / averageBandwidth;
    }


    private void calculateTransferCosts() {
        // Initializing the matrix

        for (Task task1 : (List<Task>)getCloudletList()) {
            Map<Task, Double> taskTransferCosts = new HashMap<>();
            for (Task task2 : (List<Task>)getCloudletList()) {
                taskTransferCosts.put(task2, 0.0);
            }
            transferCosts.put(task1, taskTransferCosts);
        }

        // Calculating the actual values
        for (Task parent :  (List<Task>)getCloudletList()) {
            for (Task child : parent.getChildList()) {
                transferCosts.get(parent).put(child,
                        calculateTransferCost(parent, child));
            }
        }
    }
}
