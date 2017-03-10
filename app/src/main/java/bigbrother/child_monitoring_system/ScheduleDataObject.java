package bigbrother.child_monitoring_system;

import java.io.Serializable;

/**
 * Created by mgb51_000 on 3/10/2017.
 */

public class ScheduleDataObject implements Serializable{

    private String name;
    private String macAddress;
    private int start;
    private int end;

    public ScheduleDataObject(String name, String macAddress) {

        this.name = name;
        this.macAddress = macAddress;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public String getMacAddress() { return macAddress; }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
