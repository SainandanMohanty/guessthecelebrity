package com.example.sain.guessthecelebrity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();
    }

    private void initialise() {
        String html = "";
        String url = "https://www.therichest.com/top-lists/top-100-richest-celebrities/";

        DownloadTask downloadTask = new DownloadTask();

        try {
            html = downloadTask.execute(url).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Pattern patternName = Pattern.compile("<div class=\"list-profile-name\">\\n(.*)\\n</div>");
        Matcher matcherName = patternName.matcher(html);

        Pattern patternLink = Pattern.compile(
                "<source media=\"\\(min-width: 0px\\)\" sizes=\"70px\" srcset=\"(.*)\\?q=50&amp;fit=crop&amp;w=70&amp;h=70 70w\"/>");
        Matcher matcherLink = patternLink.matcher(html);

        while (matcherName.find() && matcherLink.find()) {
            arrayList.add(matcherName.group(1));
            hashMap.put(matcherName.group(1), matcherLink.group(1));
        }
    }

    public void onClick(View view) {
    }
}
