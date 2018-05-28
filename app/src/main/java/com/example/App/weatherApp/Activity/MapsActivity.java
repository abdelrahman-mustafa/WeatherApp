package com.example.App.weatherApp.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.App.weatherApp.Model.CityDetail;
import com.example.App.weatherApp.Utilities.AppController;
import com.example.ngoctri.mapdirectionsample.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;
    List<CityDetail> list;
    List<LatLng> latList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.205753, 29.924526), 10));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(MapsActivity.this);

        String url = "http://api.openweathermap.org/data/2.5/box/city?bbox=24.982910,22.044913,34.365234,31.634676,10&appid=cf52422ddd592e0b6eaf2b087fb48188";
        JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("g", response.toString());
                        try {
                            latList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("list");
                            Log.d("ga", jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.d("ga", jsonObject.toString());

                                LatLng latLng = new LatLng(jsonObject.getJSONObject("coord").getDouble("Lat"), jsonObject.getJSONObject("coord").getDouble("Lon"));

                                IconGenerator iconFactory = new IconGenerator(MapsActivity.this);
                                addIcon(iconFactory, jsonObject.getJSONObject("main").getString("temp"), latLng);

                                /*    TextView text = new TextView(MapsActivity.this);
                                text.setText(jsonObject.getString("name"));

                                IconGenerator generator = new IconGenerator(MapsActivity.this);
                                generator.setContentView(text);
                                Bitmap icon = generator.makeIcon();

                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                               // markerOptions.icon((BitmapDescriptorFactory.fromBitmap(icon)));
                                markerOptions.title(jsonObject.getString("name"));
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mMap.addMarker(markerOptions).showInfoWindow();*/

                                list = new ArrayList<>();
                                latList.add(latLng);
                                Log.d("ga", jsonObject.toString());
                                CityDetail cityDetail = new CityDetail(jsonObject.getString("name"), jsonObject.getJSONObject("main").getString("temp"), jsonObject.getJSONObject("main").getString("temp_min"), jsonObject.getJSONObject("main").getString("temp_max"), jsonObject.getJSONObject("main").getString("pressure"), jsonObject.getJSONObject("wind").getString("speed"), jsonObject.getJSONObject("wind").getString("deg"), jsonObject.getJSONObject("main").getString("humidity"), jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                                Log.d("pr", cityDetail.toString());
                                list.add(cityDetail);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);

/*        ArrayList<LatLng> latLngs = new ArrayList<>();
        LatLng alex = new LatLng(31.205753, 29.924526);
        latLngs.add(alex);
        LatLng cairo = new LatLng(30.044281, 31.340002);
        latLngs.add(cairo);
        LatLng Jizah = new LatLng(30.00808, 31.21093);
        latLngs.add(Jizah);
        LatLng Qena = new LatLng(26.155061, 32.716012);
        latLngs.add(Qena);
        LatLng Aswan = new LatLng(24.09082, 32.89942);
        latLngs.add(Aswan);
        LatLng Assuit = new LatLng(27.180134, 31.189283);
        latLngs.add(Assuit);
        LatLng beni = new LatLng(29.066127 , 31.0833);
        latLngs.add(beni);
        LatLng fay = new LatLng( 29.308402 , 30.84285);
        latLngs.add(fay);
        LatLng benha = new LatLng(30.45906, 31.17858);
        latLngs.add(benha);

        for (int i = 0; i>latLngs.size();i++){
            Marker melbourne = mMap.addMarker(new MarkerOptions()
                    .position(latLngs.get(i))
                    .title("Melbourne"));
            melbourne.showInfoWindow();
        }*/


      /*  mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //Reset marker when already 2
                if (listPoints.size() == 1) {
                    listPoints.clear();
                    mMap.clear();
                }
                //Save first point select
                listPoints.add(latLng);
                //Create marker
             *//*   MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                mMap.addMarker(markerOptions).setTitle(latLng.toString());
           *//*
                Marker melbourne = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Melbourne"));
                melbourne.showInfoWindow();
                Log.d("g", latLng.toString());


                // String url = "api.openweathermap.org/data/2.5/weather?lat="+String.valueOf(latLng.latitude)+"&"+"lon="+String.valueOf(latLng.latitude);
                String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(latLng.latitude) + "&" + "lon=" + String.valueOf(latLng.latitude) + "&appid=cf52422ddd592e0b6eaf2b087fb48188";
                JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("g", response.toString());


                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

                AppController.getInstance().addToRequestQueue(stringRequest);


            }
        });*/

    }


    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                title(text.toString()).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        mMap.addMarker(markerOptions);
    }

    private CharSequence makeCharSequence() {
        String prefix = "Mixing ";
        String suffix = "different fonts";
        String sequence = prefix + suffix;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(ITALIC), 0, prefix.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(BOLD), prefix.length(), sequence.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(MapsActivity.this, "k", Toast.LENGTH_SHORT).show();
        final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        // String url = "api.openweathermap.org/data/2.5/weather?lat="+String.valueOf(latLng.latitude)+"&"+"lon="+String.valueOf(latLng.latitude);
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(marker.getPosition().latitude) + "&" + "lon=" + String.valueOf(marker.getPosition().longitude) + "&appid=cf52422ddd592e0b6eaf2b087fb48188";
        JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("g", response.toString());

                        View customView = inflater.inflate(R.layout.popup, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
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
                        try {
                            pre.setText("Pressure:" + response.getJSONObject("main").getString("pressure") + ",Temp " + response.getJSONObject("main").getString("temp"));

                        minT.setText("Min Temp : " + response.getJSONObject("main").getString("temp_min"));
                        maxT.setText("Max Temp : " +response.getJSONObject("main").getString("temp_max"));
                        weatDe.setText("weather : " + response.getJSONArray("weather").getJSONObject(0).getString("description"));
                        windSp.setText("Wind speed : " + response.getJSONObject("wind").getString("speed"));
                        windD.setText("Wind degree : " + response.getJSONObject("wind").getString("deg"));
                        hum.setText("Humidity : " + response.getJSONObject("main").getString("humidity"));
                        name.setText(response.getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);


/*
        for (int position = 0; position < latList.size(); position++) {
            if (latList.get(position) == marker.getPosition()) {

                View customView = inflater.inflate(R.layout.popup, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
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

            }else {
                Toast.makeText(MapsActivity.this, "nn", Toast.LENGTH_SHORT).show();

            }
        }*/
        // Inflate the custom layout/view

             /*   mPopupWindow = new PopupWindow(
                        customView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if (Build.VERSION.SDK_INT >= 21) {
                    mPopupWindow.setElevation(5.0f);
                }*/
       /*         AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                dialog.setView(customView);
                dialog.create();
                dialog.show();
*/


        return false;
    }
}
