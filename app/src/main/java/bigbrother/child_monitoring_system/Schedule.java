package bigbrother.child_monitoring_system;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mgb51_000 on 3/10/2017.
 */

public class Schedule implements Serializable{
    private ArrayList<ScheduleDataObject> schedule;

    public void setSchedule(ArrayList<ScheduleDataObject> schedule) {
        this.schedule = schedule;
    }

    public ArrayList<ScheduleDataObject> getSchedule() {
        return schedule;
    }
}
