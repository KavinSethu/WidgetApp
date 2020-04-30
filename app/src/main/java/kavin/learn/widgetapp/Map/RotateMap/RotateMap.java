package kavin.learn.widgetapp.Map.RotateMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import kavin.learn.widgetapp.R;

public class RotateMap extends AppCompatActivity implements OnMapReadyCallback , View.OnClickListener {

    SupportMapFragment mapFragment;
    TextView from_search,to_search;
    GoogleMap mMap;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    Geocoder geocoder;
    List<Address> addresses;

    ArrayList MarkerPoints = new ArrayList();
    Button btn_Search;
    RequestQueue mRequestQueue;
    MarkerOptions startMarker, endMarker;
    ConstraintLayout mapContainder;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 22;
    private static final String TAG = RotateMap.class.getSimpleName();
    boolean startPoint=false,endPoint=false;

    Marker sourceMarker,destMarker;


    String getLatLang="https://maps.googleapis.com/maps/api/geocode/json?address=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_map);

        findviews();
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    geocoder = new Geocoder(RotateMap.this, Locale.getDefault());
                                    try {
                                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    String address = null;
                                    if (addresses!=null)
                                     address = addresses.get(0).getAddressLine(0);

                                    if (address!=null)
                                    from_search.setText(address);

                                    CameraUpdate center= CameraUpdateFactory.newLatLng( new LatLng(location.getLatitude(), location.getLongitude()));
                                    CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);
                                    mMap.moveCamera(center);
                                    mMap.animateCamera(zoom);

                                    startMarker =new MarkerOptions();
                                    startMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
                                    startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_current));
                                    sourceMarker=mMap.addMarker(startMarker);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();

            //Set a location
            geocoder = new Geocoder(RotateMap.this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            String address = addresses.get(0).getAddressLine(0);

            if (address!=null)
                from_search.setText(address);

            CameraUpdate center= CameraUpdateFactory.newLatLng( new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            startMarker =new MarkerOptions();
            startMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_current));
            sourceMarker=mMap.addMarker(startMarker);
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
    }

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
                getLastLocation();
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private void findviews() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        from_search=findViewById(R.id.et_from);
        to_search=findViewById(R.id.et_to);
        btn_Search=findViewById(R.id.btn_Search);
        mapContainder=findViewById(R.id.mapContainder);

        btn_Search.setOnClickListener(this);
        from_search.setOnClickListener(this);
        to_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_Search:
                drawRouteMap();
                break;

            case R.id.et_from:
                startPoint = true;
                endPoint = false;
                onSearchCalled();
                break;

            case R.id.et_to:
                startPoint = false;
                endPoint = true;
                onSearchCalled();
                break;
        }
    }

    private void drawRouteMap() {
        if (MarkerPoints.size() >= 2) {
            LatLng origin = (LatLng) MarkerPoints.get(0);
            LatLng dest = (LatLng) MarkerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            getRoute(url);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?key=AIzaSyBjOJ79xpH-SVeN167QxJY4dJREIqzGQlU&" + parameters;


        return url;
    }

    public void getRoute(String url) {

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.getCache().clear();

        StringRequest req_data = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getLatLang_Responce---", response);
                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;

                /*try {
                    jObject = new JSONObject(response[0]);
                    Log.d("ParserTask",response[0].toString());
                    DirectionsJSONParser parser = new DirectionsJSONParser();
                    Log.d("ParserTask", parser.toString());

                    // Starts parsing data
                    routes = parser.parse(jObject);
                    Log.d("ParserTask","Executing routes");
                    Log.d("ParserTask",routes.toString());

                } catch (Exception e) {
                    Log.d("ParserTask",e.toString());
                    e.printStackTrace();
                }*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Log.d("group_sets_url---", url);
        req_data.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(req_data);

    }

    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("IN") //NIGERIA
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                String address = place.getAddress();
                // do query with address
                getLatLang(address);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(RotateMap.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    //Get lat lang from Address //Volley
    public void getLatLang(final String addrs) {

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.getCache().clear();

        String url=getLatLang+addrs+"&key=AIzaSyBjOJ79xpH-SVeN167QxJY4dJREIqzGQlU";

        StringRequest req_data = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getLatLang_Responce---", response);
                try {

                    //Parent
                    JSONObject ObjResponse = new JSONObject(String.valueOf(response));

                    //Result Array
                    JSONArray results=ObjResponse.getJSONArray("results");
                    JSONObject addrsobj=results.getJSONObject(0);

                    //Location
                    JSONObject js=addrsobj.getJSONObject("geometry");
                    JSONObject location=js.getJSONObject("location");

                    double lat= Double.parseDouble(location.getString("lat"));
                    double lng= Double.parseDouble(location.getString("lng"));

                    Log.d("StartPOint",""+startPoint);
                    Log.d("EndPOint",""+endPoint);
                    if (startPoint){

                        from_search.setText(addrs);

                        if (sourceMarker!=null)
                        sourceMarker.remove();

                        /*CameraUpdate center= CameraUpdateFactory.newLatLng( new LatLng(lat,lng));
                        CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);
                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);*/

                        startMarker =new MarkerOptions();
                        startMarker.position(new LatLng(lat,lng));
                        startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_current));
                        sourceMarker=mMap.addMarker(startMarker);

                    }else {

                        to_search.setText(addrs);

                        if (destMarker!=null)
                        destMarker.remove();

                       /* CameraUpdate center= CameraUpdateFactory.newLatLng( new LatLng(lat,lng));
                        CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);
                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);*/

                        endMarker =new MarkerOptions();
                        endMarker.position(new LatLng(lat,lng));
                        endMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
                        destMarker=mMap.addMarker(endMarker);

                    }


                    //Bring all Marker inside Screen
                    ArrayList<Marker> markers=new ArrayList<>();
                    markers.add(sourceMarker);
                    markers.add(destMarker);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker marker : markers) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = 100; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);
                    mMap.moveCamera(cu);
                    mMap.animateCamera(zoom);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Log.d("group_sets_url---", url);
        req_data.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(req_data);

    }
}
