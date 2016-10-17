package com.yininghuang.zhihudailynews.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class ZhihuNewsContent {

    /**
     * body : html
     * image_source : Yestone.com 版权图片库
     * title : 食物中的色彩秘密，悄悄地影响着我们的食欲与健康
     * image : http://pic2.zhimg.com/bc5e570d8917680f1bfa11280e4a93e9.jpg
     * share_url : http://daily.zhihu.com/story/8890064
     * js : []
     * ga_prefix : 101618
     * images : ["http://pic2.zhimg.com/ab651803a3310a7f0f2302332dd97775.jpg"]
     * type : 0
     * id : 8890064
     * css : ["http://news-at.zhihu.com/css/news_qa.auto.css?v=4b3e3"]
     */

    private String body;
    private String title;
    private int type;
    private int id;
    private List<?> js;
    private List<String> images;
    private List<String> css;
    private String image;

    @SerializedName("image_source")
    private String imageSource;

    @SerializedName("share_url")
    private String shareUrl;

    @SerializedName("theme_name")
    private String themeName;

    @SerializedName("theme_id")
    private int themeId;

    @SerializedName("editor_name")
    private String editorName;

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

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

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
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

    public List<?> getJs() {
        return js;
    }

    public void setJs(List<?> js) {
        this.js = js;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }
}
