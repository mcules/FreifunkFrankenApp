package de.itstall.freifunkfranken.model;

public class News {
    private String title;
    private String date;
    private String description;

    public News(String title, String date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}
