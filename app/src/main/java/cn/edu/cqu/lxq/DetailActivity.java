package cn.edu.cqu.lxq;

//import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
    private AlertDialog dialog;
    private SeekBar seekBar;

    int flag = 0;//audio dialog

    ////////////////////////////////////////////////////////////
    private Thread thread;
    //记录播放位置
    private int time;
    //记录是否暂停
    private boolean  isChanging = false;
    private TextView timeTextView;
    private boolean stopThread = false;
    ////////////////////////////////////////////////////////////
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

//        for(Poetry a : p){
//            Log.d("这是详细页面",a.getContent());
//        }
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
                       // Toast.makeText(DetailActivity.this,"audio",Toast.LENGTH_SHORT).show();
                        if(flag==0){
                            initDialog();
                            toolbar.getMenu().findItem(R.id.audio).setIcon(R.drawable.earphoned);}
                        else if (flag%2==1){dialog.hide();
                            toolbar.getMenu().findItem(R.id.audio).setIcon(R.drawable.earphone);}
                        else if (flag%2==0){dialog.show();
                            toolbar.getMenu().findItem(R.id.audio).setIcon(R.drawable.earphoned);}
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
//                if(thread!=null && thread.isAlive()){
//                    thread.interrupt();////中止线程
//                }
                stopThread=true;
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
        ///////////////////////////////show 之后再骚，注意自定义布局控件由window获取

       // dialog.setCanceledOnTouchOutside(true);///设置点击不取消
        Window dialogWindow = dialog.getWindow();
        Display d = getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.height=(int)(d.getHeight()*0.12);
        params.width = (int)(d.getWidth()*0.99);
        params.y = 100;
        params.gravity= Gravity.TOP;
        params.dimAmount=0.0f;///背景全透明
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        dialogWindow.setAttributes(params);
        //WindowManager m = getWindowManager();
        final ImageView playAudio = (ImageView)dialogWindow.findViewById(R.id.audioPlayStart);
        seekBar = (SeekBar)dialogWindow.findViewById(R.id.seekBar);
        timeTextView = dialogWindow.findViewById(R.id.timeText);

        String audioID = p.get(0).getAudioId();///当前诗获取资源ID
        int audio = getResources().getIdentifier(audioID,"raw",getPackageName());
        if(audio!=0){
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
                    /////////////////////////////////////////
                    seekBar.setOnSeekBarChangeListener( new SeekBarListener());
                    seekBar.setMax(mediaPlayer.getDuration());
                    thread = new Thread(new SeekBarThread());
                    // 启动线程
                    thread.start();
                    /////////////////////////////////////////////
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playAudio.setImageDrawable(getResources().getDrawable(R.drawable.button_play_64px));
                }
            });

        }
        //资源不存在
        else{
            Toast.makeText(DetailActivity.this,"No resources",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
    /////////////////////////////////////////////////////////////
    class SeekBarThread implements Runnable {

        @Override
        public void run() {
            while (!stopThread&&!isChanging && mediaPlayer.isPlaying()) {
                // 将SeekBar位置设置到当前播放位置
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                try {
                    // 每100毫秒更新一次位置
                    Thread.sleep(100);
                    //播放进度
                } catch (InterruptedException e) {
                    //e.printStackTrace();

                }
            }
        }
    }

    class SeekBarListener implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            ///////"当前播放时间"
            timeTextView.setText( ShowTime(mediaPlayer.getDuration()-progress));

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //防止在拖动进度条进行进度设置时与Thread更新播放进度条冲突
            isChanging = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            timeTextView.setText(ShowTime(seekBar.getProgress()));
            //将media进度设置为当前seekbar的进度
            mediaPlayer.seekTo(seekBar.getProgress());
            isChanging = false;
            thread = new Thread(new SeekBarThread());
            // 启动线程
            thread.start();
        }

    }
    private String ShowTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
       // minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }
/////////////////////////////////////////////////////////////////////////////////////
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager audioManager  = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            // 音量+键
            audioManager.adjustStreamVolume(AudioManager.
                    STREAM_MUSIC, AudioManager.ADJUST_RAISE,AudioManager.FX_FOCUS_NAVIGATION_UP);
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            // 音量-键
            audioManager.adjustStreamVolume(AudioManager.
                    STREAM_MUSIC, AudioManager.ADJUST_LOWER,AudioManager.FX_FOCUS_NAVIGATION_UP);
            return true;
        }
        else if(keyCode==KeyEvent.KEYCODE_BACK){
            stopThread=true;
            if(dialog !=null){
                dialog.dismiss();
            }
            if(mediaPlayer!=null){
                mediaPlayer.release();
            }
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

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
