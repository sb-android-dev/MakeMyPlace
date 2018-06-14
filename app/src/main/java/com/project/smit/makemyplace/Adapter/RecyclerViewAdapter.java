package com.project.smit.makemyplace.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.smit.makemyplace.Activity.Details;
import com.project.smit.makemyplace.Bean.RecyclerViewBean;
import com.project.smit.makemyplace.Parsing.Response;
import com.project.smit.makemyplace.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Response> values;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView text;
        public ImageView image;
        public List<Response> beanList = new ArrayList<>();
        Context context;

        public ViewHolder(View itemView,Context context,List<Response> beanList) {
            super(itemView);
            this.beanList=beanList;
            this.context=context;
            itemView.setOnClickListener(this);
            text = (TextView)itemView.findViewById(R.id.tvRowText);
            image = (ImageView)itemView.findViewById(R.id.ivRowImage);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Response response = this.beanList.get(position);
            Intent intent = new Intent(this.context,Details.class);
            intent.putExtra("image",response.getImage());
            intent.putExtra("name",response.getName());
            intent.putExtra("phone",response.getPhone());
            intent.putExtra("address",response.getAddress());
            intent.putExtra("latitude",response.getLatitude());
            intent.putExtra("longitude",response.getLongitude());
            intent.putExtra("cat",response.getCat());
            this.context.startActivity(intent);
        }
    }

    public RecyclerViewAdapter(ArrayList<Response> values,Context context) {
        this.values = values;
        this.context = context;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler,parent,false);
        return new RecyclerViewAdapter.ViewHolder(itemView,context,values);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Response response = values.get(position);
        holder.text.setText(response.getName());
        String i = response.getImage();
        byte[] imgByte = Base64.decode(i,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        holder.image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public void setFilter(ArrayList<Response> newList){
        values = new ArrayList<>();
        values.addAll(newList);
        notifyDataSetChanged();
    }


}
