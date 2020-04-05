package de.itstall.freifunkfranken.model;

public class Ssid {
    private final String ssid;
    private final String key;

    public Ssid(String ssid) {
        this.ssid = ssid;
        this.key = "";
    }

// --Commented out by Inspection START (05.04.2020 22:13):
//    public Ssid(String ssid, String key) {
//        this.ssid = ssid;
//        this.key = key;
//    }
// --Commented out by Inspection STOP (05.04.2020 22:13)

    public String getSsid() {
        return ssid;
    }

    public String getKey() {
        return key;
    }
}
