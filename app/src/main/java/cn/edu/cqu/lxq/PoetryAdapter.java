package cn.edu.cqu.lxq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class PoetryAdapter extends ArrayAdapter<Poetry> {
    private int resourceId;
    public PoetryAdapter(Context context, int textViewResourceId, List<Poetry> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Poetry poetry = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView titleItem =(TextView) view.findViewById(R.id.titleItem);
        TextView authorItem=(TextView) view.findViewById(R.id.authorItem);
        titleItem.setText(poetry.getChineseTitle()+"\n"+poetry.getEnglishTitle());
        authorItem.setText(poetry.getAuthor());
        return view;
    }
}
