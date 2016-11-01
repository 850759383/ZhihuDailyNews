package com.yininghuang.zhihudailynews.model.db;

/**
 * Created by Yining Huang on 2016/11/1.
 */

public class Favorite {

    private int id;

    private int saveTime;

    private String content;

    public Favorite() {
    }

    public Favorite(int id, int saveTime, String content) {
        this.id = id;
        this.saveTime = saveTime;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(int saveTime) {
        this.saveTime = saveTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
