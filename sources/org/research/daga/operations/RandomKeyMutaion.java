package org.research.daga.operations;

import org.research.daga.GeneticAlgorithm;
import org.research.daga.SchedulingChromosome;
import org.research.daga.SchedulingPopulation;
import org.workflowsim.CondorVM;

import java.util.List;

public class RandomKeyMutaion implements MutationPolicy {
    @Override
    public SchedulingChromosome mutate(SchedulingChromosome var1, List<CondorVM> vmListSize) {

        int rnd = GeneticAlgorithm.getRandomGenerator().nextInt(vmListSize.size()-0);
        int rndPosition =  GeneticAlgorithm.getRandomGenerator().nextInt(var1.genes.size()-0);
        var1.genes.get(rndPosition).vm = vmListSize.get(rnd);
        return null;
    }
}
