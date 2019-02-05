package footballstats;

public class RealTimeProcessing {
    PosLogToArff pos;
    String logfile;
    double lastEndTime;

    public RealTimeProcessing (PosLogToArff pos, String logfile, double positionWindow){
        this.logfile = logfile;
        this.pos = pos;
        this.lastEndTime = 0.0;

    }


    public void beginProcessing(){}

    public void createPositionImage(double [] positions){}

    public void getNextSetOfPositions(double positionWindow){
    }

    public void changeLastEndTime(double time){
        this.lastEndTime = time;
    }

    public void resetEndTime(){
        this.lastEndTime = 0.0;
    }

    public void readLogFromLastTime(){

    }
}
