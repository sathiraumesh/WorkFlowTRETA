package org.research.ga;

import org.workflowsim.CondorVM;
import org.workflowsim.Task;

import java.util.ArrayList;
import java.util.List;

public class Gene {

    private CondorVM vm;

    private Task task;

    private double cost ;

    public  Gene(CondorVM vm,Task task){

        this.task=task;
        this.vm =vm;

    }

    public CondorVM getVm() {
        return vm;
    }

    public Task getTask() {
        return task;
    }

    public void setVm(CondorVM vm) {
        this.vm = vm;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public static List<Gene> createGenlist(List<CondorVM> vms,List<Task> taskList){

        int vmSize = vms.size();
        int taskListSize = taskList.size();

        List<Gene> genes = new ArrayList<>();

        for (int i = 0; i <taskListSize ; i++) {
            CondorVM vm = vms.get(Helper.genrtaeRandom(vmSize,0));
            Task task = taskList.get(Helper.genrtaeRandom(taskListSize,0));
            genes.add(new Gene(vm,task));
        }


        return genes;

    }

    @Override
    public String toString() {
        return "TASK ID : " +this.task.getCloudletId()+" VM ID:"+this.vm.getId();
    }
}
