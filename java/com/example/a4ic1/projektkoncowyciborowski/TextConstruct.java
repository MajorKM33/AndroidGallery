package com.example.a4ic1.projektkoncowyciborowski;

import android.graphics.Typeface;

import java.io.Serializable;

/**
 * Created by 4ic1 on 2016-11-25.
 */
public class TextConstruct implements Serializable {

    private String text;
    private String font;
    private int tcolor;
    private int bcolor;

    public TextConstruct(String t1, String f1, int c1, int c2){
        text = t1;
        font = f1;
        tcolor = c1;
        bcolor = c2;
    }

    public String getText(){ return text; }
    public String getFont(){ return font; }
    public int getTcolor(){ return tcolor; }
    public int getBcolor(){ return bcolor; }
}
