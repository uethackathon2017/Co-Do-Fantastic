package com.example.anhdt.smartalarm.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.adapters.PagerAdapter;
import com.example.anhdt.smartalarm.adapters.RSSParser;
import com.example.anhdt.smartalarm.fragments.NewsFragment;
import com.example.anhdt.smartalarm.models.RSSItem;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.anhdt.smartalarm.R.id.relaytive_thugon;

public class WakeUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private RelativeLayout linerWeather;
    private LinearLayout linerLocation;
    private ImageView imvLocation;
    private TextView txvLocationName;
    private RelativeLayout relativeTemper;
    private ImageView imvWeather;
    private TextView txvTemperature;
    private TextView txvDescription;
    private TextView txvHumidity;
    private TextView txvSpeed;
    private RelativeLayout relaytiveNews;
    private TextView pageNameNews;
    private RelativeLayout relaytiveNews1;
    private ImageView imageNews1;
    private TextView news1Title;
    private RelativeLayout relaytiveNews2;
    private ImageView imageNews2;
    private TextView news2Title;
    private RelativeLayout relaytiveNews3;
    private ImageView imageNews3;
    private TextView news3Title;
    private RelativeLayout relaytiveContinuesHome;
    private TextView tvContinuesHome;
    RSSParser rssParser = new RSSParser();

    List<RSSItem> rssItems = new ArrayList<RSSItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake_up);

        textView = (TextView) findViewById(R.id.textView);
        linerWeather = (RelativeLayout) findViewById(R.id.liner_weather);
        linerLocation = (LinearLayout) findViewById(R.id.liner_location);
        imvLocation = (ImageView) findViewById(R.id.imv_location);
        txvLocationName = (TextView) findViewById(R.id.txv_location_name);
        relativeTemper = (RelativeLayout) findViewById(R.id.relative_temper);
        imvWeather = (ImageView) findViewById(R.id.imv_weather);
        txvTemperature = (TextView) findViewById(R.id.txv_temperature);
        txvDescription = (TextView) findViewById(R.id.txv_description);
        txvHumidity = (TextView) findViewById(R.id.txv_humidity);
        txvSpeed = (TextView) findViewById(R.id.txv_speed);
        relaytiveNews = (RelativeLayout) findViewById(R.id.relaytive_news);
        pageNameNews = (TextView) findViewById(R.id.pageName_news);
        relaytiveNews1 = (RelativeLayout) findViewById(R.id.relaytive_news_1);
        imageNews1 = (ImageView) findViewById(R.id.image_news_1);
        news1Title = (TextView) findViewById(R.id.news_1_Title);
        relaytiveNews2 = (RelativeLayout) findViewById(R.id.relaytive_news_2);
        imageNews2 = (ImageView) findViewById(R.id.image_news_2);
        news2Title = (TextView) findViewById(R.id.news_2_Title);
        relaytiveNews3 = (RelativeLayout) findViewById(R.id.relaytive_news_3);
        imageNews3 = (ImageView) findViewById(R.id.image_news_3);
        news3Title = (TextView) findViewById(R.id.news_3_Title);
        relaytiveContinuesHome = (RelativeLayout) findViewById(R.id.relaytive_continues_Home);
        tvContinuesHome = (TextView) findViewById(R.id.tv_continues_home);

        relaytiveContinuesHome.setOnClickListener(this);
        relaytiveNews3.setOnClickListener(this);
        relaytiveNews2.setOnClickListener(this);
        relaytiveNews1.setOnClickListener(this);

        init();
    }

    private void init() {

        //check internet
        if (isNetworkConnected() == false) {
            relaytiveNews1.setVisibility(View.GONE);
            relaytiveNews2.setVisibility(View.GONE);
            relaytiveNews3.setVisibility(View.GONE);
            tvContinuesHome.setText("no internet access");
            Log.v("abc", "ko co mang");
        } else {
            relaytiveNews1.setVisibility(View.VISIBLE);
            relaytiveNews2.setVisibility(View.VISIBLE);
            relaytiveNews3.setVisibility(View.VISIBLE);
            tvContinuesHome.setText("Xem thêm ...");
        }
        String rss_link = "http://vnexpress.net/rss/tin-moi-nhat.rss";

        new loadRSSFeedItems().execute(rss_link);

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    @Override
    public void onClick(View view) {
        Intent in;
        String page_url;
        switch (view.getId()) {

            case R.id.relaytive_continues_Home:
                in = new Intent(this, ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/tin-moi-nhat.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "VnExPressHome");
                startActivity(in);
                break;

        }
    }

    class loadRSSFeedItems extends AsyncTask<String, String, String> {

        private ArrayList<String> linkImage = new ArrayList<>();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * getting all recent articles and showing them in listview
         * */
        @Override
        protected String doInBackground(String... args) {
            // rss link url
            String rss_url = args[0];

            int count = 0;
            // list of rss items
            rssItems = rssParser.getRSSFeedItems(rss_url);

            // looping through each item
            for(RSSItem item : rssItems){
                // creating new HashMap
                if (count >= 3) break;
                String description = item.getDescription();
                if (description.contains("_180x108")) {
                    description = description.replace("_180x108", "");
                    item.setDescription(description);
                }
                count++;
            }

            // updating UI from Background Thread

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
            if (rssItems.size() > 0) {
                Picasso.with(WakeUpActivity.this)
                        .load(rssItems.get(0).getDescription())
                        .resize(220,220)
                        .into(imageNews1);
                news1Title.setText(rssItems.get(0).getTitle());
                //news1Date.setText(rssItems.get(0).getPubdate());
                Picasso.with(WakeUpActivity.this)
                        .load(rssItems.get(1).getDescription())
                        .resize(220,220)
                        .centerCrop()
                        .into(imageNews2);
                news2Title.setText(rssItems.get(1).getTitle());
                //news2Date.setText(rssItems.get(1).getPubdate());
                Picasso.with(WakeUpActivity.this)
                        .load(rssItems.get(2).getDescription())
                        .resize(220,220)
                        .centerCrop()
                        .into(imageNews3);
                news3Title.setText(rssItems.get(2).getTitle());
                //news3Date.setText(rssItems.get(2).getPubdate());
                relaytiveNews1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(WakeUpActivity.this, DisPlayWebPageActivity.class);
                        // getting page url
                        String page_url = rssItems.get(0).getLink();
                        in.putExtra("page_url", page_url);
                        in.putExtra("Title", "VnExPressHome");
                        startActivity(in);
                    }
                });
                relaytiveNews2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(WakeUpActivity.this, DisPlayWebPageActivity.class);
                        // getting page url
                        String page_url = rssItems.get(1).getLink();
                        in.putExtra("page_url", page_url);
                        in.putExtra("Title", "VnExPressHome");
                        startActivity(in);
                    }
                });
                relaytiveNews3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(WakeUpActivity.this, DisPlayWebPageActivity.class);
                        // getting page url
                        String page_url = rssItems.get(2).getLink();
                        in.putExtra("page_url", page_url);
                        in.putExtra("Title", "VnExPressHome");
                        startActivity(in);
                    }
                });
            }

        }
    }

}
