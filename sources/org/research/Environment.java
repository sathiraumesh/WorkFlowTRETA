package org.research;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.workflowsim.*;
import org.workflowsim.utils.ClusteringParameters;
import org.workflowsim.utils.OverheadParameters;
import org.workflowsim.utils.Parameters;
import org.workflowsim.utils.ReplicaCatalog;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class Environment {

    public static WorkflowPlanner planner;
    public static WorkflowEngine wf;
    public static void main(String[] args) {



        try {

            for (int i = 2; i <= 2; i++) {


                int vmNum = 10;//number of vms;

                String daxPath = "/Users/sathiraumesh/Documents/research_Implemntation /WorkflowSim-1.0-master/config/dax/CyberShake_1000.xml";

                java.io.File daxFile = new File(daxPath);
                if (!daxFile.exists()) {
                    Log.printLine("Warning: Please replace daxPath with the physical path in your working environment!");
                    return;
                }

                Parameters.SchedulingAlgorithm sch_method = Parameters.SchedulingAlgorithm.FCFS;
                Parameters.PlanningAlgorithm pln_method = Parameters.PlanningAlgorithm.INVALID;
                ReplicaCatalog.FileSystem file_system = ReplicaCatalog.FileSystem.LOCAL;


                /**
                 * No overheads
                 */
                OverheadParameters op = new OverheadParameters(0, null, null, null, null, 0);

                /**
                 * No Clustering
                 */
                ClusteringParameters.ClusteringMethod method = ClusteringParameters.ClusteringMethod.NONE;
                ClusteringParameters cp = new ClusteringParameters(0, 0, method, null);

                Parameters.init(vmNum, daxPath, null,
                        null, op, cp, sch_method, pln_method,
                        null, 0);
                ReplicaCatalog.init(file_system);

                // before creating any entities.
                int num_user = 1;   // number of grid users
                Calendar calendar = Calendar.getInstance();
                boolean trace_flag = false;  // mean trace events


                CloudSim.init(num_user, calendar, trace_flag);

                WorkflowDatacenter datacenter0 = Infastructure.createDatacenter("Datacenter_0");

                WorkflowPlanner wfPlanner = new WorkflowPlanner("planner_0", 1);
                planner=wfPlanner;

                WorkflowEngine wfEngine = wfPlanner.getWorkflowEngine();


                List<CondorVM> vmlist0 = Infastructure.createRequiredVms (wfEngine.getSchedulerId(0), 25);

                wfEngine.submitVmList(vmlist0, 0);



                wfEngine.bindSchedulerDatacenter(datacenter0.getId(), 0);
                wf =wfEngine;
                CloudSim.startSimulation();
                List<Job> outputList0 = wfEngine.getJobsReceivedList();
                CloudSim.stopSimulation();
                Comparator<Job> compareById = new Comparator<Job>() {
                    @Override
                    public int compare(Job o1, Job o2) {
                        return Double.compare(o1.getDepth(),o2.getDepth());
                    }
                };
                Collections.sort(outputList0,compareById);
                printJobList(outputList0);

                Double makeSpan=  Metrics.calculateMakeSpan(outputList0);
                List vms =wf.getScheduler(0).getVmList();
                Metrics.data.add(makeSpan);
                CalculateDegreeofImbalnce(outputList0,vms);
                calculateThrougput(outputList0);
            }

            Metrics.writeToFile("cyberShake",Metrics.data);

        }catch (Exception e){
            System.out.println("Simulation failed");
        }
    }




    protected static void printJobList(List<Job> list) {
        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Job ID" + indent + "Task ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + indent
                + "Time" + indent + "Start Time" + indent + "Finish Time" + indent + "Depth");
        DecimalFormat dft = new DecimalFormat("###.##");
        for (Job job : list) {
            Log.print(indent + job.getCloudletId() + indent + indent);
            if (job.getClassType() == Parameters.ClassType.STAGE_IN.value) {
                Log.print("Stage-in");
            }
            for (Task task : job.getTaskList()) {
                Log.print(task.getCloudletId() + ",");
            }
            Log.print(indent);

            if (job.getCloudletStatus() == Cloudlet.SUCCESS ) {
                Log.print("SUCCESS");
                Log.printLine(indent + indent + job.getResourceId() + indent + indent + indent + job.getVmId()
                        + indent + indent + indent + dft.format(job.getActualCPUTime())
                        + indent + indent + dft.format(job.getExecStartTime()) + indent + indent + indent
                        + dft.format(job.getFinishTime()) + indent + indent + indent + job.getDepth());
            } else if (job.getCloudletStatus() == Cloudlet.FAILED) {
                Log.print("FAILED");
                Log.printLine(indent + indent + job.getResourceId() + indent + indent + indent + job.getVmId()
                        + indent + indent + indent + dft.format(job.getActualCPUTime())
                        + indent + indent + dft.format(job.getExecStartTime()) + indent + indent + indent
                        + dft.format(job.getFinishTime()) + indent + indent + indent + job.getDepth());
            }
        }

        System.out.println(list.size());

    }


    private static String CalculateDegreeofImbalnce(List<Job> list,List<CondorVM> vmlist) {
        HashMap<Vm,Double> executionList  = new HashMap<>();
        int size = list.size();
        Job cloudlet;
        double cost = 0;

        for (int i = 0; i <vmlist.size() ; i++) {
            executionList.put(vmlist.get(i),0.0);
            vmlist.get(i).getHost();
        }

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);

            if (cloudlet.getStatus() == Job.SUCCESS) {
                CondorVM utilized = null;
                for (int j = 0; j < vmlist.size(); j++) {
                    CondorVM vm = vmlist.get(j);
                    if (vm.getId() == cloudlet.getVmId()) {
                        utilized = vm;
                    }
                }

                executionList.put(utilized, executionList.get(utilized) + cloudlet.getActualCPUTime());
            }
        }

        List<Double> executionValues = new ArrayList();
        for (int i = 0; i <vmlist.size() ; i++) {
            executionValues.add(executionList.get(vmlist.get(i)));
        }

        double min = Double.MAX_VALUE;
        double max = 0;
        double total =0;
        for (int i = 0; i <executionValues.size() ; i++) {
            min = Math.min(min,executionValues.get(i));
            max = Math.max(max,executionValues.get(i));
            total+=executionValues.get(i);
        }

        double di = (max-min)/(total/executionValues.size());
        System.out.println(di);
        return dft.format(di);
    }




    public static String calculateThrougput(List<Job> list ){
        DecimalFormat dft = new DecimalFormat("###.##");
        double maxFt = 0;
        int noOfCloudlets = list.size();
        double througput = 0;
        Job cloudlet =null;
        for (int i = 0; i <list.size() ; i++) {
            cloudlet = list.get(i);
            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
                double currentFt =cloudlet.getFinishTime();
                maxFt = Math.max(currentFt,maxFt);
            }

        }
        througput = noOfCloudlets/maxFt;
        System.out.println("THROUGHPUT "+dft.format(througput));
        return dft.format(througput);

    }

}
