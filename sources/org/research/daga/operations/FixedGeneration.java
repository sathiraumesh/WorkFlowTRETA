package org.research.daga.operations;

import org.apache.commons.math3.genetics.Population;
import org.research.daga.SchedulingPopulation;

public class FixedGeneration implements StoppingCondition {

    private int numGenerations = 0;
    private final int maxGenerations;

    public FixedGeneration(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }



    @Override
    public boolean isSatisfied(SchedulingPopulation var1) {

        if (this.numGenerations < this.maxGenerations) {
            ++this.numGenerations;
            return false;
        } else {
            return true;
        }
    }
}
