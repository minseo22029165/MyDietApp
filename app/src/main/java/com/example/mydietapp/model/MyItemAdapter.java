package com.example.mydietapp.model;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.example.mydietapp.R;

import java.util.List;
import java.util.Map;

public class MyItemAdapter extends BaseAdapter {
    private List<MyItem> data;
    private int layout;
    private LayoutInflater inflater;

    public MyItemAdapter(Context context, int layout, List<MyItem> data) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout=layout;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        ImageView image=convertView.findViewById(R.id.list_image);
        TextView text=convertView.findViewById(R.id.list_text);

        MyItem item=data.get(position);
        image.setImageResource(item.getIcon());
        text.setText(item.getText());

        return convertView;
    }
}
