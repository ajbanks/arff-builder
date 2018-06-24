package footballstats;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.io.IOException;

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
//              System.getProperty("user.dir"));
        String[] actions ={"tackle", "passes", "innaccurate-pass", "dribble 1", "dribble 2"};
        String[] tagIDs = {"0B3A", "921E", "59AD"} ;
        for (String action : actions) {
            PosLogToArff pos = new PosLogToArff();
            pos.readFile("06052018", action, tagIDs);
            String filePath = action + "_output.txt";
            ARFFBuilder arff = new ARFFBuilder(filePath, false);
            arff.createArffFile();
        }
        //Experiments();
    }
    
}
