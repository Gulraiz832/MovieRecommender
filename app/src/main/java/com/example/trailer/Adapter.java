package com.example.trailer;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends BaseAdapter {
    List<String>added=new ArrayList<>();
    List<String>titles;
    List<String>links;
    static Intent intent;
    Context context;
    public Adapter(List<String>titles,List<String>links,Context cont){
        this.titles=new ArrayList<>(titles);
        this.links=new ArrayList<>(links);
        context=cont;

    }
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int i) {
        return titles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myview = view;
        if (myview == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myview = inflater.inflate(R.layout.adapter_layout, null);
        }
        ImageView image=null;
        TextView text=null;

        int count=0,flag=1;
        while(count!=3&&flag==1){
            switch (count){
                case 0:
                     image=myview.findViewById(R.id.image);
                     text=myview.findViewById(R.id.title);
                     break;
                case 1:
                    image=myview.findViewById(R.id.image2);
                    text=myview.findViewById(R.id.title2);
                    break;
                case 2:
                    image=myview.findViewById(R.id.image3);
                    text=myview.findViewById(R.id.title3);
                    break;

            }

            if(i<titles.size()) {
                if(!added.contains(titles.get(i))) {
                    text.setText(titles.get(i));
                    Picasso.get().load(links.get(i)).resize(120,180).into(image);
                    image.setTag(titles.get(i));
                    final Intent intent=new Intent(context,TrailerPage.class);
                    intent.putExtra("Name",(String) image.getTag());
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            context.startActivity(intent);
                        }
                    });
                    
                    image.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                           return true;
                        }
                    });
                    added.add(titles.get(i));
                    final String tag=(String) image.getTag();
                    count++;
                }
                i++;
            }
            else
                flag=0;

        }





        return myview;
    }
}
