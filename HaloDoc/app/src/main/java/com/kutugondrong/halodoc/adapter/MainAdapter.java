package com.kutugondrong.halodoc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kutugondrong.halodoc.R;
import com.kutugondrong.halodoc.listener.CustomListViewListener;
import com.kutugondrong.halodoc.model.Data;

import java.util.ArrayList;

/**
 * Created by kutugondrong.com on 17/07/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Data> datas;
    private Context context;
    private CustomListViewListener customListViewListener;

    public MainAdapter(Context context){
        this.context = context;
        datas = new ArrayList<>();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main, parent, false);
        if(datas == null){
            datas = new ArrayList<>();
        }
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ContentViewHolder) holder).txt_title.setText(datas.get(position).getTitle());
        ((ContentViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customListViewListener.onViewClick(datas.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(datas !=null){
            return datas.size();
        }
        return 0;
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        protected TextView txt_title;

        public ContentViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            txt_title = view.findViewById(R.id.txt_title);
        }
    }

    public void setCustomListViewListener(CustomListViewListener<Data> customListViewListener) {
        this.customListViewListener = customListViewListener;
    }

    public void refresh(ArrayList<Data> datas){
        this.datas = datas;
        notifyDataSetChanged();
    }
}