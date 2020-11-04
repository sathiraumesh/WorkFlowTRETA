package org.research.daga;

import org.cloudbus.cloudsim.Consts;
import org.workflowsim.CondorVM;
import org.workflowsim.FileItem;
import org.workflowsim.Task;
import org.workflowsim.utils.Parameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Monitor {


    private static HashMap<CondorVM,Double> resourceAvailabilityMatrix = new HashMap<>();
    private static HashMap<Task,Double> earliestStartTimesTaskMatrix = new HashMap<>();
    private static HashMap<Task,Double> earliestFinishTimesTaskMatrix = new HashMap<>();
    private static Map<Task, Map<Task, Double>> transferCosts = new HashMap<>();
    private static double averageBandwidth;


    public static void InitAvailbleTimeVmMatrix(List<CondorVM> vmList ){

        for (int i = 0; i <vmList.size() ; i++) {
            resourceAvailabilityMatrix.put(vmList.get(i),0.0);
        }

    }

    private static void InitEarliestStartTimeTaskMatrix(List<Task> taskList ){

        for (int i = 0; i <taskList.size() ; i++) {
            Task task = taskList.get(i);
            if(task.getParentList().size()>0){
                earliestStartTimesTaskMatrix.put(task,Double.MAX_VALUE);

            }else{

                earliestStartTimesTaskMatrix.put(task,0.0);
            }

        }

    }


    private static double calculateAverageBandwidth(List<CondorVM> vmList){
        double avg = 0.0;
        for (Object vmObject : vmList) {
            CondorVM vm = (CondorVM) vmObject;
            avg += vm.getBw();
        }
        return avg / vmList.size();
    }


    private static void InitEarliestFinishTimeTaskMatrix(List<Task> taskList ){

        for (int i = 0; i <taskList.size() ; i++) {
            Task task = taskList.get(i);
                earliestFinishTimesTaskMatrix.put(task,Double.MAX_VALUE);

        }

    }


    private static void calculateTransferCostMatrix(List<Task> taskList,double bandwidth) {
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



    public static void InitMonitor(List<Task> tasks,List<CondorVM> vms){
       averageBandwidth= calculateAverageBandwidth(vms);
        InitAvailbleTimeVmMatrix(vms);
        InitEarliestFinishTimeTaskMatrix(tasks);
        calculateTransferCostMatrix(tasks,averageBandwidth);


    }
    public static HashMap<CondorVM, Double> getResourceAvailabilityMatrix() {
        return resourceAvailabilityMatrix;
    }

    public static HashMap<Task, Double> getEarliestStartTimesTaskMatrix() {
        return earliestStartTimesTaskMatrix;
    }

    public static Map<Task, Map<Task, Double>> getTransferCosts() {
        return transferCosts;
    }


}
