package org.research.ga;

import java.util.Random;

public class Helper {

    public static Random rnd = new Random();

    public static int genrtaeRandom(int max,int min){

        rnd = new Random();
        return min+rnd.nextInt(max-min);
    }
}
