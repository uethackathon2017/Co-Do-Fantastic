package com.example.anhdt.smartalarm.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.activities.DisPlayWebPageActivity;
import com.example.anhdt.smartalarm.activities.ListNewsActivity;
import com.example.anhdt.smartalarm.adapters.RSSParser;
import com.example.anhdt.smartalarm.database.Database;
import com.example.anhdt.smartalarm.models.RSSFeed;
import com.example.anhdt.smartalarm.models.RSSItem;
import com.example.anhdt.smartalarm.models.Weather;
import com.example.anhdt.smartalarm.services.GPSTracker;
import com.example.anhdt.smartalarm.utils.JSONWeatherParser;
import com.example.anhdt.smartalarm.utils.WeatherHttpClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class NewsFragment extends Fragment implements View.OnClickListener , GPSTracker.SettingIntent{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SPEED = "Tốc độ gió : ";
    private static final String HUMIDITY = "Đô ẩm : ";
    private static final String TEMP = " Độ C";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ProgressDialog pDialog;
    private Map<String,String> weatherCondition = new HashMap<String, String>();
    private Map<String,String> weatherConditionDescription = new HashMap<String, String>();
    private GPSTracker gpsTracker;

    // Array list for list view
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String,String>>();

    RSSParser rssParser = new RSSParser();

    List<RSSItem> rssItems = new ArrayList<RSSItem>();

    private TextView txv_temp;
    private TextView txv_humidity;
    private TextView txv_speed;
    private TextView txv_description;
    private TextView txv_locationName;
    private ImageView imv_weather;
    private ImageView imv_update;
    private TextView txv_update;

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
    private RelativeLayout relaytiveNewsFun,relaytive_continues_Home;
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
    private Activity activity;

    ListAdapter adapter;
    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
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
        //gpsTracker = new GPSTracker(getActivity(),getActivity(),this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gpsTracker = new GPSTracker(getActivity(),getActivity(),this);

        String rss_link = "http://vnexpress.net/rss/tin-moi-nhat.rss";

        new loadRSSFeedItems().execute(rss_link);
        initHashMapWeatherConditionDescriotion();
        initHashMapWeatherConditon();
        initComponent(view);
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

    private void initComponent(View view) {
        txv_temp = (TextView) view.findViewById(R.id.txv_temperature);
        txv_humidity = (TextView) view.findViewById(R.id.txv_humidity);
        txv_speed = (TextView) view.findViewById(R.id.txv_speed);
        txv_description = (TextView) view.findViewById(R.id.txv_description);
        txv_locationName = (TextView) view.findViewById(R.id.txv_location_name);
        imv_weather = (ImageView) view.findViewById(R.id.imv_weather);
        imv_update = (ImageView) view.findViewById(R.id.imv_reload);
        txv_update = (TextView) view.findViewById(R.id.txv_update);

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
        relaytiveNews2 = (RelativeLayout) view.findViewById(R.id.relaytive_news_2);
        news2Title = (TextView) view.findViewById(R.id.news_2_Title);
        relaytiveNews3 = (RelativeLayout) view.findViewById(R.id.relaytive_news_3);
        news3Title = (TextView) view.findViewById(R.id.news_3_Title);
        iView1 = (ImageView) view.findViewById(R.id.image_news_1);
        iView2 = (ImageView) view.findViewById(R.id.image_news_2);
        iView3 = (ImageView) view.findViewById(R.id.image_news_3);
        relaytive_thugon = (RelativeLayout) view.findViewById(R.id.relaytive_thugon);
        relaytive_continues_Home = (RelativeLayout) view.findViewById(R.id.relaytive_continues_Home);

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
        relaytive_continues_Home.setOnClickListener(this);
        imv_update.setOnClickListener(this);


        if(isNetworkConnected()){
            String city = "Ha Noi, VN";
            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{city});
        }
        else {
            Database.init(getActivity());
            Weather weather1 = Database.getWeather();
            if(weather1!=null){
                if (weather1.currentCondition.getIcon() != null) {
                    Picasso.with(getActivity())
                            .load("http://openweathermap.org/img/w/" + weather1.currentCondition.getIcon() + ".png")
                            .into(imv_weather);
                }
                txv_locationName.setText(weather1.location.getCity() + "," + weather1.location.getCountry());
                txv_description.setText(weatherCondition.get(weather1.currentCondition.getCondition()) + "(" + weatherConditionDescription.get(weather1.currentCondition.getDescr()) + ")");
                txv_temp.setText("" + Math.round((weather1.temperature.getTemp() - 273.15)) + " độ C");
                txv_humidity.setText(HUMIDITY + "" + weather1.currentCondition.getHumidity() + "%");
                txv_speed.setText(SPEED + "" + weather1.wind.getSpeed() + " mps");
                txv_update.setText("Cập nhật lúc : " + weather1.currentCondition.getTime());
            }
            Toast.makeText(getActivity(),"Kiểm tra kết nối Internet của bạn",Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onClick(View view) {
        Intent in;
        String page_url;
        switch (view.getId()){
            case R.id.imv_reload:
//                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//                } else {
//
//                    gpsTracker = new GPSTracker(getActivity(), getActivity(), this);
//
//                    // Check if GPS enabled
//                    if (gpsTracker.canGetLocation()) {
//                        Log.v("NewsFragment", "Ok");
////                        double latitude = gpsTracker.getLatitude();
////                        double longitude = gpsTracker.getLongitude();
////
////
////                        Log.i("TOADO", latitude + " " + longitude);
////                        Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
////                        List<Address> addresses = null;
////                        try {
////                            addresses = gcd.getFromLocation(latitude, longitude, 1);
////                            if (addresses.size() > 0)
////                            {
////                                System.out.println(addresses.get(0).getLocality());
////                                Log.i("place",addresses.get(0).getLocality());
////                                JSONWeatherTask task = new JSONWeatherTask();
////                                task.execute(new String[]{addresses.get(0).getLocality()});
////                            }
////                            else
////                            {
////                                // do your staff
////                            }
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
//                        String city = "Ha Noi, VN";
//                        JSONWeatherTask task = new JSONWeatherTask();
//                        task.execute(new String[]{city});
//
//                    } else {
//                        Log.v("NewsFragment", "No");
//                        gpsTracker.showSettingsAlert();
//                    }
//                }
                getLatLong();
                break;

            case R.id.relaytive_continues_Home:
                in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/tin-moi-nhat.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "VnExPressHome");
                startActivity(in);
                break;

            case R.id.relaytive_news_health:
                 in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                 page_url = "http://vnexpress.net/rss/suc-khoe.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Sức khỏe");
                startActivity(in);
                break;
            case R.id.relaytive_news_bussiness:
                 in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                 page_url = "http://vnexpress.net/rss/kinh-doanh.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Kinh doanh");
                startActivity(in);
                break;
            case R.id.relaytive_news_science:
                in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/khoa-hoc.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Khoa học");
                startActivity(in);
                break;
            case R.id.relaytive_news_sports:
                in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/the-thao.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Thể thao");
                startActivity(in);
                break;
            case R.id.relaytive_news_world:
                in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/the-gioi.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Thế giới");
                startActivity(in);
                break;
            case R.id.relaytive_news_travel:
                in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/du-lich.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Du lịch");
                startActivity(in);
                break;
            case R.id.relaytive_news_fun:
                in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/cuoi.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Cười");
                startActivity(in);
                break;
            case R.id.relaytive_news_education:
                in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/giao-duc.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Giáo dục");
                startActivity(in);
                break;
            case R.id.relaytive_news_events:
                in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/thoi-su.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Thời sự");
                startActivity(in);
                break;
            case R.id.relaytive_news_entertain:
                in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/giai-tri.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Giải trí");
                startActivity(in);
                break;
            case R.id.relaytive_news_laws:
                in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/phap-luat.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Luật");
                startActivity(in);
                break;
            case R.id.relaytive_news_xe:
                in = new Intent(getContext(), ListNewsActivity.class);

                // getting page url
                page_url = "http://vnexpress.net/rss/oto-xe-may.rss";
                in.putExtra("page_url", page_url);
                in.putExtra("Title", "Ôtô Xe máy");
                startActivity(in);
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
                Picasso.with(activity)
                        .load(rssItems.get(0).getDescription())
                        .resize(220,220)
                        .into(iView1);
                news1Title.setText(rssItems.get(0).getTitle());
                //news1Date.setText(rssItems.get(0).getPubdate());
                Picasso.with(activity)
                        .load(rssItems.get(1).getDescription())
                        .resize(220,220)
                        .centerCrop()
                        .into(iView2);
                news2Title.setText(rssItems.get(1).getTitle());
                //news2Date.setText(rssItems.get(1).getPubdate());
                Picasso.with(activity)
                        .load(rssItems.get(2).getDescription())
                        .resize(220,220)
                        .centerCrop()
                        .into(iView3);
                news3Title.setText(rssItems.get(2).getTitle());
                //news3Date.setText(rssItems.get(2).getPubdate());
                relaytiveNews1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(getContext(), DisPlayWebPageActivity.class);
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
                        Intent in = new Intent(getContext(), DisPlayWebPageActivity.class);
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
                        Intent in = new Intent(getContext(), DisPlayWebPageActivity.class);
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

                Database.init(getActivity());
                Weather weather1 = Database.getWeather();
                Log.i("Chan qua", "@@");
                if(weather1!=null){
                    if (weather1.currentCondition.getIcon() != null) {
                        Picasso.with(getActivity())
                                .load("http://openweathermap.org/img/w/" + weather1.currentCondition.getIcon() + ".png")
                                .into(imv_weather);
                        Log.i("ICON", weather.currentCondition.getIcon() +"");
                    }
                    txv_locationName.setText(weather1.location.getCity() + "," + weather1.location.getCountry());
                    txv_description.setText(weatherCondition.get(weather1.currentCondition.getCondition()) + "(" + weatherConditionDescription.get(weather1.currentCondition.getDescr()) + ")");
                    txv_temp.setText("" + Math.round((weather1.temperature.getTemp() - 273.15)) + " độ C");
                    txv_humidity.setText(HUMIDITY + "" + weather1.currentCondition.getHumidity() + "%");
                    txv_speed.setText(SPEED + "" + weather1.wind.getSpeed() + " mps");
                    txv_update.setText("Cập nhật lúc : " + weather1.currentCondition.getTime());
                    Log.i("database" , weather1.location.getCity());
                    Log.i("Time_database" , weather1.currentCondition.getTime());
                }
            }
            else {
                Log.i("Haiz", "@@");
                if (weather.currentCondition.getIcon() != null) {
                    //Bitmap img = BitmapFactory.decodeByteArray(weather.currentCondition.getIcon(), 0, weather.iconData.length);
                    //imv_weather.setImageBitmap(img);
                    Picasso.with(getActivity())
                            .load("http://openweathermap.org/img/w/" + weather.currentCondition.getIcon() + ".png")
                            .into(imv_weather);
                    Log.i("ICON", weather.currentCondition.getIcon() +"");
                }
                txv_locationName.setText(weather.location.getCity() + "," + weather.location.getCountry());
                txv_description.setText(weatherCondition.get(weather.currentCondition.getCondition()) + "(" + weatherConditionDescription.get(weather.currentCondition.getDescr()) + ")");
                txv_temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + " độ C");
                txv_humidity.setText(HUMIDITY + "" + weather.currentCondition.getHumidity() + "%");
//            press.setText("" + weather.currentCondition.getPressure() + " hPa");
                txv_speed.setText(SPEED + "" + weather.wind.getSpeed() + " mps");
//            windDeg.setText("" + weather.wind.getDeg() + "�");

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                weather.currentCondition.setTime(sdf.format(c.getTime()));

                txv_update.setText("Cập nhật lúc : " + sdf.format(c.getTime()));

                //weather.currentCondition.setTime("12:00");
                Log.i("Time",weather.currentCondition.getTime());
                Database.init(getActivity());
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

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.

                    gpsTracker = new GPSTracker(getActivity(), getActivity(), this);

                    // Check if GPS enabled
                    if (gpsTracker.canGetLocation()) {

                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();


                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gpsTracker.showSettingsAlert();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "You need to grant permission", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void getLatLong() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            gpsTracker = new GPSTracker(getActivity(), getActivity(), this);

            // Check if GPS enabled
            if (gpsTracker.canGetLocation()) {

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                Log.v("NewsFragment", latitude + " " + longitude);

                Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(latitude, longitude, 1);
                            if (addresses.size() > 0)
                            {
                                System.out.println(addresses.get(0).getLocality());
                                Log.i("place",addresses.get(0).getLocality());
                                JSONWeatherTask task = new JSONWeatherTask();
                                task.execute(new String[]{addresses.get(0).getLocality()});
                            }
                            else
                            {
                                // do your staff
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        String city = "Ha Noi, VN";
//                        JSONWeatherTask task = new JSONWeatherTask();
//                        task.execute(new String[]{city});

            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gpsTracker.showSettingsAlert();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //getLatLong();
        checkPermission();
    }
}
