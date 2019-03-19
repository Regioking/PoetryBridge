package cn.edu.cqu.lxq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置全屏
        setContentView(R.layout.activity_splash);
        firstLoad();
       handler.postDelayed(transition,3000);
    }



    private Runnable transition=new Runnable()
    {
        @Override
        public void run() {
            Intent i = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(i);
            SplashActivity.this.finish();
        }
    };


    public String getFromRaw(){///////读取json
        String Result="";
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().openRawResource(R.raw.poetry));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";

            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){return  true;}
        return  super.onKeyDown(keyCode,event);
    }

    private void firstLoad() {
        SharedPreferences shared = getSharedPreferences("is", MODE_PRIVATE);
        boolean isfer = shared.getBoolean("isfer", true);
        SharedPreferences.Editor editor=shared.edit();
        if (isfer) {
            //第一次进入 加载数据

            Utility.handlePoetryFromJson(getFromRaw());
            editor.putBoolean("isfer", false);
            editor.commit();
        }
        else {
            //第二次进入
        }

    }
}
