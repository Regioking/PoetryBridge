package cn.edu.cqu.lxq;



import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class RecyclePoetryAdapter extends RecyclerView.Adapter<RecyclePoetryAdapter.ViewHolder>{

    private List<Poetry> mPoetryList;
    //1、创建构造函数
    //构造函数参数是list的集合，他是rv直接的数据来源
    //在做Adapter实例化的时候必须传入list的集合数据
    public RecyclePoetryAdapter(List<Poetry> fruitList) {
        mPoetryList = fruitList;
    }
    //2、创建类ViewHolder（视图容器），承载的是条目中的控件
    //做条目中控件的声明和绑定
    static class ViewHolder extends RecyclerView.ViewHolder {
        View poetryView;

        TextView titleItem;
        TextView authorItem;

        public ViewHolder(View view) {
            super(view);
            poetryView = view;
            titleItem = (TextView) view.findViewById(R.id.titleItem);
            authorItem = (TextView) view.findViewById(R.id.authorItem);
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poetrylist_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.poetryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Poetry poetry = mPoetryList.get(position);
                //Toast.makeText(v.getContext(), "you clicked view " + poetry.getChineseTitle(), Toast.LENGTH_SHORT).show();
                Intent it = new Intent();
                it.putExtra("title",poetry.getChineseTitle());
                Intent intent = it.setClass(v.getContext(),DetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });


//        holder.titleItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Poetry fruit = mPoetryList.get(position);
//                Toast.makeText(v.getContext(), "you clicked image " + fruit.getAuthor(), Toast.LENGTH_SHORT).show();
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Poetry poetry = mPoetryList.get(position);
        holder.titleItem.setText(poetry.getChineseTitle()+"\n"+poetry.getEnglishTitle());
        holder.authorItem.setText(poetry.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mPoetryList.size();
    }

}
