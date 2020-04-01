package de.itstall.freifunkfranken.model;

import android.location.Location;

public class AccessPoint {
    private String name;
    private boolean online;
    private double lat;
    private double lon;
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

    public int getDistance(double gpsLat, double gpsLon) {
        float[] distance = new float[1];
        Location.distanceBetween(gpsLat, gpsLon, this.getLat(), this.getLon(), distance);

        return (int) distance[0];
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