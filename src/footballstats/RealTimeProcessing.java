package footballstats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    public void liveClassification() throws IOException, IOException {
        double maxTimeInLogFile = 500;
        int maxTimeWindow = 5;
        //loop from 0 miliseconds to max duration of log file
        for (double currentTime = 0; currentTime < maxTimeInLogFile; currentTime++){
            double[] probabilities = new double[maxTimeWindow];
            double[] classes = new double[maxTimeWindow];

            //loop from current time to the maximum time an action can take
            int count = 0;
            for (double j = currentTime; j < maxTimeWindow; j+=0.1){
                //classify this time window as an action
                double[] times = {0.0,0.0};
                times[0] = currentTime;
                times[1] = j;
                pos.actionTimes = times;
                //create image
                pos.readFile("logfilename", "combined_actions_251118" ,tagids, false, true);

                //classify image and get result

                Process p = Runtime.getRuntime().exec("python_cnn_classification/kera_cnn_transferlearning.py");
                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(p.getInputStream()));
                String[] output = stdInput.readLine().split(",");
                double[] probAndClass = new double[2];
                for (int k = 0; k < output.length; k++)
                    probAndClass[k] = Double.parseDouble(output[k]);
                classes[count] = probAndClass[0];
                probabilities[count] = probAndClass[1];
                count++;
            }

            //get highest classification probability
            double maxProb = probabilities[0];
            int maxIndex = 0;
            for (int j = 1; j < probabilities.length; j++){
                if (probabilities[j] > maxProb){
                    maxProb = probabilities[j];
                    maxIndex = j;
                }
            }

            //set currentTime to end  of image with the highest probability score
            double newTime = currentTime + (maxIndex/10);
            System.out.println("Time: " + currentTime + " to " + newTime + ". Classified as: " + classes[maxIndex]);
            currentTime = newTime;

        }
    }


}
