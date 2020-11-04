package org.research.ga;

import org.cloudbus.cloudsim.Consts;
import org.research.daga.Monitor;
import org.workflowsim.CondorVM;
import org.workflowsim.FileItem;
import org.workflowsim.Task;
import org.workflowsim.utils.Parameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metrics {

    private static Map<Task, Map<Task, Double>> transferCosts = new HashMap<>();
    private static Map<Task, Map<CondorVM, Double>> computationCosts = new HashMap<>();
    private static  Map<CondorVM,Double>  resourceAvailabilityTime = new HashMap<>();


    private static void calculateTransferCostMatrix(List<Task> taskList, double bandwidth) {
        // Initializing the matrix
        for (Task task1 : taskList) {
            Map<Task, Double> taskTransferCosts = new HashMap<>();
            for (Task task2 : taskList) {
                taskTransferCosts.put(task2, 0.0);
            }
            transferCosts.put(task1, taskTransferCosts);
        }

        // Calculating the actual values
        for (Task parent : taskList) {
            for (Task child : parent.getChildList()) {
                transferCosts.get(parent).put(child,
                        calculateTransferCost(parent, child,bandwidth));
            }
        }
    }


    public static double calculateTransferCost(Task parent, Task child,double bandwidth) {
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
        return acc * 8 / bandwidth;
    }


    private static void calculateComputationCosts(List<Task> taskList,List<CondorVM> vmList) {
        for (Task task : taskList) {
            Map<CondorVM, Double> costsVm = new HashMap<>();
            for (Object vmObject : vmList) {
                CondorVM vm = (CondorVM) vmObject;
                if (vm.getNumberOfPes() < task.getNumberOfPes()) {
                    costsVm.put(vm, Double.MAX_VALUE);
                } else {
                    costsVm.put(vm,
                            task.getCloudletTotalLength() / vm.getMips());
                }
            }
            computationCosts.put(task, costsVm);
        }
    }


    public static void calculateResourceAvailabilityTime (List<CondorVM> vmList){
        for (int i = 0; i <vmList.size() ; i++) {
            CondorVM vm = vmList.get(i);
            resourceAvailabilityTime.put(vm,0.0);


        }
    }


    public static Map<Task, Map<Task, Double>> getTransferCosts() {
        return transferCosts;
    }

    public Map<Task, Map<CondorVM, Double>> getComputationCosts() {
        return computationCosts;
    }

    public static Map<CondorVM, Double> getResourceAvailabilityTime() {
        return resourceAvailabilityTime;
    }


}
