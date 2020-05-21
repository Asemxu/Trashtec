package com.labawsrh.aws.trashtec.Models;

public class ScreenItem {

    public String Title,Description;
    public String ScreenImg;
    public int posicion_actual;

    public ScreenItem(){

    }
    public ScreenItem(String title, String description, String screenImg) {
        Title = title;
        Description = description;
        ScreenImg = screenImg;
    }

    public void set_Title(String title) {
        Title = title;
    }

    public void set_Description(String description) {
        Description = description;
    }

    public void set_ScreenImg(String screenImg) {
        ScreenImg = screenImg;
    }

    public String get_Title() {
        return Title;
    }

    public String get_Description() {
        return Description;
    }

    public String get_ScreenImg() {
        return ScreenImg;
    }
}
