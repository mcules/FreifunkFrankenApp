package de.itstall.freifunkfranken.model;

public class Ssid {
    private final String ssid;
    private final String key;

    public Ssid(String ssid) {
        this.ssid = ssid;
        this.key = "";
    }

    public Ssid(String ssid, String key) {
        this.ssid = ssid;
        this.key = key;
    }

    public String getSsid() {
        return ssid;
    }

    public String getKey() {
        return key;
    }
}
