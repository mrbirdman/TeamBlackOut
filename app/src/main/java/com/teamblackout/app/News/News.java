package com.teamblackout.app.News;


    /* static final String KEY_NAME = "Name";
    static final String KEY_ICON = "ThumbnailUrl";
    static final String KEY_MD5 = "MD5";
    static final String KEY_DOWNLOAD = "Url";
    static final String KEY_DATE = "Date";
    static final String KEY_PREVIEW = "Preview";
    static final String KEY_DESCRIPTION = "Desc"; */

public class News {
    public String title;
    public String date;
    public String description;

    public News(){
        super();
    }

    public News(String title, String date, String description) {
        super();
        this.title = title;
        this.date = date;
        this.description = description;

    }
}
