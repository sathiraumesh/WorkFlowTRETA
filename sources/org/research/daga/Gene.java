package org.research.daga;

import org.workflowsim.CondorVM;
import org.workflowsim.Task;

public class Gene implements Comparable<Gene> {

    public CondorVM vm ;

    public Task task;

    public double startTimeOfTask=0;

    public double finishTimeOfTask=0;

    public Gene(Task task,CondorVM vm ){

        this.task=task;
        this.vm=vm;

    }

    @Override
    public int compareTo(Gene o) {
        return Double.compare(this.startTimeOfTask,o.startTimeOfTask);
    }
}
