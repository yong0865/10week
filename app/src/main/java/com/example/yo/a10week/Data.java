package com.example.yo.a10week;

/**
 * Created by yo on 2017-05-10.
 */

public class Data {
    String url;
    String sitename;


    public Data(String sitename, String url){
        this.sitename = sitename;
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
    public String getSitename(){
        return sitename;
    }
}

