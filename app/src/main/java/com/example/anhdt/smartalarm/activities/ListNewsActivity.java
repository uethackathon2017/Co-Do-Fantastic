package com.example.anhdt.smartalarm.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.adapters.RSSParser;
import com.example.anhdt.smartalarm.models.RSSFeed;
import com.example.anhdt.smartalarm.models.RSSItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 11/03/2017.
 */

public class ListNewsActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Array list for list view
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String,String>>();

    RSSParser rssParser = new RSSParser();

    // button add new website
    ImageButton btnAddSite;

    List<RSSItem> rssItems = new ArrayList<RSSItem>();

    RSSFeed rssFeed;
    ImageView btn_back;
    TextView Title;
    String link_Title,text_Title;

    public ListNewsActivity() {


    }

    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid"; // not used

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_news_list);


        // get intent data
        // SQLite Row id
        // Getting Single website from SQLite

        Title = (TextView) findViewById(R.id.title_Page) ;
        Title.setText(getIntent().getStringExtra("Title"));
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        String rss_link = getIntent().getStringExtra("page_url");
        new loadRSSFeedItems().execute(rss_link);

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent in = new Intent(getApplicationContext(), DisPlayWebPageActivity.class);

                // getting page url
                String page_url = ((TextView) view.findViewById(R.id.page_url)).getText().toString();
                String news_Title = ((TextView) view.findViewById(R.id.news_Title)).getText().toString();
                in.putExtra("page_url", page_url);
                in.putExtra("Title",getIntent().getStringExtra("Title"));

                startActivity(in);
            }
        });
    }

    /**
     * Background Async Task to get RSS Feed Items data from URL
     * */
    class 	loadRSSFeedItems extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(
                    ListNewsActivity.this);
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
                if (description.contains("_180x108")) {
                    description = description.replace("_180x108", "");
                    item.setDescription(description);
                }
                map.put(TAG_DESRIPTION, description);

                // adding HashList to ArrayList
                rssItemList.add(map);
            }

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed items into listview
                     * */
                    SimpleAdapter adapter = new SimpleAdapter(
                            ListNewsActivity.this,
                            rssItemList, R.layout.rss_item_list_row,
                            new String[] { TAG_LINK, TAG_TITLE, TAG_DESRIPTION },
                            new int[] { R.id.page_url, R.id.news_Title , R.id.image_news });

                    // updating listview

                    adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Object data, String textRepresentation) {
                            if(view.getId() == R.id.image_news) {
                                ImageView imageView = (ImageView) view;
                                Picasso.with(ListNewsActivity.this)
                                        .load((String)data)
                                        .resize(250,250)
                                        .centerCrop()
                                        .into(imageView);
                                return true;
                            }
                            return false;
                        }
                    });
                    setListAdapter(adapter);
                }
            });
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
    }
}
