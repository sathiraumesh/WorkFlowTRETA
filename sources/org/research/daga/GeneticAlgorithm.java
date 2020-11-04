package org.research.daga;





import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.research.daga.operations.*;

public class GeneticAlgorithm {

    private static RandomGenerator randomGenerator = new JDKRandomGenerator();
    private final CrossoverPolicy crossoverPolicy;
    private final double crossoverRate;
    private final MutationPolicy mutationPolicy;
    private final double mutationRate;
    private final SelectionPolicy  selectionPolicy;
    private int generationsEvolved = 0;

    public GeneticAlgorithm(CrossoverPolicy crossoverPolicy, double crossoverRate, MutationPolicy mutationPolicy, double mutationRate, SelectionPolicy selectionPolicy) {
        this.crossoverPolicy = crossoverPolicy;
        this.crossoverRate = crossoverRate;
        this.mutationPolicy = mutationPolicy;
        this.mutationRate = mutationRate;
        this.selectionPolicy = selectionPolicy;
    }

    public static synchronized RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }


    public SchedulingPopulation evolve(SchedulingPopulation initial, StoppingCondition condition) {
        SchedulingPopulation current = initial;

        for(this.generationsEvolved = 0; !condition.isSatisfied(current); ++this.generationsEvolved) {
//            current = this.nextGeneration(current);
      }

        return current;
    }

//    public SchedulingPopulation nextGeneration(SchedulingPopulation current) {
//
//        RandomGenerator rnd = getRandomGenerator();
//        SchedulingPopulation nextGen = current.nextGenration();
//
//        while (nextGen.getPopulationSize()<current.getPopulationSize()){
//
//            ChromosomePair pair = this.getSelectionPolicy().select(current);
//            if (rnd.nextDouble() < this.getCrossoverRate()) {
//                pair = this.getCrossoverPolicy().crossover(pair.getFirst(), pair.getSecond());
//            }
//
//            if (rnd .nextDouble() < this.getMutationRate()) {
//                pair = new ChromosomePair(this.getMutationPolicy().mutate(pair.getFirst()), this.getMutationPolicy().mutate(pair.getSecond()));
//            }
//
//            nextGen.addChromosne(pair.getFirst());
//            if (nextGen.getPopulationSize() < current.getPopulationSize()) {
//                nextGeneration.addChromosome(pair.getSecond());
//            }
//        }
//
//    }

    public CrossoverPolicy getCrossoverPolicy() {
        return crossoverPolicy;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public MutationPolicy getMutationPolicy() {
        return mutationPolicy;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public SelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public int getGenerationsEvolved() {
        return generationsEvolved;
    }
}
