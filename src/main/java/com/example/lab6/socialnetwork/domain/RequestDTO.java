package com.example.lab6.socialnetwork.domain;

public class RequestDTO {

    private String from;
    private String status;
    private String date;

    public RequestDTO(String from, String status, String date) {
        this.from = from;
        this.status = status;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String username) {
        this.from = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
                "username='" + from + '\'' +
                ", status='" + status + '\'' +
                ", data='" + date + '\'' +
                '}';
    }
}
