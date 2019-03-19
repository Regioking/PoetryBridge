package cn.edu.cqu.lxq;

//import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import org.litepal.LitePal;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    String title;
    List<Poetry> p;
    private Toolbar toolbar;
    private MediaPlayer mediaPlayer;
    AlertDialog dialog;
    int flag = 0;//audio dialog



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setSupportActionBar(toolbar);

        //getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        p = LitePal.where("chineseTitle = ?",title).find(Poetry.class);
        setText();
        initToolbar();

        for(Poetry a : p){
            Log.d("这是详细页面",a.getContent());

        }
    }

    private void initToolbar(){

        toolbar= (Toolbar) findViewById(R.id.toolbar2);

        toolbar.inflateMenu(R.menu.toolbar);
        boolean collectFlag = p.get(0).isCollect();
        MenuItem collectIcon = toolbar.getMenu().findItem(R.id.collect);
        if(collectFlag){
            collectIcon.setIcon(R.drawable.bookmarked);
        }
        else{collectIcon.setIcon(R.drawable.bookmark);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.audio:
                        Toast.makeText(DetailActivity.this,"audio",Toast.LENGTH_SHORT).show();
                        if(flag==0){initDialog();}
                        else if (flag%2==1){dialog.hide();}
                        else if (flag%2==0){dialog.show();}
                        flag+=1;
                        break;

                    case R.id.collect:
                        collect();
                        //Toast.makeText(DetailActivity.this,"collectMark",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog !=null){
                    dialog.dismiss();
                }
                if(mediaPlayer!=null){
                    mediaPlayer.release();
                }
                DetailActivity.this.finish();
            }
        });
    }
    private void setText(){
        TextView contentTextView = (TextView)findViewById(R.id.contentTextView);
        TextView titleTextView = (TextView)findViewById(R.id.titleTextView);
        TextView authorTextView = (TextView)findViewById(R.id.authorTextView);
        String ChineseTitle = p.get(0).getChineseTitle();
        String EnglishTitle = p.get(0).getEnglishTitle();
        String author = p.get(0).getAuthor();
        String content = p.get(0).getContent();
        titleTextView.setText(ChineseTitle+"\n"+EnglishTitle);
        authorTextView.setText("[author]  "+author);
        contentTextView.setText(content);
    }

    private void initDialog(){//initToolbar调用


        //View view = LayoutInflater.from(this).inflate()
        AlertDialog.Builder alterdialog = new AlertDialog.Builder(DetailActivity.this);
        alterdialog.setView(R.layout.audio_dialog);
        dialog=alterdialog.create();
        dialog.show();
       // dialog.setCanceledOnTouchOutside(true);///设置点击不取消
        Window dialogWindow = dialog.getWindow();
        Display d = getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.height=(int)(d.getHeight()*0.15);
        params.width = (int)(d.getWidth()*0.95);
        params.gravity= Gravity.BOTTOM;
        params.dimAmount=0.0f;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        dialogWindow.setAttributes(params);
        //WindowManager m = getWindowManager();

        String audioID = p.get(0).getAudioId();
        int audio = getResources().getIdentifier(audioID,"raw",getPackageName());
        final ImageView playAudio = (ImageView)dialogWindow.findViewById(R.id.audioPlayStart);
        mediaPlayer = MediaPlayer.create(DetailActivity.this,audio);


        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mediaPlayer.start();
                if(!(mediaPlayer.isPlaying())){
                    //Log.d("yinpin","这是no");
                    mediaPlayer.start();
                    playAudio.setImageDrawable(getResources().getDrawable(R.drawable.button_pause_64px));
                }
                else if(mediaPlayer.isPlaying()){
                   Log.d("yinpin","这是is");
                    mediaPlayer.pause();
                    playAudio.setImageDrawable(getResources().getDrawable(R.drawable.button_play_64px));
                }
            }
        });
    }
    private void collect(){
        MenuItem collectIcon = toolbar.getMenu().findItem(R.id.collect);

        boolean collectFlag =(LitePal.where("chineseTitle = ?",title).find(Poetry.class))
                .get(0).isCollect();
        if(!collectFlag){
            Poetry poetry = new Poetry();
           poetry.setCollect(true);
           poetry.updateAll("chineseTitle = ?",title);
           collectIcon.setIcon(R.drawable.bookmarked);
           Toast.makeText(DetailActivity.this,"collecting successfully",Toast.LENGTH_SHORT).show();
        }
        else{
            Poetry poetry = new Poetry();
            poetry.setToDefault("collect");///更新为 默认值 即 false ，不能用set（false）
            //poetry.setCollect(false);
            poetry.updateAll("chineseTitle = ?",title);
            collectIcon.setIcon(R.drawable.bookmark);
            Toast.makeText(DetailActivity.this,"cancel collect",Toast.LENGTH_SHORT).show();
            //Log.d("zhe shi dijishou cang de",(int)collectFlag);
        }
    }
//    private void initMediaPlayer(){
//
//
//    }
    @Override/////并不是返回键
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(" return btn in toolbar","return");
                if(dialog !=null){
                    dialog.dismiss();
                }
                mediaPlayer.release();
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
