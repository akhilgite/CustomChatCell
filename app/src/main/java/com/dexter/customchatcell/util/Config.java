package com.dexter.customchatcell.util;

import android.content.Context;

/**
 * Created by 10644291 on 09-10-2017.
 */

public class Config {
    private static Config INSTANCE;
    private int screenWidth;
    private int screenHeight;

    private Config(){
    }

    public static Config getInstance(){
        if (INSTANCE==null){
            INSTANCE=new Config();
            return INSTANCE;
        }else return INSTANCE;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}
