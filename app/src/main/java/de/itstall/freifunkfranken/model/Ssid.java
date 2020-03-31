package de.itstall.freifunkfranken.model;

public class Ssid {
    private String ssid;
    private String key;

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
