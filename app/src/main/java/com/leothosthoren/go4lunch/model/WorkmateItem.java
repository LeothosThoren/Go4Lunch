package com.leothosthoren.go4lunch.model;

public class WorkmateItem {

    private String workmateName;
    private String url;

    public WorkmateItem(String workmateName, String url) {
        this.workmateName = workmateName;
        this.url = url;
    }

    public String getWorkmateName() {
        return workmateName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWorkmateName(String workmateName) {
        this.workmateName = workmateName;
    }
}
