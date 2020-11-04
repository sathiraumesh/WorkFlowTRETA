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
import org.research.daga.Monitor;
import org.research.matrix.Couple;
import org.workflowsim.*;
import org.workflowsim.utils.Parameters;
import org.workflowsim.utils.ReplicaCatalog;

/**
 * Data aware algorithm. Schedule a job to a vm that has most input data it requires. 
 * It only works for a local environment. 
 *
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.0
 * @date Apr 9, 2013
 */
public class DataAwareSchedulingAlgorithm extends BaseSchedulingAlgorithm {



    static HashMap<CondorVM, Double> resourceUsage =null;
    public DataAwareSchedulingAlgorithm() {
        super();

        if (resourceUsage==null){
            resourceUsage= new HashMap<>();
            List<CondorVM> vms = getVmList();
            for (int i = 0; i < vms.size(); i++) {
                resourceUsage.put(vms.get(i), 0.0);
            }
        }


    }

    @Override
    public void run() throws Exception {
        timeAware();
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
        return acc * 8 / 1000;
    }



    public void timeAware() {
        System.out.println("Time aware scheduling");
       ;
        List<CondorVM> vms = getVmList();
        List<Cloudlet>  cloudlets = getCloudletList();


        for (int i = 0; i <cloudlets.size(); i++) {
            Cloudlet cloulet = cloudlets.get(i);
            double max = Double.MAX_VALUE;
            CondorVM selectedVm = null;
            for (int j = 0; j < vms.size(); j++) {
                double temp = max;
                CondorVM vm = vms.get(j);


                double usage = resourceUsage.get(vm);
                double cpu = cloulet.getCloudletLength() / vm.getCurrentRequestedTotalMips();
                max = Math.min(cpu + usage , max);
                if (max != temp) {
                    selectedVm = vm;
                }

            }

            if (selectedVm.getState()==WorkflowSimTags.VM_STATUS_IDLE){
                resourceUsage.put(selectedVm,max);
                cloulet.setVmId(selectedVm.getId());
            }

        }


    }




    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


}
