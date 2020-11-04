package org.research.realgeneticalgroithm;

import org.research.daga.Gene;
import org.research.genticalgorithm.Chromosome;

import java.util.List;

public class RealChromosome extends Chromosome<Double> implements Comparable<Chromosome<Double>>{

    public RealChromosome(List<Double> genes) {
        super(genes);
    }

    @Override
    public double getFitness() {
        this.fitness = costFunction();
        return this.fitness ;
    }

    @Override
    public int getNumberOfGenes() {
        return this.genes.size();
    }

    @Override
    public List<Double> getGenes() {
        return this.genes;
    }

    @Override
    public double costFunction() {

        double sum = 0;
        for (double val : this.genes) {

            sum =sum+val;
        }
        // number of elements >= 0
        return sum;
    }


    @Override
    public int compareTo(Chromosome<Double> o) {
       return Double.compare(this.getFitness(),o.getFitness());
    }

}
