package com.kutugondrong.halodocgallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    protected ArrayList<File> list;

    private static final int INITIAL_REQUEST=11;
    private static final String[] INITIAL_PERMS={Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!hasPermissions(this,INITIAL_PERMS)){
            ActivityCompat.requestPermissions(this, INITIAL_PERMS, INITIAL_REQUEST);
        }
        initLayout();
        initFile();
        initAdapter();
        initAction();

    }

    private void initLayout(){
        gridView = (GridView) findViewById(R.id.gridview);
    }

    private void initFile(){
        list = imageReader(Environment.getExternalStorageDirectory());
    }

    private void initAdapter(){
        gridView.setAdapter(new GridAdapter());
    }

    private ArrayList<File> imageReader(File root){
        ArrayList<File> files = new ArrayList<>();
        File[] listFile = root.listFiles();
        if(listFile == null){
            return files;
        }
        for(int i=0;i<listFile.length;i++){
            if(listFile[i].isDirectory()){
                files.addAll(imageReader(listFile[i]));
            }else{
                if(listFile[i].getName().endsWith("jpg")){
                    files.add(listFile[i]);
                }
            }
        }
        return files;
    }


    private void initAction(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent  intent= new Intent(getApplicationContext(),ViewImage.class);
                intent.putExtra("img",list.get(i).toString());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case INITIAL_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    for(int i=0;i<INITIAL_PERMS.length;i++){
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            Log.d("REQUESTEXT","true");
                        }else{
                            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                            Log.d("REQUESTEXT","false");
                            return;
                        }
                    }
                }
                return;
            }
        }
    }

    /**
     * Created by kutu gondrong on 06/08/2017.
     */

    public  class GridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.singgle_grid, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.iv.setImageURI(Uri.parse(getItem(i).toString()));
            return view;
        }

        protected class ViewHolder {
            protected  ImageView iv;

            public ViewHolder(View view) {
                iv = view.findViewById(R.id.iv);
            }
        }
    }
}
