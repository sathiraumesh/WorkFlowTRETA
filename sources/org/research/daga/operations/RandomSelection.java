package org.research.daga.operations;

import org.apache.commons.math3.random.RandomGenerator;
import org.research.daga.GeneticAlgorithm;
import org.research.daga.SchedulingChromosome;
import org.research.daga.SchedulingPopulation;

public class RandomSelection implements SelectionPolicy {
    @Override
    public ChromosomePair select(SchedulingPopulation var1) {

        RandomGenerator rnd = GeneticAlgorithm.getRandomGenerator();
        int pr1 = rnd.nextInt(var1.getPopulationSize()-0);
        int pr2 = rnd.nextInt(var1.getPopulationSize()-0);

        return new ChromosomePair(var1.getPopulation().get(pr1),var1.getPopulation().get(pr1));
    }
}
