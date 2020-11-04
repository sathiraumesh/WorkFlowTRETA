package org.research.genticalgorithm;

import java.util.List;

public abstract  class Population<T> {

    public List<Chromosome> population;

    public Population(List<Chromosome> population){

        this.population = population;
    }


    public abstract  int getPopulationSize();

    public abstract List<Chromosome> getPopulation();

    public abstract void addChromosome(Chromosome e);

    public abstract Chromosome getFittestChromosme();

    public abstract Population<T> nextGenration();

}
