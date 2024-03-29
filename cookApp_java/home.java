package com.example.cookapp1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class home extends AppCompatActivity {
    public static Activity _home;

    TextView textView1;

    Button button1, button2, button3;
    SwipeRefreshLayout swipeRefreshLayout1;
    ImageView imageView1;

    String userID;

    static int count = 0;

    String scrollJson_url = "http://15.165.241.115/db/countselect.php";
    String onescrollJson_url = "http://15.165.241.115/db/onecountselect.php";
    String original_url = "http://15.165.241.115";
    String search_url = "http://15.165.241.115/db/search_countselect.php";

    private static final String TAG = "home";
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    boolean isLoading = false;
    ArrayList<String> contList = new ArrayList<>();
    ArrayList<RecyclerItem> llist = new ArrayList<>();

    ArrayList<String> titleallList = new ArrayList<>();
    ArrayList<String> writerallList = new ArrayList<>();
    ArrayList<RecyclerItem> recyclerItemList = new ArrayList<>();
    RecyclerItem recyclerItem;

    String[] order = {"con_num", "mlike"};
    static int point_order = 0;

    String[] order_by = {"desc", "asc"};
    static int point_order_by = 0;

    String[] like_order = {"con_num", "mlike"};
    static int point_like_order = 1;

    String[] like_order_by = {"desc", "asc"};
    static int point_like_order_by = 0;

    getJSON getJSON;

    EditText searchet;
    String string_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent getintent = getIntent();
        userID = getintent.getStringExtra("userid");

        count = 0;
        titleallList.clear();
        writerallList.clear();
        contList.clear();
        recyclerItemList.clear();
        llist.clear();
        isLoading = false;
        string_search = "";

        _home = home.this;

        button1 = findViewById(R.id.bt1);
        button2 = findViewById(R.id.bt2);
        swipeRefreshLayout1 = findViewById(R.id.swip1);
        searchet = findViewById(R.id.searchet);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getJSON = new getJSON();
        getJSON.execute(scrollJson_url, String.valueOf(count), order[point_order], order_by[point_order_by], like_order[point_like_order], like_order_by[point_like_order_by], string_search);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initAdapter();
                refresh();
            }
        }, 1000);

        initScrollListener();

        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();

                swipeRefreshLayout1.setRefreshing(false);
            }
        });

    }

    public void search(View view) {

        if (searchet.getText().toString().trim().equals("") || searchet.getText().toString().trim() == null) {
            Toast.makeText(home.this, "값이 없음", Toast.LENGTH_LONG).show();
        } else {

            count = 0;
            titleallList.clear();
            writerallList.clear();
            contList.clear();
            recyclerItemList.clear();
            llist.clear();
            isLoading = false;

            string_search = searchet.getText().toString().trim();

            getJSON = new getJSON();
            getJSON.execute(search_url, String.valueOf(count), order[point_order], order_by[point_order_by], like_order[point_like_order], like_order_by[point_like_order_by], string_search);

        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initAdapter();
            }
        }, 2500);

        Toast.makeText(home.this, "검색중", Toast.LENGTH_LONG).show();
        initScrollListener();
    }

    public void sort_popular(View view) {

        point_order = 1;
        point_like_order = 0;

        if (point_order_by == 0) {
            refresh();
            point_order_by = 1;
        } else {
            refresh();
            point_order_by = 0;
        }

    }
    public void sort_latest(View view) {

        point_order = 0;
        point_like_order = 1;

        if (point_order_by == 0) {
            point_order_by = 1;
            refresh();
        }else{
            point_order_by = 0;
            refresh();
        }

    }

    public void refresh() {
        count = 0;
        titleallList.clear();
        writerallList.clear();
        contList.clear();
        recyclerItemList.clear();
        llist.clear();
        isLoading = false;

        searchet.setText(null);
        string_search = "";

        getJSON = new getJSON();
        getJSON.execute(scrollJson_url, String.valueOf(count), order[point_order], order_by[point_order_by], like_order[point_like_order], like_order_by[point_like_order_by], string_search);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initAdapter();
            }
        }, 2500);

        Toast.makeText(home.this, "새로고침중", Toast.LENGTH_LONG).show();
        initScrollListener();
    }

    public class getJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String search = strings[6];
            if (search.equals("") || search == null) {
                return getS(strings);
            } else {
                return getS_search(strings);
            }
        }

        protected void onPostExecute(String result) {
            count++;

            getJ(result);

        }

        private String getS(String[] strings){
            String str=strings[0];
            String str_count = strings[1];
            String order=strings[2];
            String order_by = strings[3];
            String like_order = strings[4];
            String like_order_by = strings[5];

            HttpURLConnection conn = null;
            String param = "count=" + str_count + "&order=" + order + "&order_by=" + order_by + "&like_order=" + like_order + "&like_order_by=" + like_order_by;
            try {
                URL url = new URL(str);
                conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.connect();

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(param.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int response = conn.getResponseCode();

                InputStream iStream;
                if (response == HttpURLConnection.HTTP_OK) {
                    iStream = conn.getInputStream();
                } else {
                    iStream = conn.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(iStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();

            } catch (Exception e) {
                return null;
            }

        }

        private String getS_search(String[] strings) {
            String str=strings[0];
            String str_count = strings[1];
            String order=strings[2];
            String order_by = strings[3];
            String like_order = strings[4];
            String like_order_by = strings[5];
            String search = strings[6];

            HttpURLConnection conn = null;
            String param = "count=" + str_count + "&order=" + order + "&order_by=" + order_by + "&like_order=" + like_order + "&like_order_by=" + like_order_by + "&search=" + search;
            try {
                URL url = new URL(str);
                conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.connect();

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(param.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int response = conn.getResponseCode();

                InputStream iStream;
                if (response == HttpURLConnection.HTTP_OK) {
                    iStream = conn.getInputStream();
                } else {
                    iStream = conn.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(iStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();

            } catch (Exception e) {
                return null;
            }
        }

        private void getJ(String page) {

            try {
                JSONObject json = new JSONObject(page);

                JSONArray jArr = json.getJSONArray("result");

                JSONObject result_json;

                for (int i = 0; i < jArr.length(); i++) {

                    result_json = jArr.getJSONObject(i);

                    recyclerItem = new RecyclerItem();
                    recyclerItem.setTitle(result_json.getString("title"));
                    recyclerItem.setWriter(result_json.getString("writer"));
                    recyclerItem.setCont(result_json.getString("cont"));
                    recyclerItem.setLike(result_json.getInt("mlike"));
                    recyclerItem.setDate(result_json.getString("con_date"));
                    recyclerItem.setNum(result_json.getInt("con_num"));
                    recyclerItem.setImg(original_url + result_json.getString("img_src").substring(2));

                    recyclerItemList.add(recyclerItem);

                    llist.add(recyclerItem);

                }

            } catch (Exception e) {

            }
        }

    }

    public class getBit extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String urli = strings[0];
            Bitmap bitmap = null;

            try {
                InputStream in = new URL(urli).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                return null;
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            imageView1.setImageBitmap(result);
        }
    }


    private void initAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(llist);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(home.this, contView.class);
                intent.putExtra("pos", llist.get(pos).getNum());
                intent.putExtra("position", Integer.parseInt(llist.get(pos).getNum()));
                intent.putExtra("userid", userID);
                startActivity(intent);

            }
        });
    }
    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView2, int newState) {
                super.onScrollStateChanged(recyclerView2, newState);
                Log.d(TAG, "onScrollStateChanged: ");
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView3, int dx, int dy) {
                super.onScrolled(recyclerView3, dx, dy);
                Log.d(TAG, "onSCrolled: ");

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView3.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == llist.size()-1) {

                        dataMore();

                        isLoading = true;
                        Toast.makeText(home.this, "로딩중", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void dataMore() {
        Log.d(TAG, "dataMore: ");

        string_search = searchet.getText().toString().trim();

        if (string_search.equals("") || string_search == null) {
            getJSON = new getJSON();
            getJSON.execute(scrollJson_url, String.valueOf(count), order[point_order], order_by[point_order_by], like_order[point_like_order], like_order_by[point_like_order_by], string_search);
        }else{
            getJSON = new getJSON();
            getJSON.execute(search_url, String.valueOf(count), order[point_order], order_by[point_order_by], like_order[point_like_order], like_order_by[point_like_order_by], string_search);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerViewAdapter.notifyItemChanged(llist.size());
                isLoading = false;
            }
        }, 2000);

    }

    public void createcont(View view) {
        Intent intent = new Intent(home.this, createCont.class);
        intent.putExtra("userid", userID);
        startActivity(intent);
    }

}
