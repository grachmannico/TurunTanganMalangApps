package com.example.nicko.turuntanganmalangapps.sqlite;

/**
 * Created by nicko on 7/2/2017.
 */

public class NotificationModel {
    private String id, title, body, intent, id_target;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getId_target() {
        return id_target;
    }

    public void setId_target(String id_target) {
        this.id_target = id_target;
    }
}
