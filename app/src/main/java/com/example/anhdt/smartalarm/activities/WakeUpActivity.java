package com.example.anhdt.smartalarm.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.provider.Settings;
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
import android.widget.Toast;

import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.adapters.PagerAdapter;
import com.example.anhdt.smartalarm.adapters.RSSParser;
import com.example.anhdt.smartalarm.database.Database;
import com.example.anhdt.smartalarm.fragments.NewsFragment;
import com.example.anhdt.smartalarm.models.RSSItem;
import com.example.anhdt.smartalarm.models.Weather;
import com.example.anhdt.smartalarm.services.GPSTracker;
import com.example.anhdt.smartalarm.utils.JSONWeatherParser;
import com.example.anhdt.smartalarm.utils.WeatherHttpClient;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.anhdt.smartalarm.R.id.relaytive_thugon;

public class WakeUpActivity extends AppCompatActivity implements View.OnClickListener , GPSTracker.SettingIntent{


    private static final String SPEED = "Tốc độ gió : ";
    private static final String HUMIDITY = "Đô ẩm : ";
    private static final String TEMP = " Độ C";

    private Map<String,String> weatherCondition = new HashMap<String, String>();
    private Map<String,String> weatherConditionDescription = new HashMap<String, String>();
    private ImageView imv_update;
    private TextView txv_update;
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
    private RelativeLayout relaytiveContinuesHome,relaytive_Home;
    private TextView tvContinuesHome;
    RSSParser rssParser = new RSSParser();

    private GPSTracker gpsTracker;

    List<RSSItem> rssItems = new ArrayList<RSSItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake_up);

        gpsTracker = new GPSTracker(this, this, this);
        relaytive_Home = (RelativeLayout) findViewById(R.id.relaytive_Home);
        imv_update = (ImageView) findViewById(R.id.imv_reload);
        txv_update = (TextView) findViewById(R.id.txv_update);
        txvTemperature = (TextView) findViewById(R.id.txv_temperature);
        imvWeather = (ImageView) findViewById(R.id.imv_weather);
        imvLocation = (ImageView) findViewById(R.id.imv_location);
        txvLocationName = (TextView) findViewById(R.id.txv_location_name);
        txvDescription = (TextView) findViewById(R.id.txv_description);
        txvHumidity = (TextView) findViewById(R.id.txv_humidity);
        txvSpeed = (TextView) findViewById(R.id.txv_speed);

        textView = (TextView) findViewById(R.id.textView);
        linerWeather = (RelativeLayout) findViewById(R.id.liner_weather);
        linerLocation = (LinearLayout) findViewById(R.id.liner_location);
        relativeTemper = (RelativeLayout) findViewById(R.id.relative_temper);
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
        relaytive_Home.setOnClickListener(this);
        relaytiveNews3.setOnClickListener(this);
        relaytiveNews2.setOnClickListener(this);
        relaytiveNews1.setOnClickListener(this);
        initHashMapWeatherConditon();
        initHashMapWeatherConditionDescriotion();
        init();

        if(isNetworkConnected()){
            String city = "Ha Noi, VN";
            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{city});
        }
        else {
            Database.init(this);
            Weather weather1 = Database.getWeather();
            if(weather1!=null){
                if (weather1.currentCondition.getIcon() != null) {
                    Picasso.with(this)
                            .load("http://openweathermap.org/img/w/" + weather1.currentCondition.getIcon() + ".png")
                            .into(imvWeather);
                }
                txvLocationName.setText(weather1.location.getCity() + "," + weather1.location.getCountry());
                txvDescription.setText(weatherCondition.get(weather1.currentCondition.getCondition()) + "(" + weatherConditionDescription.get(weather1.currentCondition.getDescr()) + ")");
                txvTemperature.setText("" + Math.round((weather1.temperature.getTemp() - 273.15)) + " độ C");
                txvHumidity.setText(HUMIDITY + "" + weather1.currentCondition.getHumidity() + "%");
                txvSpeed.setText(SPEED + "" + weather1.wind.getSpeed() + " mps");
            }
            Toast.makeText(this,"Kiểm tra kết nối Internet của bạn",Toast.LENGTH_SHORT).show();

        }
    }

    private void initHashMapWeatherConditon(){
        weatherCondition.put("Thunderstorm","Bão");
        weatherCondition.put("Drizzle","Mưa phùn");
        weatherCondition.put("Rain","Mưa");
        weatherCondition.put("Snow","Tuyết");
        weatherCondition.put("Atmosphere","Khí trời");
        weatherCondition.put("Clear","Trog sạch");
        weatherCondition.put("Extreme","Cực đoan");
        weatherCondition.put("Clouds","Mây");
        weatherCondition.put("Additional","Bổ sung");
    }
    private void initHashMapWeatherConditionDescriotion(){
        weatherConditionDescription.put("thunderstorm with light rain","Bão có mưa nhẹ");
        weatherConditionDescription.put("thunderstorm with rain","Cơn bão với mưa");
        weatherConditionDescription.put("thunderstorm with heavy rain","Bão có mưa lớn");
        weatherConditionDescription.put("light thunderstorm","Sấm sét nhẹ");
        weatherConditionDescription.put("thunderstorm","Dông");
        weatherConditionDescription.put("heavy thunderstorm","Cơn bão mạnh");
        weatherConditionDescription.put("ragged thunderstorm","Dông bão");
        weatherConditionDescription.put("thunderstorm with light drizzle","Bão có mưa phùn");
        weatherConditionDescription.put("thunderstorm with drizzle","Bão có mưa phùn");
        weatherConditionDescription.put("thunderstorm with heavy drizzle","Bão có mưa lớn");
        weatherConditionDescription.put("light intensity drizzle","Mưa phùn trời sáng");
        weatherConditionDescription.put("drizzle","Mưa phùn");
        weatherConditionDescription.put("heavy intensity drizzle","Mưa phùn cường độ nặng");
        weatherConditionDescription.put("light intensity drizzle rain","Cường độ mưa phùn nhẹ");
        weatherConditionDescription.put("drizzle rain","Mưa phùn");
        weatherConditionDescription.put("heavy intensity drizzle rain","Cường độ mưa phùn lớn");
        weatherConditionDescription.put("shower rain and drizzle","Mưa vừa và mưa phùn");
        weatherConditionDescription.put("heavy shower rain and drizzle","Mưa lớn và mưa phùn");
        weatherConditionDescription.put("shower drizzle","Mưa vừa");
        weatherConditionDescription.put("light rain","Mưa nhỏ");
        weatherConditionDescription.put("moderate rain","Mưa vừa");
        weatherConditionDescription.put("heavy intensity rain","Mưa cường độ lớn");
        weatherConditionDescription.put("very heavy rain","Mưa rất lớn");
        weatherConditionDescription.put("extreme rain","Thời tiết mưa xấu");
        weatherConditionDescription.put("freezing rain","Mưa lạnh");
        weatherConditionDescription.put("light intensity shower rain","Mưa rào");
        weatherConditionDescription.put("shower rain","Mưa nặng hạt");
        weatherConditionDescription.put("heavy intensity shower rain","Cường độ mưa lớn");
        weatherConditionDescription.put("ragged shower rain","Mưa rào");
        weatherConditionDescription.put("light snow","Tuyết nhẹ");
        weatherConditionDescription.put("snow","Tuyết");
        weatherConditionDescription.put("heavy snow","Tuyết rơi nhiều");
        weatherConditionDescription.put("sleet","Mưa tuyết");
        weatherConditionDescription.put("shower sleet","Mưa đá");
        weatherConditionDescription.put("light rain and snow","Mưa nhẹ kèm theo tuyết rơi");
        weatherConditionDescription.put("rain and snow","Mưa và xuất hiện tuyết rơi");
        weatherConditionDescription.put("light shower snow","Mưa tuyết nhẹ");
        weatherConditionDescription.put("shower snow","Mưa tuyết");
        weatherConditionDescription.put("heavy shower snow","Mưa tuyết nặng");
        weatherConditionDescription.put("mist","Sương mù");
        weatherConditionDescription.put("smoke","Khói");
        weatherConditionDescription.put("haze","Sương mù");
        weatherConditionDescription.put("sand, dust whirls","Cát và bụi xoáy");
        weatherConditionDescription.put("fog","Sương mù");
        weatherConditionDescription.put("sand","Cát");
        weatherConditionDescription.put("dust","Bụi bặm");
        weatherConditionDescription.put("volcanic ash","Tro núi lửa");
        weatherConditionDescription.put("squalls","Gió giật");
        weatherConditionDescription.put("tornado","Vòi rồng");
        weatherConditionDescription.put("clear sky","Bầu trời trong xanh");
        weatherConditionDescription.put("few clouds","Mây nhẹ");
        weatherConditionDescription.put("scattered clouds","Trời quang mây");
        weatherConditionDescription.put("broken clouds","Không có mây");
        weatherConditionDescription.put("overcast clouds","Nhiều mây u ám");
        weatherConditionDescription.put("tropical storm","Bão nhiệt đới");
        weatherConditionDescription.put("hurricane","Bão");
        weatherConditionDescription.put("cold","Lạnh");
        weatherConditionDescription.put("hot","Nóng");
        weatherConditionDescription.put("windy","gió");
        weatherConditionDescription.put("hail","Mưa đá");
        weatherConditionDescription.put("calm","yên tĩnh");
        weatherConditionDescription.put("light breeze","gió nhẹ");
        weatherConditionDescription.put("gentle breeze","gió nhẹ nhàng");
        weatherConditionDescription.put("moderate breeze","gió vừa phải");
        weatherConditionDescription.put("fresh breeze","gió nhẹ");
        weatherConditionDescription.put("strong breeze","gió to");
        weatherConditionDescription.put("high wind, near gale","gió lớn gần cơn bão");
        weatherConditionDescription.put("gale","cơn lốc");
        weatherConditionDescription.put("severe gale","cơn bão mạnh");
        weatherConditionDescription.put("storm","bão");
        weatherConditionDescription.put("violent storm","cơn bão dữ dội");
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
            case R.id.relaytive_Home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.imv_reload:
//                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//                } else {

                gpsTracker = new GPSTracker(this, this, this);

                // Check if GPS enabled
                if (gpsTracker.canGetLocation()) {

//                        double latitude = gpsTracker.getLatitude();
//                        double longitude = gpsTracker.getLongitude();
//
//
//                        Log.i("TOADO", latitude + " " + longitude);
//                        Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
//                        List<Address> addresses = null;
//                        try {
//                            addresses = gcd.getFromLocation(latitude, longitude, 1);
//                            if (addresses.size() > 0)
//                            {
//                                System.out.println(addresses.get(0).getLocality());
//                                Log.i("place",addresses.get(0).getLocality());
//                                JSONWeatherTask task = new JSONWeatherTask();
//                                task.execute(new String[]{addresses.get(0).getLocality()});
//                            }
//                            else
//                            {
//                                // do your staff
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    String city = "Ha Noi, VN";
                    JSONWeatherTask task = new JSONWeatherTask();
                    task.execute(new String[]{city});

                } else {
                    gpsTracker.showSettingsAlert();
                }
                //}

                break;

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
    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);
                // Let's retrieve the icon
                weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (gpsTracker.canGetLocation() == false) {

                Database.init(WakeUpActivity.this);
                Weather weather1 = Database.getWeather();
                if(weather1!=null){
                    if (weather1.currentCondition.getIcon() != null) {
                        Picasso.with(WakeUpActivity.this)
                                .load("http://openweathermap.org/img/w/" + weather1.currentCondition.getIcon() + ".png")
                                .into(imvWeather);
                    }
                    txvLocationName.setText(weather1.location.getCity() + "," + weather1.location.getCountry());
                    txvDescription.setText(weatherCondition.get(weather1.currentCondition.getCondition()) + "(" + weatherConditionDescription.get(weather1.currentCondition.getDescr()) + ")");
                    txvTemperature.setText("" + Math.round((weather1.temperature.getTemp() - 273.15)) + " độ C");
                    txvHumidity.setText(HUMIDITY + "" + weather1.currentCondition.getHumidity() + "%");
                    txvSpeed.setText(SPEED + "" + weather1.wind.getSpeed() + " mps");
                    txv_update.setText("Cập nhật lúc : " + weather1.currentCondition.getTime());
                }
                Toast.makeText(WakeUpActivity.this,"Kiểm tra kết nối Internet của bạn",Toast.LENGTH_SHORT).show();
            }
            else {
                Log.i("Haiz", "@@");
                if (weather.currentCondition.getIcon() != null) {
                    //Bitmap img = BitmapFactory.decodeByteArray(weather.currentCondition.getIcon(), 0, weather.iconData.length);
                    //imv_weather.setImageBitmap(img);
                    Picasso.with(WakeUpActivity.this)
                            .load("http://openweathermap.org/img/w/" + weather.currentCondition.getIcon() + ".png")
                            .into(imvWeather);
                    Log.i("ICON", weather.currentCondition.getIcon() +"");
                }
                txvLocationName.setText(weather.location.getCity() + "," + weather.location.getCountry());
                txvDescription.setText(weatherCondition.get(weather.currentCondition.getCondition()) + "(" + weatherConditionDescription.get(weather.currentCondition.getDescr()) + ")");
                txvTemperature.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + " độ C");
                txvHumidity.setText(HUMIDITY + "" + weather.currentCondition.getHumidity() + "%");
//            press.setText("" + weather.currentCondition.getPressure() + " hPa");
                txvSpeed.setText(SPEED + "" + weather.wind.getSpeed() + " mps");
//            windDeg.setText("" + weather.wind.getDeg() + "�");

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                weather.currentCondition.setTime(sdf.format(c.getTime()));

                txv_update.setText("Cập nhật lúc : " + sdf.format(c.getTime()));

                //weather.currentCondition.setTime("12:00");
                Log.i("Time",weather.currentCondition.getTime());
                Database.init(WakeUpActivity.this);
                Weather weather1 = Database.getWeather();
                if(weather1 == null){
                    Log.i("create","@@");
                    Database.createWeather(weather);
                }
                else {
                    Log.i("update","@@");
                    Database.updateWeather(weather);
                }
            }
        }
    }
    @Override
    public void setOnShowSettingIntent() {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}
