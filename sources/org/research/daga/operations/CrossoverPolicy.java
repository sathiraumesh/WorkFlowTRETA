package org.research.daga.operations;


import org.research.daga.SchedulingChromosome;

public interface CrossoverPolicy {
        ChromosomePair crossover(SchedulingChromosome var1, SchedulingChromosome var2) ;
    }

