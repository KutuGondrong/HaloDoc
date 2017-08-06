package com.kutugondrong.halodoc.helper;

import android.content.Context;
import android.util.Log;

import com.kutugondrong.halodoc.model.BaseModel;
import com.kutugondrong.jsonkg.DataHeader;
import com.kutugondrong.jsonkg.GetJsonInternet;
import com.kutugondrong.jsonkg.InternetConectionListener;
import com.kutugondrong.jsonkg.PostJsonInternet;
import com.kutugondrong.jsonkg.model.error.ErrorKG;

import java.util.ArrayList;

/**
 * Created by kutu gondrong on 06/08/2017.
 */

public class ApiHelper <T extends BaseModel> {
    public static int POST = 1;
    public static int GET = 2;
    private String URL;
    private int REQUEST;
    private ArrayList<DataHeader> bodys;
    private String params;

    public ApiHelper(String URL, int REQUEST) {
        this.URL = URL;
        this.REQUEST = REQUEST;
        bodys = new ArrayList<>();
        params = "";
    }

    public void addDataHeader(DataHeader body){
        bodys.add(body);
    }

    public void addParams(String key,String value){
        if(params.equals("")){
            params = "?"+key+"="+value;
        }else{
            params = params+"&"+key+"="+value;
        }
    }

    public void execute(final BaseModel<T> baseModel, final RequestListener<T> requestListener){
        requestListener.onStart();
        if(REQUEST == 1){
            PostJsonInternet p = new PostJsonInternet(new InternetConectionListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onProgress(Integer... progress) {

                }

                @Override
                public void onConectionError(ErrorKG error) {
                    requestListener.onFailed();
                }

                @Override
                public void onDone(String result) {
                    Log.d("Result API",result);
                    requestListener.onSuccess(baseModel.getJsonFromApi(result));
                }
            });
            for(int i = 0; i< bodys.size(); i++){
                p.addPair(bodys.get(i).getNameHeader(),bodys.get(i).getResutHeader());
            }
            p.execute(URL+params);
        }else if(REQUEST == 2){
            GetJsonInternet p = new GetJsonInternet(new InternetConectionListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onProgress(Integer... progress) {

                }

                @Override
                public void onConectionError(ErrorKG error) {
                    requestListener.onFailed();
                }

                @Override
                public void onDone(String result) {
                    Log.d("Result API",result);
                    requestListener.onSuccess(baseModel.getJsonFromApi(result));
                }
            });
            p.execute(URL+params);
        }else{
            requestListener.onFailed();
        }
    }
}
