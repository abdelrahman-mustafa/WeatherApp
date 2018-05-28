package com.example.App.weatherApp.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.App.weatherApp.Adaptors.CitiesAdaptor;
import com.example.App.weatherApp.Model.CityDetail;
import com.example.App.weatherApp.Utilities.AppController;
import com.example.App.weatherApp.Utilities.ClickListener;
import com.example.ngoctri.mapdirectionsample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cities extends AppCompatActivity implements CitiesAdaptor.ContactsAdapterListener {


    private RecyclerView recyclerView;
    private CitiesAdaptor citiesAdaptor;
    private List<CityDetail> list;
    private RelativeLayout mRelativeLayout;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        recyclerView = findViewById(R.id.userProfile_pho_recycler_view);


        mRelativeLayout = (RelativeLayout) findViewById(R.id.Content_homePage);
        list = new ArrayList<>();
        citiesAdaptor = new CitiesAdaptor(list, getApplicationContext(), this);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(citiesAdaptor);
        EditText etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // citiesAdaptor.filter(s);
                citiesAdaptor.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                citiesAdaptor.getFilter().filter(s);

            }
        });


        String url = "http://api.openweathermap.org/data/2.5/box/city?bbox=24.982910,22.044913,34.365234,31.634676,10&appid=cf52422ddd592e0b6eaf2b087fb48188";
        JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("g", response.toString());

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("list");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("ga", jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.d("ga", jsonObject.toString());
                                CityDetail cityDetail = new CityDetail(jsonObject.getString("name"), jsonObject.getJSONObject("main").getString("temp"), jsonObject.getJSONObject("main").getString("temp_min"), jsonObject.getJSONObject("main").getString("temp_max"), jsonObject.getJSONObject("main").getString("pressure"), jsonObject.getJSONObject("wind").getString("speed"), jsonObject.getJSONObject("wind").getString("deg"), jsonObject.getJSONObject("main").getString("humidity"), jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                                Log.d("pr", cityDetail.toString());
                                list.add(cityDetail);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            citiesAdaptor.notifyDataSetChanged();


                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);

        // setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
                recyclerView, new ClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well

                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.popup, null);


                AlertDialog.Builder builder = new AlertDialog.Builder(Cities.this);
                builder.setView(customView);

                AlertDialog dialog0 = builder.create();
                dialog0.show();


                TextView name = (TextView) customView.findViewById(R.id.name);
                TextView minT = (TextView) customView.findViewById(R.id.minTemp);
                TextView maxT = (TextView) customView.findViewById(R.id.maxTemp);
                TextView windD = (TextView) customView.findViewById(R.id.windDg);
                TextView windSp = (TextView) customView.findViewById(R.id.windSp);
                TextView weatDe = (TextView) customView.findViewById(R.id.weatheDes);
                TextView hum = (TextView) customView.findViewById(R.id.humidity);
                TextView pre = (TextView) customView.findViewById(R.id.pres);

                pre.setText("Pressure:" + list.get(position).getPreasure() + ",Temp " + list.get(position).getCurrentTemp());
                minT.setText("Min Temp : " + list.get(position).getMinTemp());
                maxT.setText("Max Temp : " + list.get(position).getMaxTemp());
                weatDe.setText("weather : " + list.get(position).getWeatherDes());
                windSp.setText("Wind speed : " + list.get(position).getWindSp());
                windD.setText("Wind degree : " + list.get(position).getWindDeg());
                hum.setText("Humidity : " + list.get(position).getHumidity());
                name.setText(list.get(position).getName());


            }

            @Override
            public void onLongClick(View view, int position) {


            }
        }));
        recyclerView.setAdapter(citiesAdaptor);

    }


    @Override
    public void onContactSelected(CityDetail contact) {

    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(Cities.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_pro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id != 0) {
            startActivity(new Intent(Cities.this, MapsActivity.class));
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
