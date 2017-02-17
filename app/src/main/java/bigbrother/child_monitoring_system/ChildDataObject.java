package bigbrother.child_monitoring_system;

/**
 * Created by Luka on 2/15/2017.
 */

public class ChildDataObject {

    private String name;
    private String macAddress;

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
