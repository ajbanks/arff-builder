package footballstats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RealTimeProcessing {
    PosLogToArff pos;
    String logfile;
    double lastEndTime;
    ArrayList<Integer> classifications;
    Boolean classifyActions;
    double timeSinceLastPosition;
    long positionWindow;
    String [] tagids;

    public RealTimeProcessing (PosLogToArff pos, String[] tagids, String logfile, long positionWindow) throws IOException {
        this.logfile = logfile;
        this.pos = pos;
        this.lastEndTime = 0.0;
        this.classifications = new ArrayList<Integer>();
        this.classifyActions = true;
        this.timeSinceLastPosition = 0.0;
        this.positionWindow = positionWindow;
        this.tagids = new String[] {"41AA", "88B4", "CC03"};//tagids;
        liveClassification();
    }


    public void beginProcessing() throws InterruptedException {
        while (classifyActions = true) {

            int classification = getClassificationOfPosition(getNextSetOfPositions(0.0, "file"));
            classifications.add(classification);
            System.out.println(classification);
            lastEndTime = lastEndTime + positionWindow;
            timeSinceLastPosition = 0.0;

            TimeUnit.SECONDS.sleep(positionWindow);

        }
    }

    public double[][] createPositionImage(double [] positions){
        double [][] image = {{0.0},{0.0}};
        return image;
    }

    public double [] getNextSetOfPositions(double positionWindow, String logfile){
        double [] positions = {0.0, 0.1, 0.2, 0.3} ;
        return positions;
    }

    public void changeLastEndTime(double time){
        this.lastEndTime = time;
    }

    public void resetEndTime(){
        this.lastEndTime = 0.0;
    }

    public int getClassificationOfPosition(double [] positions){
        double [][] image = createPositionImage(positions);
        int classification = 0;
        return classification;
    }

    public void inputActionTimeInLog(String logfile){

    }

    public void readLogFromLastTime(){

    }

    public void liveClassification() throws IOException {
        //String[] classNames = {"Tackle", "Dribble", "Pass"};
        String[] classNames = {"Dribble" , "Pass", "Tackle"}; // redone class names after conversion to three training classes
        //10 = 1 seconds and 1 = 1 milisecond i.e. 23 = 2.3 seconds, 103 = 10.3 seconds, 123 = 12.3 seconds 1352 = 135.2 secondds
        //max time in log file is 10000 which is 1000 seconds which is 16.67 minutes
        int maxTimeInLogFile = 12805;
        //maximum time an action can take is 50 which is 5 seconds
        int maxTimeWindow = 40;
        //loop from 0 miliseconds to max duration of log file
        for (double currentTime = 0; currentTime < maxTimeInLogFile; currentTime++){
            double[] probabilities = new double[maxTimeWindow*10];
            int[] classes = new int[maxTimeWindow*10];

            //loop from current time to the maximum time an action can take
            int count = 0;
            for (double j = currentTime + 1; j < currentTime+maxTimeWindow; j+= 1){
                if (j == 30)
                    System.out.println();
                //classify this time window as an action
                double[] times = {currentTime / 10, j / 10};
                pos = new PosLogToArff();
                pos.actionTimes = times;
                //create image

                boolean result = pos.generateActionPositionsForLiveClassification("logs/100219/100219(2).txt", "actions_100219" ,tagids, false, true);
                //if it didnt find any positions then go to the next times
                if (!result)
                    return;
                //classify image and get result

                Process p = Runtime.getRuntime().exec("py -3.6 kera_cnn_transferlearning.py");
                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(p.getInputStream()));
                //0 = tackle, 1 = pass 2 = dribble
                String output = stdInput.readLine();
                output = stdInput.readLine();
                //String output = "2,[0.111 0.99 0.12]";

                //get class classifier thinks it most likely is
                //this way
                int windowMaxIndex = Integer.parseInt(output.substring(0, output.indexOf(",")));
                String[] classificationValuesTemp = output.substring(3,output.length()-1).split(" ");
                String[] classificationValues = new String[4];
                count = 0;
                for (int i = 0; i < classificationValuesTemp.length; i++){
                    if (!classificationValuesTemp[i].trim().isEmpty()){
                        classificationValues[count] = classificationValuesTemp[i];
                        count++;
                    }
                }
                double windowMaxProb = Double.parseDouble(classificationValues[windowMaxIndex].trim());

//                Random rand = new Random();
//                windowMaxIndex = rand.nextInt(3) + 0;
//                windowMaxProb = (rand.nextInt(100) + 0) / (double)100;

                //or alternatively this way
//                output = output.substring(3,output.length());
//                String[] classificationValues = output.split(" ");
//                int windowMaxIndex = 0;
//                double windowMaxProb = Double.parseDouble(classificationValues[0]);
//
//                for (int k = 1; k < classificationValues.length; k++){
//                    if (Double.parseDouble(classificationValues[k]) > windowMaxProb){
//                        windowMaxProb = Double.parseDouble(classificationValues[k]);
//                        windowMaxIndex = k;
//                    }
//                }
                //System.out.println("Times enetered loop:" + count + "max prob: " + windowMaxProb);
                double[] probAndClass = {windowMaxIndex, windowMaxProb};
                classes[count] = (int)probAndClass[0];
                probabilities[count] = probAndClass[1];
                count++;
            }

            //get highest classification probability
            double maxProb = probabilities[0];
            int maxIndex = 0;
            for (int j = 1; j < probabilities.length; j++){
                System.out.print("prediction:  " + classes[j] + "  " + classNames[classes[j]] + " \n");
                if (probabilities[j] > maxProb){
                    maxProb = probabilities[j];
                    maxIndex = j;

                }
            }

            //set currentTime to end  of image with the highest probability score
            double newTime = currentTime + (maxIndex) + 1;
//            for(int i  : classes){
//                System.out.println("Time (secs): " + (currentTime /10) + " to " + (newTime/10) + ". Classified as: " + classNames[classes[i]] );
//            }
            System.out.println("Time (secs): " + (currentTime /10) + " to " + (newTime/10) + ". Classified as: " + classNames[classes[maxIndex]] + " Max Prob: " + maxProb);
            //System.out.println("Time (secs): " + (currentTime /10) + " to " + (newTime/10) + ". Classified as: " + classNames[0] + " Max Prob: " + maxProb);
            currentTime = newTime;

        }
    }


}
