package com.yininghuang.zhihudailynews.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class ZhihuLatestNews {

    private String date;

    private List<ZhihuStory> stories;

    @SerializedName("top_stories")
    private List<ZhihuTopStory> topStories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ZhihuStory> getStories() {
        return stories;
    }

    public void setStories(List<ZhihuStory> stories) {
        this.stories = stories;
    }

    public List<ZhihuTopStory> getTopStories() {
        return topStories;
    }

    public void setTopStories(List<ZhihuTopStory> topStories) {
        this.topStories = topStories;
    }

    public class ZhihuStory {

        private String title;

        private List<String> images;

        private int type;

        private int id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public class ZhihuTopStory {

        private String title;

        private String image;

        private int type;

        private int id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
