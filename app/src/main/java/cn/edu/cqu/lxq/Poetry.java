package cn.edu.cqu.lxq;


import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class Poetry extends LitePalSupport {
    @SerializedName("title_c")
    private String chineseTitle;
    @SerializedName("title_e")
    private String englishTitle;
    @SerializedName("audioid")
    private String audioId;
     private String author;
     private String content;
    private boolean collect = false;

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public String getChineseTitle() {
        return chineseTitle;
    }

    public String getEnglishTitle() {
        return englishTitle;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getAudioId() { return audioId; }
}
