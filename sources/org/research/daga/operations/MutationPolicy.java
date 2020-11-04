package org.research.daga.operations;

import org.apache.commons.math3.genetics.Chromosome;
import org.research.daga.SchedulingChromosome;
import org.research.daga.SchedulingPopulation;
import org.workflowsim.CondorVM;

import java.util.List;

public interface MutationPolicy {
    SchedulingChromosome mutate(SchedulingChromosome var1, List<CondorVM> vmList);
}
