package org.research.daga;

import java.util.Random;

public class Test {
    public static void main(String[] args) {

        for (int i = 0; i <100 ; i++) {
            int crossoverIndex =  genrtaeRandom(5,1);
            System.out.println(crossoverIndex);
        }
    }


    public static int genrtaeRandom(int max,int min){

        Random rnd = new Random();
        return min+rnd.nextInt(max-min);
    }
}
