package cn.edu.cqu.lxq;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Utility {



    public static boolean handlePoetryFromJson(String text){
        Gson gson = new Gson();
        List<Poetry> poetryList = gson.fromJson(text,new TypeToken< List<Poetry>>(){}.getType());
        for (Poetry p : poetryList){
            Log.d("TESTTTTTTTTTTTTTTTTT",p.getContent());
           // p.save();

        }
        LitePal.saveAll(poetryList);
        //LitePalSupport.saveAll();
        return true;
    }

}
