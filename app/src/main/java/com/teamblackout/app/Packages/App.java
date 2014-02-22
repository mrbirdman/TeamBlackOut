package com.teamblackout.app.Packages;

    /* static final String KEY_NAME = "Name";
    static final String KEY_ICON = "ThumbnailUrl";
    static final String KEY_MD5 = "MD5";
    static final String KEY_DOWNLOAD = "Url";
    static final String KEY_DATE = "Date";
    static final String KEY_PREVIEW = "Preview";
    static final String KEY_DESCRIPTION = "Desc"; */

public class App {
    public String title;
    public String icon;
    public String md5;
    public String download;
    public String date;
    public String preview;
    public String description;
    public String section;

    public App(){
        super();
    }

    public App(String title, String icon, String md5, String download, String date, String preview, String description, String section) {
        super();
        this.title = title;
        this.icon = icon;
        this.md5 = md5;
        this.download = download;
        this.date = date;
        this.preview = preview;
        this.description = description;
        this.section = section;

    }
}