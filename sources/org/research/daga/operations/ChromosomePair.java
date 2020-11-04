package org.research.daga.operations;

import org.research.daga.SchedulingChromosome;

public class ChromosomePair {

    private final SchedulingChromosome first;
    private final SchedulingChromosome second;

    public ChromosomePair(SchedulingChromosome c1, SchedulingChromosome c2) {
        this.first = c1;
        this.second = c2;
    }

    public SchedulingChromosome getFirst() {
        return this.first;
    }

    public SchedulingChromosome getSecond() {

        return this.second;
    }

    public String toString() {
        return String.format("(%s,%s)", this.getFirst(), this.getSecond());
    }
}
