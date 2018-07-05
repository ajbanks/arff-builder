package footballstats;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;

/**
 *
 * @author natha
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
//        System.out.println("Working Directory = " +
//             System.getProperty("user.dir"));
          String[] actions = {"tackle", "pass", "inaccurateP", "dribble", "dribble2"};
          String[] tagIDs = {"0B3A", "921E", "59AD"};
//        mergeOutputs(actions);
//        for (String action : actions) {
//              PosLogToArff pos = new PosLogToArff();
//              pos.readFile("06052018", action, tagIDs, false, true);
//              //String filePath = action + "_output.txt";
//
//        }
        //ARFFBuilder arff = new ARFFBuilder("combined_output.txt", false, tagIDs.length);
        //arff.createArffFile();
        //Experiments.runExperiments();
        /*for (String action : actions) {
              PosLogToArff pos = new PosLogToArff();
              pos.readFile("06052018", action, tagIDs, true, true);
              //String filePath = action + "_output.txt";

        }*/
         ARFFBuilder arff = new ARFFBuilder("combined_output.txt", false, tagIDs.length);
         arff.createArffFile(true);
        // Experiments.runExperiments();
    }

    static void mergeOutputs(String[] actions) {
        String firstFile = actions[0] + "_output.txt";
        String destination = "combined_output.txt";


        try (FileWriter fw = new FileWriter(destination, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (String action : actions) {
                String filePath = action + "_output.txt";
                FileReader fr = new FileReader(filePath);
                BufferedReader br = new BufferedReader(fr);

                String line = "";
                while ((line = br.readLine()) != null) {
                    out.println(line);
                }
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }


    }



    }
    

