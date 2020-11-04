package org.research.daga.operations;


import org.research.daga.Gene;
import org.research.daga.GeneticAlgorithm;
import org.research.daga.SchedulingChromosome;

import java.util.ArrayList;
import java.util.List;

public class SinglePointCrossOver implements CrossoverPolicy{
    @Override
    public ChromosomePair crossover(SchedulingChromosome var1, SchedulingChromosome var2) {

        int length = var1.getNumberOfGenes();
        if (length != var2.getNumberOfGenes()) {
            System.out.println("Genes don't match");
            return null;
        } else {
            List<Gene> parent1Rep = var1.genes;
            List<Gene> parent2Rep = var2.genes;
            List<Gene> child1Rep = new ArrayList(length);
            List<Gene> child2Rep = new ArrayList(length);
            int crossoverIndex;
            if (length==2) {
                crossoverIndex  = 1 ;
            }else {
                crossoverIndex  = 1 + GeneticAlgorithm.getRandomGenerator().nextInt(length - 2);
            }

            int i;
            for(i = 0; i < crossoverIndex; ++i) {
                child1Rep.add(parent1Rep.get(i));
                child2Rep.add(parent2Rep.get(i));
            }

            for(i = crossoverIndex; i < length; ++i) {
                child1Rep.add(parent2Rep.get(i));
                child2Rep.add(parent1Rep.get(i));
            }

            return new ChromosomePair(new SchedulingChromosome(child1Rep), new SchedulingChromosome(child2Rep));
        }
    }


}
