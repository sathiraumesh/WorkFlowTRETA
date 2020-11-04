package org.research.daga;

import org.workflowsim.CondorVM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Operators {

    public static SchedulingChromosome [] selectRandomParents(List<SchedulingChromosome> population){

        int randOne = Helper.getRandomNumberInRange(0,population.size()-1);
        int randTwo = Helper.getRandomNumberInRange(0,population.size()-1);

        while(randOne==randTwo){
            randTwo=Helper.getRandomNumberInRange(0,population.size()-1);
        }

         SchedulingChromosome parentOne = population.get(randOne);
        SchedulingChromosome parentTwo = population.get(randTwo);

        SchedulingChromosome [] parents = new SchedulingChromosome[2];

        parents[0]=parentOne;
        parents[1] =parentTwo;

        return parents;
    }

    public static  SchedulingChromosome [] singlePointCrossOver(SchedulingChromosome individualOne,SchedulingChromosome individualTwo){

        SchedulingChromosome [] offSprings = new SchedulingChromosome[2] ;
        int cuttingPoint;
        if(individualOne.genes.size()==2){
            cuttingPoint=1;
        }else{
                cuttingPoint = Helper.getRandomNumberInRange(1,individualOne.genes.size()-1);
        }



        List[] cutListOne  = singleGeneCut(cuttingPoint,individualOne.genes);
        List [] cutListTwo  = singleGeneCut(cuttingPoint,individualTwo.genes);

        ArrayList<Gene> firstChildChromosome= new ArrayList<>() ;

        firstChildChromosome.addAll((ArrayList<Gene>) cutListOne[0]);
        firstChildChromosome.addAll((ArrayList<Gene>)cutListTwo[1]);


        ArrayList<Gene> secondChildChromosome =  new ArrayList<>();

        secondChildChromosome.addAll((ArrayList<Gene>) cutListTwo[0]) ;
        secondChildChromosome.addAll((ArrayList<Gene>)cutListOne[1]);

        SchedulingChromosome childOne = new SchedulingChromosome(firstChildChromosome);
        SchedulingChromosome secondChild = new SchedulingChromosome(secondChildChromosome);

        offSprings[0] = childOne;
        offSprings[1] = secondChild;

        return  offSprings;

    }


    public static List [] singleGeneCut(int cuttingIndex,List<Gene> chromsome){


        List [] chromosomeHalves = new List[2];
        ArrayList<Gene> firstHalf = new ArrayList<>();
        ArrayList<Gene> secondHalf = new ArrayList<>();


        for (int i = 0; i <chromsome.size() ; i++) {
            if(i<cuttingIndex){
                firstHalf.add(chromsome.get(i));
            }
            if(i>=cuttingIndex){
                secondHalf.add(chromsome.get(i));

            }

        }
        chromosomeHalves[0] = firstHalf;
        chromosomeHalves[1] =secondHalf;

        return  chromosomeHalves;
    }

    public static void Mutate(SchedulingChromosome induvidual,List<CondorVM> vmList){

        int mutaionIndex [] = new int[induvidual.genes.size()];

        for (int i = 0; i <mutaionIndex.length ; i++) {
            Random generator = new Random();
            double number = generator.nextDouble()*1;
            if(number<0.5){
                mutaionIndex[i]= 1;
            }
            else {
                mutaionIndex[i]=0;
            }
        }

        for (int i = 0; i <mutaionIndex.length ; i++) {

            if(mutaionIndex[i]==1){

                while (true){
                    int randomInt = Helper.getRandomNumberInRange(0,vmList.size()-1);

                    if( induvidual.genes.get(i).vm.getId() ==vmList.get(randomInt).getId()){
                        continue;
                    }else{
                        induvidual.genes.get(i).vm =vmList.get(randomInt);

                        break;
                    }

                }

            }

        }
    }


    public static List<SchedulingChromosome>  selectNexPopoulation(  List<SchedulingChromosome> population ,List<SchedulingChromosome> populationChildren ){

        int initialPopSize = population.size();
        List<SchedulingChromosome> nextGenration = new ArrayList<>();
        nextGenration.addAll(population);

        for (int i = 0; i <populationChildren.size() ; i++) {
            nextGenration.add(populationChildren.get(i));
        }

        for (int i = 0; i <population.size() ; i++) {
            nextGenration.get(i).updateTaskFinishTimes();
            nextGenration.get(i).costFunction();
        }


        Collections.sort(nextGenration);

        List<SchedulingChromosome>  p= new ArrayList<>();
        for (int i = 0; i < initialPopSize; i++) {
            p.add(nextGenration.get(i));
        }

        return p;

    }
}
