package bigbrother.child_monitoring_system;

import java.io.Serializable;

/**
 * Created by Luka on 2/15/2017.
 */

public class ChildDataObject implements Serializable {

    private String name;
    private String macAddress;

    public ChildDataObject () {

    }
    ChildDataObject (String name, String macAddress) {

        this.name = name;
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getName() {
        return name;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setName(String name) {
        this.name = name;
    }
}
