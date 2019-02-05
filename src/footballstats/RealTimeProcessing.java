package footballstats;

import java.util.ArrayList;

public class RealTimeProcessing {
    PosLogToArff pos;
    String logfile;
    double lastEndTime;
    ArrayList<Integer> classifications;
    Boolean classifyActions;
    double timeSinceLastPosition;
    double positionWindow;

    public RealTimeProcessing (PosLogToArff pos, String logfile, double positionWindow){
        this.logfile = logfile;
        this.pos = pos;
        this.lastEndTime = 0.0;
        this.classifications = new ArrayList<Integer>();
        this.classifyActions = true;
        this.timeSinceLastPosition = 0.0;
        this.positionWindow = positionWindow;

    }


    public void beginProcessing() {
        while (classifyActions = true) {
            if (timeSinceLastPosition >= positionWindow) {
                int classification = getClassificationOfPosition(getNextSetOfPositions(0.0, "file"));
                classifications.add(classification);
                System.out.println(classification);
                lastEndTime = lastEndTime + positionWindow;
                timeSinceLastPosition = 0.0;
            }

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

    public void readLogFromLastTime(){

    }

}
