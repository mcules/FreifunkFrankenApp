package de.itstall.freifunkfranken.model;

import android.location.Location;

public class AccessPoint {
    private final String name;
    private final boolean online;
    private final double lat;
    private final double lon;
    private int distance;

    public AccessPoint(String name, double lat, double lon, boolean online) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return online;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getDistance() {
        return distance;
    }

    public Location getLocation() {
        Location apLocation = new Location("");
        apLocation.setLatitude(getLat());
        apLocation.setLongitude(getLon());

        return apLocation;
    }

    public void setDistance(Location location) {
        this.distance = (int) this.getLocation().distanceTo(location);
    }
}