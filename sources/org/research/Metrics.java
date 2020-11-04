package org.research;

import org.workflowsim.Job;
import org.workflowsim.Task;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Metrics {


    public static List<Double> data= new ArrayList<>();

    public static double  calculateMakeSpan(List<Job> list) {

        DecimalFormat dft = new DecimalFormat("###.##");
        double makeSpan = 0;
        for (Job job : list) {
            for (Task task : job.getTaskList()) { ;
                makeSpan = Math.max(makeSpan,task.getTaskFinishTime());


            }
        }
        System.out.println(dft.format(makeSpan));
        return makeSpan;

    }


    public static void writeToFile(String fileName, List<Double> data){
        FileWriter writer = null;
        DecimalFormat dft = new DecimalFormat("###.##");
        try{
            writer = new FileWriter(fileName+".txt");
            for(Double str: data) {
                writer.write(dft.format(str) + System.lineSeparator());
            }

            writer.close();
        }catch (Exception e){

        }


    }
}
