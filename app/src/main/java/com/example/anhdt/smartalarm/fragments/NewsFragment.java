package com.example.anhdt.smartalarm.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.activities.DisPlayWebPageActivity;
import com.example.anhdt.smartalarm.adapters.RSSParser;
import com.example.anhdt.smartalarm.models.RSSFeed;
import com.example.anhdt.smartalarm.models.RSSItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ProgressDialog pDialog;

    // Array list for list view
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String,String>>();

    RSSParser rssParser = new RSSParser();

    List<RSSItem> rssItems = new ArrayList<RSSItem>();

    private RelativeLayout newsHead;
    private RelativeLayout relaytiveNewsWorld;
    private ImageView iconNewsWorld;
    private TextView newsWorld;
    private RelativeLayout relaytiveNewsEvents;
    private ImageView iconNewsEvents;
    private TextView newsEvents;
    private RelativeLayout relaytiveNewsHealth;
    private ImageView iconNewsHealth;
    private TextView newsHealth;
    private RelativeLayout relaytiveNewsLaws;
    private ImageView iconNewsLaws;
    private TextView newsLaws;
    private RelativeLayout relaytiveNewsEntertain;
    private ImageView iconNewsEntertain;
    private TextView newsEntertain;
    private RelativeLayout relaytiveNewsSports;
    private ImageView iconNewsSports;
    private TextView newsSports;
    private RelativeLayout relaytiveContinues,relaytive_thugon;
    private TextView tvContinues;
    private RelativeLayout relaytiveHead2;
    private RelativeLayout relaytiveNewsEducation;
    private ImageView iconNewsEducation;
    private TextView newsEducation;
    private RelativeLayout relaytiveNewsScience;
    private ImageView iconNewsScience;
    private TextView newsScience;
    private RelativeLayout relaytiveNewsBussiness;
    private ImageView iconNewsBussiness;
    private TextView newsBussiness;
    private RelativeLayout relaytiveNewsXe;
    private ImageView iconNewsXe;
    private TextView newsXe;
    private RelativeLayout relaytiveNewsTravel;
    private ImageView iconNewsTravel;
    private TextView newsTravel;
    private RelativeLayout relaytiveNewsFun;
    private ImageView iconNewsFun;
    private TextView newsFun;
    private RelativeLayout relaytiveNews1;
    private TextView news1Title;
    private TextView news1Date;
    private RelativeLayout relaytiveNews2;
    private TextView news2Title;
    private TextView news2Date;
    private RelativeLayout relaytiveNews3;
    private TextView news3Title;
    private TextView news3Date;
    private ImageView iView1,iView2,iView3;

    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid"; // not used
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListAdapter adapter;
    public NewsFragment() {
        // Required empty public constructor
    }


    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String rss_link = "http://vnexpress.net/rss/tin-moi-nhat.rss";

        /**
         * Calling a backgroung thread will loads recent articles of a website
         * @param rss url of website
         * */
        new loadRSSFeedItems().execute(rss_link);
        initComponent(view);
    }

    private void initComponent(View view) {
        newsHead = (RelativeLayout) view.findViewById(R.id.news_head);
        relaytiveNewsWorld = (RelativeLayout) view.findViewById(R.id.relaytive_news_world);
        iconNewsWorld = (ImageView) view.findViewById(R.id.icon_news_world);
        newsWorld = (TextView) view.findViewById(R.id.news_world);
        relaytiveNewsEvents = (RelativeLayout) view.findViewById(R.id.relaytive_news_events);
        iconNewsEvents = (ImageView) view.findViewById(R.id.icon_news_events);
        newsEvents = (TextView) view.findViewById(R.id.news_events);
        relaytiveNewsHealth = (RelativeLayout) view.findViewById(R.id.relaytive_news_health);
        iconNewsHealth = (ImageView) view.findViewById(R.id.icon_news_health);
        newsHealth = (TextView) view.findViewById(R.id.news_health);
        relaytiveNewsLaws = (RelativeLayout) view.findViewById(R.id.relaytive_news_laws);
        iconNewsLaws = (ImageView) view.findViewById(R.id.icon_news_laws);
        newsLaws = (TextView) view.findViewById(R.id.news_laws);
        relaytiveNewsEntertain = (RelativeLayout) view.findViewById(R.id.relaytive_news_entertain);
        iconNewsEntertain = (ImageView) view.findViewById(R.id.icon_news_entertain);
        newsEntertain = (TextView) view.findViewById(R.id.news_entertain);
        relaytiveNewsSports = (RelativeLayout) view.findViewById(R.id.relaytive_news_sports);
        iconNewsSports = (ImageView) view.findViewById(R.id.icon_news_sports);
        newsSports = (TextView) view.findViewById(R.id.news_sports);
        relaytiveContinues = (RelativeLayout) view.findViewById(R.id.relaytive_continues);
        tvContinues = (TextView) view.findViewById(R.id.tv_continues);
        relaytiveHead2 = (RelativeLayout) view.findViewById(R.id.relaytive_head_2);
        relaytiveNewsEducation = (RelativeLayout) view.findViewById(R.id.relaytive_news_education);
        iconNewsEducation = (ImageView) view.findViewById(R.id.icon_news_education);
        newsEducation = (TextView) view.findViewById(R.id.news_education);
        relaytiveNewsScience = (RelativeLayout) view.findViewById(R.id.relaytive_news_science);
        iconNewsScience = (ImageView) view.findViewById(R.id.icon_news_science);
        newsScience = (TextView) view.findViewById(R.id.news_science);
        relaytiveNewsBussiness = (RelativeLayout) view.findViewById(R.id.relaytive_news_bussiness);
        iconNewsBussiness = (ImageView) view.findViewById(R.id.icon_news_bussiness);
        newsBussiness = (TextView) view.findViewById(R.id.news_bussiness);
        relaytiveNewsXe = (RelativeLayout) view.findViewById(R.id.relaytive_news_xe);
        iconNewsXe = (ImageView) view.findViewById(R.id.icon_news_xe);
        newsXe = (TextView) view.findViewById(R.id.news_xe);
        relaytiveNewsTravel = (RelativeLayout) view.findViewById(R.id.relaytive_news_travel);
        iconNewsTravel = (ImageView) view.findViewById(R.id.icon_news_travel);
        newsTravel = (TextView) view.findViewById(R.id.news_travel);
        relaytiveNewsFun = (RelativeLayout) view.findViewById(R.id.relaytive_news_fun);
        iconNewsFun = (ImageView) view.findViewById(R.id.icon_news_fun);
        newsFun = (TextView) view.findViewById(R.id.news_fun);
        relaytiveNews1 = (RelativeLayout) view.findViewById(R.id.relaytive_news_1);
        news1Title = (TextView) view.findViewById(R.id.news_1_Title);
        news1Date = (TextView) view.findViewById(R.id.news_1_Date);
        relaytiveNews2 = (RelativeLayout) view.findViewById(R.id.relaytive_news_2);
        news2Title = (TextView) view.findViewById(R.id.news_2_Title);
        news2Date = (TextView) view.findViewById(R.id.news_2_Date);
        relaytiveNews3 = (RelativeLayout) view.findViewById(R.id.relaytive_news_3);
        news3Title = (TextView) view.findViewById(R.id.news_3_Title);
        news3Date = (TextView) view.findViewById(R.id.news_3_Date);
        iView1 = (ImageView) view.findViewById(R.id.image_news_1);
        iView2 = (ImageView) view.findViewById(R.id.image_news_2);
        iView3 = (ImageView) view.findViewById(R.id.image_news_3);
        relaytive_thugon = (RelativeLayout) view.findViewById(R.id.relaytive_thugon);
        relaytiveContinues.setOnClickListener(this);
        relaytive_thugon.setOnClickListener(this);
        relaytiveNewsBussiness.setOnClickListener(this);
        relaytiveNewsEducation.setOnClickListener(this);
        relaytiveNewsEntertain.setOnClickListener(this);
        relaytiveNewsEvents.setOnClickListener(this);
        relaytiveNewsFun.setOnClickListener(this);
        relaytiveNewsHealth.setOnClickListener(this);
        relaytiveNewsLaws.setOnClickListener(this);
        relaytiveNewsScience.setOnClickListener(this);
        relaytiveNewsSports.setOnClickListener(this);
        relaytiveNewsTravel.setOnClickListener(this);
        relaytiveNewsWorld.setOnClickListener(this);
        relaytiveNewsXe.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.relaytive_news_health:
                break;
            case R.id.relaytive_news_bussiness:
                break;
            case R.id.relaytive_news_science:
                break;
            case R.id.relaytive_news_sports:
                break;
            case R.id.relaytive_news_world:
                break;
            case R.id.relaytive_news_travel:
                break;
            case R.id.relaytive_news_fun:
                break;
            case R.id.relaytive_news_education:
                break;
            case R.id.relaytive_news_events:
                break;
            case R.id.relaytive_news_entertain:
                break;
            case R.id.relaytive_news_laws:
                break;
            case R.id.relaytive_news_xe:
                break;
            case R.id.relaytive_thugon:
                relaytive_thugon.setVisibility(View.GONE);
                relaytiveHead2.setVisibility(View.GONE);
                relaytiveContinues.setVisibility(View.VISIBLE);
                break;
            case R.id.relaytive_continues:
                relaytive_thugon.setVisibility(View.VISIBLE);
                relaytiveHead2.setVisibility(View.VISIBLE);
                relaytiveContinues.setVisibility(View.GONE);
                break;
        }

    }

    class 	loadRSSFeedItems extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(
                    getContext());
            pDialog.setMessage("Loading recent articles...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting all recent articles and showing them in listview
         * */
        @Override
        protected String doInBackground(String... args) {
            // rss link url
            String rss_url = args[0];

            // list of rss items
            rssItems = rssParser.getRSSFeedItems(rss_url);

            // looping through each item
            for(RSSItem item : rssItems){
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_TITLE, item.getTitle());
                map.put(TAG_LINK, item.getLink());
                map.put(TAG_PUB_DATE, item.getPubdate()); // If you want parse the date
                String description = item.getDescription();
                // taking only 200 chars from description
                if(description.length() > 100){
                    description = description.substring(0, 97) + "..";
                }
                map.put(TAG_DESRIPTION, description);

                // adding HashList to ArrayList
                rssItemList.add(map);
            }

            // updating UI from Background Thread

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

            Glide.with(getContext())
                    .load(rssItems.get(0).getDescription())
                    .centerCrop()
                    .into(iView1);
            news1Title.setText(rssItems.get(0).getTitle());
            news1Date.setText(rssItems.get(0).getPubdate());
            Glide.with(getContext())
                    .load(rssItems.get(1).getDescription())
                    .centerCrop()
                    .into(iView2);
            news2Title.setText(rssItems.get(1).getTitle());
            news2Date.setText(rssItems.get(1).getPubdate());
            Glide.with(getContext())
                    .load(rssItems.get(2).getDescription())
                    .centerCrop()
                    .into(iView3);
            news3Title.setText(rssItems.get(2).getTitle());
            news3Date.setText(rssItems.get(2).getPubdate());
            relaytiveNews1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getContext(), DisPlayWebPageActivity.class);

                    // getting page url
                    String page_url = rssItems.get(0).getLink();
                    in.putExtra("page_url", page_url);
                    startActivity(in);
                }
            });
            relaytiveNews2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getContext(), DisPlayWebPageActivity.class);

                    // getting page url
                    String page_url = rssItems.get(1).getLink();
                    in.putExtra("page_url", page_url);
                    startActivity(in);
                }
            });
            relaytiveNews3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getContext(), DisPlayWebPageActivity.class);

                    // getting page url
                    String page_url = rssItems.get(2).getLink();
                    in.putExtra("page_url", page_url);
                    startActivity(in);
                }
            });
        }
    }
}
