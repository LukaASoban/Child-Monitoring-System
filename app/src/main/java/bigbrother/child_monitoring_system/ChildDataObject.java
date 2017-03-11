package bigbrother.child_monitoring_system;

import java.io.Serializable;

/**
 * Created by Luka on 2/15/2017.
 */

public class ChildDataObject implements Serializable {

    private String name;
    private String macAddress;
    private String locationMAC;
    private String timestamp;

    public ChildDataObject () {

    }
    public ChildDataObject (String name, String macAddress) {

        this.name = name;
        this.macAddress = macAddress;
    }

    public ChildDataObject(String name, String macAddress, String locationMAC, String timestamp) {
        this.name = name;
        this.macAddress = macAddress;
        this.locationMAC = locationMAC;
        this.timestamp = timestamp;
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

    public String getLocationMAC() {
        return locationMAC;
    }

    public void setLocationMAC(String locationMAC) {
        this.locationMAC = locationMAC;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
