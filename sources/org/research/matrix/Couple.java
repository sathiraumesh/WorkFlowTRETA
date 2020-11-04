package org.research.matrix;

import org.workflowsim.CondorVM;
import org.workflowsim.Task;

public class Couple implements Comparable<Couple> {

    public double cost ;

    public CondorVM vm;

    public Task task;

    public double transferTime;

    public double cpuTime ;

    public double finsihTime ;

    public double startTime ;

    public boolean choosen = false;


    public Couple (CondorVM vm,Task task){
        this.vm =vm;
        this.task =task;
        this.cost = task.getCloudletLength()/vm.getCurrentRequestedTotalMips();
        this.cpuTime=cost;


    }
    @Override
    public int compareTo(Couple o) {
        return Double.compare(this.cost,o.cost);
    }

    public void setEarliestStartTime(double add ){
        if(!choosen){
            cost=cost+add;
            this.startTime=add;
        }

    }

    public void setTaransferTime(double add){
        cost+=add;
        this.transferTime+=add;
    }

    public void setFinishTime(){
        this.finsihTime = cpuTime+this.transferTime+startTime;
    }
}
