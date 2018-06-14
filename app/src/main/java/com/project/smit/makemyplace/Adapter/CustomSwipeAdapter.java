package com.project.smit.makemyplace.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.project.smit.makemyplace.R;


public class CustomSwipeAdapter extends PagerAdapter {

    private int[] image_resource;
    private Context cxt;
    private LayoutInflater layoutInflater;

    public CustomSwipeAdapter(Context cxt, int[] image_resource) {
        this.cxt = cxt;
        this.image_resource=image_resource;
    }

    @Override
    public int getCount() {
        return image_resource.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.swipe_viewpager,container,false);
        ImageView imageView = (ImageView)itemView.findViewById(R.id.ivSlider);
        //imageView.setImageResource(image_resource[position]);
        imageView.setBackgroundResource(image_resource[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
