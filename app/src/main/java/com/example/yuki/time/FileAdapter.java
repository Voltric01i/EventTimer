package com.example.yuki.time;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Yuki on 2016/04/19.
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private ArrayList<InputData>mDataList;

    public FileAdapter(Context context, ArrayList<InputData> dataList) {
        super();
        mLayoutInflater = LayoutInflater.from(context);
        mDataList = dataList;
    }

    // getViewのinfrateするところだけ取り出した感じ
    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.event_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InputData data = (InputData) mDataList.get(position);
        holder.title.setText(data.getTitle() +"\n" + data.getDateTimeString());
        holder.countdown.setText(data.getCountDownString());
        holder.memo.setText(data.getMemo());
    }

    // ViewHolder内でwidgetを割り当てる
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView countdown;
        TextView memo;

        public ViewHolder(View v) {
            super(v);
            title= (TextView) v.findViewById(R.id.EventTitle);
            countdown=(TextView) v.findViewById(R.id.EventConutdown);
            memo = (TextView) v.findViewById(R.id.EventMemo);
        }

    }
}
