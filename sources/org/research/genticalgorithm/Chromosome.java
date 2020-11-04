package org.research.genticalgorithm;

import java.util.List;

public abstract class Chromosome<T> {

    // properties of chromosome
    protected double fitness = 1.0D/0.0;

    protected List<T> genes;





    public Chromosome(List<T> genes){

        this.genes = genes;
    }

     // methods for chromsome

    public abstract double getFitness();

    public abstract int getNumberOfGenes();

    public abstract List<T>  getGenes();

    public abstract double costFunction();


}
