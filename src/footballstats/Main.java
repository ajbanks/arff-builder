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
        //  String[] actions = {"tackle", "pass", "inaccurateP", "dribble", "dribble2"};
        String[] actions = {"tackle2"};
     //     String[] tagIDs = {"0B3A", "921E", "59AD"};
//921e = ball
        // these are the arrays for the second logfile / set of data
        String[] tagIDs = {"41AA", "88B4", "CC03"};  //41aa = ball
       // String[] actions = {"pass2", "inaccurateP2"};
        ARFFBuilder arff = new ARFFBuilder("tackle2_output.txtPossesion.txt", false, tagIDs.length);
        for (String action : actions) {
              PosLogToArff pos = new PosLogToArff();
               pos.readFile("06052018", action, tagIDs, false, true);
//              //String filePath = action + "_output.txt";
           LogSummarisation summarisation = new LogSummarisation(tagIDs, 0.5, arff);
            summarisation.readOuputFile(action + "_output.txt");
//
       }
       //   mergeOutputs(actions, "_output.txtPossesion2.txt");

     //    arff.createArffFile(false);
        // Experiments.runExperiments();;
      //  LogSummarisation summarisation = new LogSummarisation(tagIDs, 1.40, arff);
       // summarisation.readOuputFile("pass_output.txt");

    }

    static void mergeOutputs(String[] actions, String output) {
        String firstFile = actions[0] + output;
        String destination = "newcombinedPossesion2_output.txt";


        try (FileWriter fw = new FileWriter(destination, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (String action : actions) {
                String filePath = action + output;
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
    

