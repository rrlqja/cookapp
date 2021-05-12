package com.example.cookapp1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class home extends AppCompatActivity {
    TextView textView1, textView2;
    Button button1, button2;
    SwipeRefreshLayout swipeRefreshLayout1;

    String userID;

    static int count = 0;
    static int length = 0;

    String cont_url = "http://15.165.241.115/db/cont.php";
    String scrollJson_url = "http://15.165.241.115/db/countselect.php";
    String onescrollJson_url = "http://15.165.241.115/db/onecountselect.php";

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
//    String order = "con_num";
    static int point_order = 0;

    String[] order_by = {"desc", "asc"};
    static int point_order_by = 0;

    //    String like_order = "con_num";
    String[] like_order = {"con_num", "mlike"};
//    String like_order = "desc";
    static int point_like_order = 1;

    String[] like_order_by = {"desc", "asc"};
    static int point_like_order_by = 0;

    String input_order;
    String input_order_by;

    getJSON getJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textView2 = findViewById(R.id.tx2);
        button1 = findViewById(R.id.bt1);
        button2 = findViewById(R.id.bt2);
        swipeRefreshLayout1 = findViewById(R.id.swip1);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userid");

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getJSON = new getJSON();
        getJSON.execute(scrollJson_url, String.valueOf(count), order[point_order], order_by[point_order_by], like_order[point_like_order], like_order_by[point_like_order_by]);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initAdapter();
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

        getJSON = new getJSON();
        getJSON.execute(scrollJson_url, String.valueOf(count), order[point_order], order_by[point_order_by], like_order[point_like_order], like_order_by[point_like_order_by]);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initAdapter();
            }
        }, 1500);

        Toast.makeText(home.this, "새로고침중", Toast.LENGTH_LONG).show();
        initScrollListener();
    }

    public class getJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return getS(strings);
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

        private void getJ(String page) {

            try {
                JSONObject json = new JSONObject(page);

                JSONArray jArr = json.getJSONArray("result");
//                JSONArray like_jArr = json.getJSONArray("like_result");

                JSONObject result_json;
//                JSONObject like_json;

                for (int i = 0; i < jArr.length(); i++) {

                    result_json = jArr.getJSONObject(i);
//                    like_json = like_jArr.getJSONObject(i);

                    recyclerItem = new RecyclerItem();
                    recyclerItem.setTitle(result_json.getString("title"));
                    recyclerItem.setWriter(result_json.getString("writer"));
                    recyclerItem.setCont(result_json.getString("cont"));
                    recyclerItem.setLike(result_json.getInt("mlike"));
                    recyclerItem.setDate(result_json.getString("con_date"));
                    recyclerItem.setNum(result_json.getInt("con_num"));
                    recyclerItemList.add(recyclerItem);
                    llist.add(recyclerItem);

//                    textView2.append(json.getString("title"));
                    textView2.setText(String.valueOf(recyclerItemList.size()));
                }

            } catch (Exception e) {

            }
        }

    }

//    public class more_getJSON extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            return getS(strings[0], strings[1]);
//        }
//
//        protected void onPostExecute(String result) {
//            count++;
//            getJ(result);
//        }
//
//        private String getS(String str, String str_count) {
//            HttpURLConnection conn = null;
//            String param = "count=" + str_count;
//            try {
//                URL url = new URL(str);
//                conn = (HttpURLConnection) url.openConnection();
//
//                conn.setReadTimeout(5000);
//                conn.setConnectTimeout(5000);
//                conn.setRequestMethod("POST");
//                conn.connect();
//
//                OutputStream outputStream = conn.getOutputStream();
//                outputStream.write(param.getBytes("UTF-8"));
//                outputStream.flush();
//                outputStream.close();
//
//                int response = conn.getResponseCode();
//
//                InputStream iStream;
//                if (response == HttpURLConnection.HTTP_OK) {
//                    iStream = conn.getInputStream();
//                } else {
//                    iStream = conn.getErrorStream();
//                }
//
//                InputStreamReader inputStreamReader = new InputStreamReader(iStream, "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                StringBuilder sb = new StringBuilder();
//                String line = "";
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                bufferedReader.close();
//
//                return sb.toString();
//
//            } catch (Exception e) {
//                return null;
//            }
//
//        }
//
//        private void getJ(String page) {
//
//            try {
//                JSONObject json = new JSONObject(page);
//
//                JSONArray jArr = json.getJSONArray("result");
//
//                for (int i = 0; i < jArr.length(); i++) {
//                    json = jArr.getJSONObject(i);
//
//                    recyclerItem = new RecyclerItem();
//                    recyclerItem.setTitle(json.getString("title"));
//                    recyclerItem.setWriter(json.getString("writer"));
//                    recyclerItem.setCont(json.getString("cont"));
//                    recyclerItemList.add(recyclerItem);
//                    llist.add(recyclerItem);
//
////                    textView2.append(json.getString("title"));
//                    textView2.setText(recyclerItemList.size());
//                }
//
//            } catch (Exception e) {
//
//            }
//        }
//    }
//    private void firstData() {
//        Log.d(TAG, "firstData:");
////        for(int a=0;a<30;a++) {
////            contList.add("Item " + a);
////            titleallList.add("Title " + a);
////            writerallList.add("Writer " + a);
////
////            recyclerItem = new RecyclerItem();
////            recyclerItem.setTitle(titleallList.get(a));
////            recyclerItem.setWriter(writerallList.get(a));
////            recyclerItem.setCont(contList.get(a));
////
////            recyclerItemList.add(recyclerItem);
////        }
//
//        for (int i = 0; i < 10; i++) {
//
//            llist.add(recyclerItemList.get(i));
//        }
//
//    }

    private void initAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(llist);
        recyclerView.setAdapter(recyclerViewAdapter);
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
//                        more_getJSON more_getJSON = new more_getJSON();
//                        more_getJSON.execute(scrollJson_url, String.valueOf(count));

                        isLoading = true;
                        Toast.makeText(home.this, "로딩중", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void dataMore() {
        Log.d(TAG, "dataMore: ");

        getJSON = new getJSON();
        getJSON.execute(scrollJson_url, String.valueOf(count), order[point_order], order_by[point_order_by], like_order[point_like_order], like_order_by[point_like_order_by]);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerViewAdapter.notifyItemChanged(llist.size());
                isLoading = false;
            }
        }, 2000);
//        llist.add(null);
//        recyclerViewAdapter.notifyItemInserted(llist.size() - 1);
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                llist.remove(llist.size() - 1);
//                int scrollPosition = llist.size();
//                recyclerViewAdapter.notifyItemRemoved(scrollPosition+1);
//                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
//                recyclerViewAdapter.notifyItemRemoved(scrollPosition+2);
//                recyclerViewAdapter.notifyItemRemoved(scrollPosition+3);
//
//                getJSON = new getJSON();
//                getJSON.execute(scrollJson_url, String.valueOf(count));
//
////                int currentSize = scrollPosition;
////                int nextLimit = currentSize + 10;
////                for (int i = currentSize; i < nextLimit; i++) {
////                    if (i == recyclerItemList.size()+10) {
////                        return;
////                    }
////
//////                    recyclerItem = new RecyclerItem();
//////                    recyclerItem.setTitle(titleallList.get(i));
//////                    recyclerItem.setWriter(writerallList.get(i));
//////                    recyclerItem.setCont(contList.get(i));
//////                    llist.add(recyclerItem);
////
////                }
//
//                recyclerViewAdapter.notifyDataSetChanged();
//                isLoading = false;
//            }
//        }, 2000);

    }

}
