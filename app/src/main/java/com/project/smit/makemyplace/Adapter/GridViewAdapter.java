package com.project.smit.makemyplace.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.smit.makemyplace.R;

public class GridViewAdapter extends ArrayAdapter<String> {

    String[] name={};
    int[] image={};
    Context c;
    LayoutInflater inflater;

    public GridViewAdapter(@NonNull Context context, String[] name, int[] image) {
        super(context, R.layout.row_grid, name);
        this.c = context;
        this.name = name;
        this.image = image;
    }

    public class ViewHolder{
        TextView nameTv;
        ImageView imageIv;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.row_grid,null);
        }

        final ViewHolder holder = new ViewHolder();

        holder.nameTv=(TextView)convertView.findViewById(R.id.tvGridText);
        holder.imageIv=(ImageView)convertView.findViewById(R.id.ivGridImage);

        holder.nameTv.setText(name[position]);
        holder.imageIv.setImageResource(image[position]);

        return convertView;
    }
}
