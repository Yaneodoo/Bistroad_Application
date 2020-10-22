package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Photo implements Serializable {
    private String sourceUrl;
    private String thumbnailUrl;

    public Photo(String sourceUrl, String thumbnailUrl) {
        this.sourceUrl = sourceUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
