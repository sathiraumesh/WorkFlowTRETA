package org.research.daga;



import org.workflowsim.CondorVM;
import org.workflowsim.FileItem;
import org.workflowsim.Task;
import org.workflowsim.utils.Parameters;

import java.util.*;

public class SchedulingChromosome implements Comparable<SchedulingChromosome> {


    public List<Gene> genes;
    Map<CondorVM,Double> resourceAvailability;
    Map<Task,Double> TaskFinisTime;


    double fitness = 1.0D/00;

    public SchedulingChromosome(List<Gene> genes){

        this.genes = genes;

    }

    public int getNumberOfGenes(){
        return this.genes.size();
    }

    public List<Gene> genes(){
        return this.genes;
    }


    public double getFitness(){
        return this.fitness =this.costFunction();
    }



    public double costFunction (){

        double makespan=0;
        ArrayList<Double> taskFinishTimes = new ArrayList<>(this.TaskFinisTime.values());

        for (int i = 0; i < taskFinishTimes.size(); i++) {
                makespan = Math.max(makespan,taskFinishTimes.get(i));
        }

        return  makespan;
    }


    @Override
    public int compareTo(SchedulingChromosome o) {
        return Double.compare(this.getFitness(),o.getFitness());
    }


    public void updateTaskFinishTimes(){

        Collections.sort(this.genes);
        Map<CondorVM,Double> reourceAvailability = new HashMap<>(Monitor.getResourceAvailabilityMatrix());
        Map<Task,Double> TaskFinisTime = new HashMap<>(Monitor.getEarliestStartTimesTaskMatrix());


        for (int i = 0; i <genes.size() ; i++) {

            Task task   =genes.get(i).task;
            CondorVM vm   =genes.get(i).vm;
            List<Task> parentList  =task.getParentList();

            double availableTimeOfresource = reourceAvailability.get(vm);
            double maxFinishTimeOfParent = calculateMaxFinishTimeOfParent(parentList,TaskFinisTime);
            double earliestStartTimeOfTask = Math.max(availableTimeOfresource,maxFinishTimeOfParent);
            double finishTimeOfTask = earliestStartTimeOfTask+task.getCloudletTotalLength()/vm.getCurrentRequestedTotalMips();
            reourceAvailability.put(vm,finishTimeOfTask);
            TaskFinisTime.put(task,finishTimeOfTask);
        }

        this.resourceAvailability =reourceAvailability;
        this.TaskFinisTime=TaskFinisTime;
    }


    public double calculateMaxFinishTimeOfParent(List<Task> parentList, Map<Task,Double> taskFinishTimes){

        double finishTimeMax=0;
        if(parentList.size()>0){
            for (int i = 0; i <parentList.size() ; i++) {
                finishTimeMax = Math.max(finishTimeMax,taskFinishTimes.get(parentList.get(i)));
            }

            return finishTimeMax;
        }
        return 0.0;
    }




    @Override
    public String toString() {
       String info = "Chromosome \n ";
        for (int i = 0; i <this.genes.size() ; i++) {
            Gene gene = this.genes.get(i);
            info+="Task Id "+gene.task.getCloudletId()+" "+" VMID :"+gene.vm.getId()+"\n";
        }
        return info+"Fitness: "+this.fitness+"\n";
    }
}
