package com.kutugondrong.halodoc.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kutu gondrong on 06/08/2017.
 */

public class Data extends BaseModel{

    private String title;
    private String url;

    private ArrayList<Data> datas;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Data> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<Data> datas) {
        this.datas = datas;
    }

    @Override
    public Object getJsonFromApi(String value) {
        Data baseModel = new Data();
        ArrayList<Data> datas = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(value);
            JSONArray arr = obj.getJSONArray("hits");
            for(int i=0;i<arr.length();i++){
                Data data = new Data();
                JSONObject v = arr.getJSONObject(i);
                data.setTitle(v.getString("title"));
                data.setUrl(v.getString("url"));
                Log.d("TITLE",data.getTitle());
                datas.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseModel.setDatas(datas);
        return baseModel;
    }
}
