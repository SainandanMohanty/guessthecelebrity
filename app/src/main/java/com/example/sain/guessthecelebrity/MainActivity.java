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

        ArrayList<String> urlArrayList = new ArrayList<>();
        urlArrayList.add("https://www.therichest.com/top-lists/top-100-richest-celebrities/");
        urlArrayList.add("https://www.therichest.com/top-lists/top-100-richest-celebrities/page/2/");
        urlArrayList.add("https://www.therichest.com/top-lists/top-100-richest-celebrities/page/3/");
        urlArrayList.add("https://www.therichest.com/top-lists/top-100-richest-celebrities/page/4/");

        initialise(urlArrayList);
        setQuestion();
    }

    private void initialise(ArrayList<String> urlArrayList) {
        String html = "";

        for (String url : urlArrayList) {
            DownloadTask downloadTask = new DownloadTask();

            try {
                html = html.concat(downloadTask.execute(url).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        int position = random.nextInt(100);

        DownloadImage downloadImage = new DownloadImage();
        Bitmap bitmap = null;
        try {
            bitmap = downloadImage.execute(hashMap.get(arrayList.get(position))).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
        imageView.setTag(arrayList.get(position));

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
                    option = random.nextInt(arrayList.size());
                }
                while (option == position || optionArrayList.contains(option));
                optionArrayList.add(option);
                buttonOption.setText(arrayList.get(option));
                buttonOption.setTag("0");
            }
        }
    }

    public void onClick(View view) {
        ImageView imageView = findViewById(R.id.imageView);

        if (view.getTag().equals("1")) {
            Toast.makeText(this, "Correct!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, String.format("Wrong, it's %s", imageView.getTag().toString()), Toast.LENGTH_SHORT).show();
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
