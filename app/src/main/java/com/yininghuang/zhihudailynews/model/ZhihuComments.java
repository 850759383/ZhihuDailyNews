package com.yininghuang.zhihudailynews.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yining Huang on 2016/10/20.
 */

public class ZhihuComments {

    private int count;
    private List<ZhihuComment> comments;

    public List<ZhihuComment> getComments() {
        return comments;
    }

    public void setComments(List<ZhihuComment> comments) {
        this.comments = comments;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class ZhihuComment {

        /**
         * author : Xiaole说
         * id : 545721
         * content : 就吃了个花生米，呵呵
         * likes : 0
         * time : 1413600071
         * avatar : http://pic1.zhimg.com/c41f035ab_im.jpg
         */

        private String author;
        private int id;
        private String content;
        private int likes;
        private int time;
        private String avatar;
        @SerializedName("reply_to")
        private ReplyTo replyTo;

        public ReplyTo getReplyTo() {
            return replyTo;
        }

        public void setReplyTo(ReplyTo replyTo) {
            this.replyTo = replyTo;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class ReplyTo {

        /**
         * content : 安卓系统才是关键
         * status : 0
         * id : 26937002
         * author : 一个人挺好的的
         */

        private String content;
        private int status;
        private int id;
        private String author;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}
