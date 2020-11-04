package org.research.daga;

import org.research.genticalgorithm.Chromosome;
import org.research.genticalgorithm.Population;
import org.workflowsim.CondorVM;
import org.workflowsim.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchedulingPopulation {

    public List<SchedulingChromosome> population ;
    private int populationlimit;
    private double eltismRate;

    public SchedulingPopulation(List<SchedulingChromosome> population ){
        this.population=population;


    }

    public SchedulingPopulation(int populationlimit){
        this.populationlimit=populationlimit;
        this.population = new ArrayList<>();
    }

    public  int getPopulationSize(){

        return this.population.size();
    }

    public SchedulingPopulation nextGenration (){

        SchedulingPopulation nextGenration = new SchedulingPopulation(50);
        int bound = nextGenration.populationlimit;
        List<SchedulingChromosome> oldChromsoms =  this.getPopulation();
        Collections.sort(oldChromsoms);
        for (int i = 0; i <bound ; i++) {
            nextGenration.addChromosne(oldChromsoms.get(i));

        }

        return nextGenration;
    }

    public List<SchedulingChromosome> getPopulation(){
        return this.population;
    }


    public void addChromosne (SchedulingChromosome c1){
        this.population.add(c1);
    }


    public  static   SchedulingChromosome createChromosome(List<Task> taskList, List<CondorVM> vmList){
        List<Gene> genes = new ArrayList<>();


        for (int i = 0; i <taskList.size() ; i++) {
            Task task =  taskList.get(i);
            CondorVM vm = vmList.get(Helper.getRandomNumberInRange(0,vmList.size()-1));
            genes.add(new Gene(task,vm));
        }

        return new SchedulingChromosome(genes);
    }


    public static List<SchedulingChromosome> createRandomChromosomesList(List<Task> taskList, List<CondorVM> vmList, int pop){
        List<SchedulingChromosome> chromosomes = new ArrayList<>();

        for (int i = 0; i <pop ; i++) {
            chromosomes.add(createChromosome(taskList,vmList));
        }

        return chromosomes;
    }


}
