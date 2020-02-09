package bots;

public class Utils {
    private boolean goTime;
    private boolean lastGoTime;
    private boolean sentToSecond;

    public Utils(){
        this.goTime=false;
        this.sentToSecond =false;
    }

    public boolean getTime(){
        return goTime;
    }

    public void setTime(boolean Time){
        this.lastGoTime=goTime;
        this.goTime=Time;
    }

    public boolean getLastTime(){
        return lastGoTime;
    }

    public boolean getSentToSecond() {
        return sentToSecond;
    }

    public void setSentToSecond(boolean sentToSecond) {
        this.sentToSecond = sentToSecond;
    }
}