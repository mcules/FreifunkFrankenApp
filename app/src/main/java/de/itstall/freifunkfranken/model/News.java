package de.itstall.freifunkfranken.model;

public class News {
    private final String title;
    private final String date;
    private final String description;
    private final String link;

    public News(String title, String date, String description, String link) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.link = link;
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

    public String getLink() {
        return link;
    }
}
