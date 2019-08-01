package com.example.a4ic1.projektkoncowyciborowski;

/**
 * Created by MajorKM33 on 14.12.2016.
 */
public class JsonData {

    String created_at;
    String bytes;
    String id;
    String ver;

    public JsonData(String _created_at, String _bytes, String _id, String _ver ) {
        this.created_at = _created_at;
        this.bytes = _bytes;
        this.id = _id;
        this.ver = _ver;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getBytes() {
        return bytes;
    }

    public String getPId() {
        return id;
    }

    public String getVer() {
        return ver;
    }
}
