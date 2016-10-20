package com.yininghuang.zhihudailynews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yining Huang on 2016/10/20.
 */

public class ZhihuNewsExtra {

    @SerializedName("long_comments")
    private int longComments;
    private int popularity;
    @SerializedName("short_comments")
    private int shortComments;
    private int comments;

    public int getLongComments() {
        return longComments;
    }

    public void setLongComments(int longComments) {
        this.longComments = longComments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getShortComments() {
        return shortComments;
    }

    public void setShortComments(int shortComments) {
        this.shortComments = shortComments;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
