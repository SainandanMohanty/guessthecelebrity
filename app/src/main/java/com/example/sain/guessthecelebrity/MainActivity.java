package com.example.sain.guessthecelebrity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
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
        setQuestion();
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

    private void setQuestion() {
        Random random = new Random();
        int position = random.nextInt(25);

        DownloadImage downloadImage = new DownloadImage();
        Bitmap bitmap = null;
        try {
            bitmap = downloadImage.execute(hashMap.get(arrayList.get(position))).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        Button buttonOption;
        int option;

        int correctOption = random.nextInt(4);
        ArrayList<Integer> optionArrayList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            buttonOption = (Button) linearLayout.getChildAt(i);

            if (i == correctOption) {
                buttonOption.setText(arrayList.get(position));
                buttonOption.setTag("1");
            } else {
                do {
                    option = random.nextInt(25);
                }
                while (option == position || optionArrayList.contains(option));
                optionArrayList.add(option);
                buttonOption.setText(arrayList.get(option));
                buttonOption.setTag("0");
            }
        }
    }

    public void onClick(View view) {
        if (view.getTag().equals("1")) {
            Toast.makeText(this, "Correct :)", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect :(", Toast.LENGTH_SHORT).show();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setQuestion();
            }
        }, 1500);
    }
}
