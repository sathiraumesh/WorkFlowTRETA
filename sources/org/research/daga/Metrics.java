package org.research.daga;

import org.cloudbus.cloudsim.Consts;
import org.workflowsim.CondorVM;
import org.workflowsim.FileItem;
import org.workflowsim.Task;
import org.workflowsim.utils.Parameters;

import java.util.List;

public class Metrics {

    public static double calculateComputaionCost(Task task,CondorVM vm){
        return task.getCloudletTotalLength()/vm.getCurrentRequestedTotalMips()*vm.getNumberOfPes();
    }

    public static double calculateTransferCost(Task parent,Task child ,double bandwith){

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

        // acc is in bytes need to covert to mb
        acc = acc /1024*1024 ;
        // acc in MB, averageBandwidth in Mb/s
        return acc  /bandwith;

    }
}
