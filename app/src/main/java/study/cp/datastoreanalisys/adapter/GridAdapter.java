package study.cp.datastoreanalisys.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GridAdapter extends BaseAdapter{
    private Context mContext;
    private List<List<String>> content;

    public GridAdapter(Context mContext, List<List<String>> values) {
        this.mContext = mContext;
        content = values;
    }

    @Override
    public int getCount() {
        int size = 0;
        for (List<String> i: content){
            for (List<String> j: content){
                size++;
            }
        }
        return size;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int k, View view, ViewGroup viewGroup) {
        TextView tv;
        if (view == null) {
            tv = new TextView(mContext);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            tv = (TextView) view;
        }
        int i = k/3;
        int j = k%3;
        if(content.get(i).size()<3) return tv;
        tv.setText(content.get(i).get(j));
        return tv;
    }
}
