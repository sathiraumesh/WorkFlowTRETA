package org.research.daga.operations;

import org.apache.commons.math3.genetics.Population;
import org.research.daga.SchedulingPopulation;

public interface StoppingCondition {
    boolean isSatisfied(SchedulingPopulation var1);
}

