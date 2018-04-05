package upec.projetandroid2017_2018;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by BELKHIR on 13/03/2018.
 */

public class MyAdapter extends ArrayAdapter<String>{
    private List<String> nameSongs;
    private int imgId;
    private Activity context;

    public MyAdapter(Activity context, List<String> nameSongs,int imgId) {
        super(context, R.layout.itemsrow, nameSongs);
        this.nameSongs=nameSongs;
        this.imgId=imgId;
        this.context=context;
    }
    public void filterList(List<String> list){
         nameSongs=list;
         notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.itemsrow,null,true);
        TextView text= view.findViewById(R.id.textrow);
        ImageView image = view.findViewById(R.id.imagerow);
        text.setText(nameSongs.get(position));
        image.setImageResource(imgId);
        return view;
    }
}


/* View r = convertView;
        ViewHolder viewHolder=null;

        if( r==null){
         LayoutInflater layoutInflater = context.getLayoutInflater();
         r=layoutInflater.inflate(R.layout.itemsrow,null,true);
         viewHolder = new ViewHolder(r);
         r.setTag(viewHolder);


        }else{
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.imgv.setImageResource(R.drawable.img);
        viewHolder.textv.setText(nameSongs.get(position));
        return r;

    }

    class ViewHolder {
        TextView textv ;
        ImageView imgv;

        ViewHolder( View v){
            textv = (TextView) v.findViewById(R.id.txtr);
            imgv =(ImageView) v.findViewById(R.id.imageViewr);
        }*/