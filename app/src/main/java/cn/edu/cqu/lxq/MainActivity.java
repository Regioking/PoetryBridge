package cn.edu.cqu.lxq;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DailyFragment dailyFragment = new DailyFragment();
    PoetryListFragment poetryListFragment = new PoetryListFragment();
    CollectionFragment collectionFragment = new CollectionFragment();
    SearchView searchView ;
    List<Poetry> searchList = new ArrayList<>();
    List<Poetry> resultList = new ArrayList<>();
    int curPoisition = 1;
   // Toolbar toolbar ;
  // private GestureDetector mDetector;
    //private final static int MIN_MOVE = 200;   //最小距离
    //private MyGestureListener mgListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentInitial();
        ButtonControl();

       // LitePal.getDatabase();
       // LitePal.deleteAll(Poetry.class);
        //Log.d("riqi", ""+Calendar.getInstance().get(Calendar.DATE));
       initSearchView();

        //实例化GestureListener与GestureDetector对象
       // mgListener = new MyGestureListener();
       // mDetector = new GestureDetector(this, mgListener);
    }


    private void fragmentInitial(){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.dailyFramelayout,dailyFragment,"dailyFragment");
        ft.add(R.id.dailyFramelayout,poetryListFragment,"poetryListFragment");
        ft.add(R.id.dailyFramelayout,collectionFragment,"collectFragment");
        ft.hide(poetryListFragment);
        ft.hide(collectionFragment);
        ft.commit();
    }


    ///搜索框
    private void initSearchView(){
        searchView = findViewById(R.id.searchView);
        //searchView.onActionViewExpanded();
        final ListPopupWindow listPopupWindow = new ListPopupWindow(MainActivity.this);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Poetry poetry = searchList.get(position);
                //Toast.makeText(v.getContext(), "you clicked view " + poetry.getChineseTitle(), Toast.LENGTH_SHORT).show();
                Intent it = new Intent();
                it.putExtra("title",poetry.getChineseTitle());
                Intent intent = it.setClass(MainActivity.this,DetailActivity.class);
                startActivity(intent);
                listPopupWindow.dismiss();
            }
        });
        searchView.setQueryHint("Please enter the search content");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query))
                {
                    Toast.makeText(MainActivity.this, "please input！", Toast.LENGTH_SHORT).show();
                }
               else{
                   searchList = LitePal.where("chineseTitle like ? or " + "englishTitle like ? " +
                           "or author like ?","%"+query+"%","%"+query+"%","%"+query+"%").find(Poetry.class);
                    if(searchList.size() == 0)
                    {
                        Toast.makeText(MainActivity.this, "Please enter what you want to find", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Toast.makeText(MainActivity.this, "Search success", Toast.LENGTH_SHORT).show();
                        PoetryAdapter searchAdapter = new PoetryAdapter(MainActivity.
                                this,R.layout.poetrylist_item, searchList);
                        listPopupWindow.setAdapter(searchAdapter);
                        listPopupWindow.setAnchorView(findViewById(R.id.toolbar));
                        //listPopupWindow.setVerticalOffset(100);
                        listPopupWindow.show();
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!=null&&newText.length()!=0){
                    Log.d("textchange",newText);
                    searchList = LitePal.where("chineseTitle like ? or " + "englishTitle like ? " +
                            "or author like ?","%"+newText+"%","%"+newText+"%","%"+newText+"%")
                            .limit(8)
                            .find(Poetry.class);
                }

                if(searchList.size() != 0)
                {
                    //Toast.makeText(MainActivity.this, "无", Toast.LENGTH_SHORT).show();
                    PoetryAdapter searchAdapter = new PoetryAdapter(MainActivity.
                            this,R.layout.poetrylist_item, searchList);
                    listPopupWindow.setAdapter(searchAdapter);
                    listPopupWindow.setAnchorView(findViewById(R.id.toolbar));
                    //listPopupWindow.setVerticalOffset(100);
                    listPopupWindow.show();
                }
                return false;
            }
        });
    }



    private void ButtonControl(){
        final Button btn_PoetryList = (Button)findViewById(R.id.btn_PoetryList);
        final Button btn_daily = (Button)findViewById(R.id.btn_daily);
        final Button btn_collect = (Button)findViewById(R.id.btn_collect);
        btn_daily.setEnabled(false);
        btn_PoetryList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getSupportFragmentManager();
                final FragmentTransaction ft = fm.beginTransaction();
                btn_collect.setEnabled(true);
                btn_daily.setEnabled(true);
                btn_PoetryList.setEnabled(false);
                //addFragment(R.id.dailyFramelayout,new PoetryListFragment(),"PoetryList");
                ft.hide(dailyFragment);
                ft.hide(collectionFragment);
                ft.show(poetryListFragment);
                ft.commit();
            }
        });
        btn_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getSupportFragmentManager();
                final FragmentTransaction ft = fm.beginTransaction();
                btn_collect.setEnabled(true);
                btn_daily.setEnabled(false);
                btn_PoetryList.setEnabled(true);
                //addFragment(R.id.dailyFramelayout,new PoetryListFragment(),"PoetryList");
                ft.hide(poetryListFragment);
                ft.hide(collectionFragment);
                ft.show(dailyFragment);
                ft.commit();
            }
        });
        btn_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getSupportFragmentManager();
                final FragmentTransaction ft = fm.beginTransaction();
                btn_collect.setEnabled(false);
                btn_daily.setEnabled(true);
                btn_PoetryList.setEnabled(true);
                //addFragment(R.id.dailyFramelayout,new PoetryListFragment(),"PoetryList");
                ft.hide(poetryListFragment);
                ft.hide(dailyFragment);
                ft.show(collectionFragment);
                ft.commit();
            }
        });
    }


//    private void addFragment(int ID,Fragment d,String tag){
//        //DailyFragment d = new DailyFragment();
//        getSupportFragmentManager(). beginTransaction().replace(ID, d,tag).commit();
////        FragmentTransaction transaction = fragmentManager. beginTransaction();
////        transaction.replace(R.id.dailyfragment, d);
////        transaction.commit();
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return mDetector.onTouchEvent(event);
//    }
//
//    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
//            if(curPoisition==1){
//                if(e1.getX() - e2.getX() > MIN_MOVE){
//                    Toast.makeText(MainActivity.this, "1 to 2", Toast.LENGTH_SHORT).show();
//                    curPoisition++;
//                }
//            }
//            else if(curPoisition==2){
//                if(e1.getX() - e2.getX() > MIN_MOVE){
//                    Toast.makeText(MainActivity.this, "2 to 3", Toast.LENGTH_SHORT).show();
//                    curPoisition++;
//                }
//                if(e1.getX() - e2.getX() < -MIN_MOVE){
//                    Toast.makeText(MainActivity.this, "2 to 1", Toast.LENGTH_SHORT).show();
//                    curPoisition--;
//                }
//            }
//            else if(curPoisition==3){
//                if(e1.getX() - e2.getX() < -MIN_MOVE){
//                    Toast.makeText(MainActivity.this, "3 to 2", Toast.LENGTH_SHORT).show();
//                    curPoisition--;
//                }
//            }
//
//
//            return true;
//        }
//    }


}
