package org.research.ga;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Chromosome {

    private List<Gene> genes;

    private double cost ;

    public Chromosome(List<Gene> genes){
        this.genes =genes;
        this.cost=0;
    }

    public List<Gene> getGenes() {
        return genes;
    }

    public double getCost() {
        return cost;
    }


    public void setGenes(List<Gene> genes) {
        this.genes = genes;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public static Chromosome createChromosome(List<Gene> genes){

        return  new Chromosome(genes);
    }

    public void processSchedule(){

        List<Gene> schedule = new ArrayList<>(this.genes);
        List<Gene> processedList= new ArrayList<>();


        while (!schedule.isEmpty()){

            Gene gene = schedule.get(0);

            processedList.add(gene);
            schedule.remove(gene);

        }

    }

    @Override
    public String toString() {
        String info ="Chromosome\n";
        for (int i = 0; i <this.genes.size() ; i++) {
          Gene gene = this.genes.get(i);
          info+="GENE :"+i+" "+gene+"\n";
        }
        return info;
    }
}
