package org.workflowsim.planning;


import org.cloudbus.cloudsim.Consts;
import org.research.daga.*;
import org.research.ga.Chromosome;
import org.research.ga.Gene;
import org.research.matrix.Couple;
import org.workflowsim.CondorVM;
import org.workflowsim.FileItem;
import org.workflowsim.Task;
import org.workflowsim.utils.Parameters;

import java.util.*;

public class DepthAwarePlanningAlgorithm extends BasePlanningAlgorithm {
    private Map<Task, Map<Task, Double>> transferCosts= new HashMap<>();

    @Override
    public void run() throws Exception {

//      List<Gene> genes=  Gene.createGenlist(getVmList(),getTaskList());
//      Chromosome chromome = Chromosome.createChromosome(genes);
//        System.out.println(chromome);
        timeAware();



    }

    private void calculateTransferCosts() {
        // Initializing the matrix
        for (Task task1 : getTaskList()) {
            Map<Task, Double> taskTransferCosts = new HashMap<>();
            for (Task task2 : getTaskList()) {
                taskTransferCosts.put(task2, 0.0);
            }
            transferCosts.put(task1, taskTransferCosts);
        }

        // Calculating the actual values
        for (Task parent : getTaskList()) {
            for (Task child : parent.getChildList()) {
                transferCosts.get(parent).put(child,
                        calculateTransferCost(parent, child));
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
        return acc * 8 / 1000;
    }



    public void timeAware() {
        System.out.println("Time aware scheduling");
        HashMap<CondorVM, Double> resourceUsage = new HashMap<>();
        List<CondorVM> vms = getVmList();
        List<Task> tasks = getTaskList();


        for (int i = 0; i < vms.size(); i++) {
            resourceUsage.put(vms.get(i), 0.0);
        }



        for (int i = tasks.size()-1; i >=0; i--) {
            Task task = tasks.get(i);
            double max = Double.MAX_VALUE;
            CondorVM selectedVm = null;
            for (int j = 0; j < vms.size(); j++) {
                double temp = max;
                CondorVM vm = vms.get(j);


                double usage = resourceUsage.get(vm);
                double cpu = task.getCloudletLength() / vm.getCurrentRequestedTotalMips();
                max = Math.min(cpu + usage , max);
                if (max != temp) {
                    selectedVm = vm;
                }

            }

            resourceUsage.put(selectedVm,max);
            task.setVmId(selectedVm.getId());
        }


    }

    public void DepthWaraeSheduling(){


        List<Task> tasks = getTaskList();
        List<CondorVM> vms = getVmList();
        HashMap<CondorVM,List<Task>> shedule = new HashMap<>();
        HashMap<CondorVM,Double> resourceAvailableTimes = new HashMap<>();

        for (int i = 0; i <getVmList().size() ; i++) {
            resourceAvailableTimes.put((CondorVM) getVmList().get(i),0.0);
        }

        for (int i = 0; i <vms.size() ; i++) {
            shedule.put(vms.get(i),new ArrayList<>());
        }

        Monitor.InitMonitor(tasks,vms);

        HashMap<Integer,List<Task>> depthTasks = getTasksindepth();
        int iterations =30;
        int childPopulationSize = 20;

        List<Integer> depths = new ArrayList<>(depthTasks.keySet());


        HashMap<Task,Couple> shed= new HashMap<>();
        for (int i = 0; i <depthTasks.size(); i++) {

            HashMap<Task,List<Couple>> taskMatrix = new HashMap<>();
            List<Task> taskList = depthTasks.get(depths.get(i));
            for (int j = 0; j <taskList.size() ; j++) {
                taskMatrix.put(taskList.get(j),new ArrayList<>());

                for (int k = 0; k <vms.size() ; k++) {
                    taskMatrix.get(taskList.get(j)).add(new Couple(vms.get(k),taskList.get(j)));
                    double earliestStartTime = 0;
                    if (taskList.get(j).getParentList().size()>0){

                        for (int l = 0; l <taskList.get(j).getParentList().size() ; l++) {

                            Task parentTask = taskList.get(j).getParentList().get(l);

                            Double  tCost =Monitor.getTransferCosts().get(parentTask).get(taskList.get(j));
                            taskMatrix.get(taskList.get(j)).get(taskMatrix.get(taskList.get(j)).size()-1).setTaransferTime(tCost);
                            if (shed.size()>0){
                                earliestStartTime =Math.max(shed.get(parentTask).finsihTime,earliestStartTime);
                            }
                        }
                    }

                    taskMatrix.get(taskList.get(j)).get(taskMatrix.get(taskList.get(j)).size()-1).setEarliestStartTime(Math.max(earliestStartTime,resourceAvailableTimes.get(vms.get(k))));

                }


                Collections.sort(taskMatrix.get(taskList.get(j)));
            }




            for (int j = 0; j <taskList.size() ; j++) {

                double max = 0;

                Couple coup = null;

                Iterator matrixIter = taskMatrix.entrySet().iterator();

                while (matrixIter.hasNext()) {

                    Map.Entry mapElement = (Map.Entry)matrixIter.next();
                    List<Couple> coupleList = (List<Couple>) mapElement.getValue();
                    double change = max;
                    max = Math.max(coupleList.get(0).cost,max);

                    if (change!=max){
                        coup=coupleList.get(0);
                    }
                    System.out.println("");

                }

                coup.choosen=true;

                Iterator Iter2 = taskMatrix.entrySet().iterator();


                while (Iter2.hasNext()) {

                    Map.Entry mapElement = (Map.Entry)Iter2.next();
                    List<Couple> coupleList = (List<Couple>) mapElement.getValue();

                    for (int k = 0; k <coupleList.size() ; k++) {

                        if (coup.vm.getId()==coupleList.get(k).vm.getId()){
                            coupleList.get(k).setEarliestStartTime(coup.cost);
                        }

                    }

                    Collections.sort((List<Couple>) mapElement.getValue());
                }







                coup.setFinishTime();
                resourceAvailableTimes.put(coup.vm,coup.finsihTime);
                shed.put(coup.task,coup);
                taskMatrix.remove(coup.task);



            }



        }

        Iterator sheduleItr = shed.entrySet().iterator();

        while (sheduleItr.hasNext()) {
            Map.Entry mapElement = (Map.Entry)sheduleItr.next();
            Task task = (Task) mapElement.getKey();
            Couple co = (Couple) mapElement.getValue();
            task.setVmId(co.vm.getId());
        }



    }


    private HashMap getTasksindepth (){

        TreeSet<Integer> depths = new TreeSet<>();
        HashMap<Integer,List<Task>> depthTasks = new HashMap<>();

        for (int i = 0; i <getTaskList().size() ; i++) {

            depths.add(getTaskList().get(i).getDepth());
        }

        List<Integer> depthNumbers = new ArrayList<>(depths);

        for (int i = 0; i <depthNumbers.size() ; i++) {

            List<Task> tasksInDepth =new ArrayList<>();

            for (int j = 0; j <getTaskList().size() ; j++) {
                if(getTaskList().get(j).getDepth()==depthNumbers.get(i)){
                    tasksInDepth.add(getTaskList().get(j))   ;

                }
            }

            depthTasks.put(depthNumbers.get(i),tasksInDepth);
        }

        return depthTasks;

    }




    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }



}
