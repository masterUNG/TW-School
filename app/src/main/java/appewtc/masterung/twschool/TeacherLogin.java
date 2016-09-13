package appewtc.masterung.twschool;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class TeacherLogin extends FragmentActivity implements OnMapReadyCallback {

    //Explicit
    private GoogleMap mMap;
    private String[] userLoginStrings, nameStudentStrings,
    surnameStudentStrings, latStrings, lngStrings;
    private TextView textView;
    private static final String urlJSON = "http://swiftcodingthai.com/tw/get_user_where_room_master.php";
    private String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_login_layout);

        textView = (TextView) findViewById(R.id.textView15);

        userLoginStrings = getIntent().getStringArrayExtra("Login");

        textView.setText(userLoginStrings[1] + " " +
        userLoginStrings[2] + " ห้อง " + userLoginStrings[4]);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }   // Main Method

    public void clickListAllStudent(View view) {
        Intent intent = new Intent(TeacherLogin.this, ListAllStudent.class);

        intent.putExtra("Room", userLoginStrings[4]);
        intent.putExtra("Name", nameStudentStrings);
        intent.putExtra("Surname", surnameStudentStrings);
        intent.putExtra("Lat", latStrings);
        intent.putExtra("Lng", lngStrings);

        startActivity(intent);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //For Setup Center Map
        final double twLat = 15.350664;   // Latitude ของ ตากฟ้า
        final double twLng = 100.491939;

        LatLng latLng = new LatLng(twLat, twLng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        //For Marker School
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tw_logo48))
                .title("โรงเรียนตากฟ้าวิชาประสิทธิ์")
                .snippet("โรงเรียนมัธยมอันดับหนึ่งของ นครสวรรค์"));

        //Create Student Marker
        SynMyStudent synMyStudent = new SynMyStudent();
        synMyStudent.execute();






    }   // onMapReady

    private class SynMyStudent extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("Room", userLoginStrings[4])
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlJSON).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("12SepV5", "JSON ==> " + s);

            try {

                JSONArray jsonArray = new JSONArray(s);
                nameStudentStrings = new String[jsonArray.length()];
                surnameStudentStrings = new String[jsonArray.length()];
                latStrings = new String[jsonArray.length()];
                lngStrings = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i += 1) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    nameStudentStrings[i] = jsonObject.getString("Name");
                    surnameStudentStrings[i] = jsonObject.getString("Surname");
                    latStrings[i] = jsonObject.getString("Lat");
                    lngStrings[i] = jsonObject.getString("Lng");

                    Log.d("12SepV3", "Length ==> " + jsonArray.length());
                    Log.d("12SepV3", "Name (" + i + ") = " + nameStudentStrings[i]);
                    Log.d("12SepV3", "Lat(" + i + ") = " + latStrings[i]);
                    Log.d("12SepV3", "Lng(" + i + ") = " + lngStrings[i]);

                    addMyMarker(latStrings[i], lngStrings[i],
                            (nameStudentStrings[i] + " " + surnameStudentStrings[i]));

                }   // for

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }






    private void addMyMarker(String latString,
                             String lngString,
                             String strTitle) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(latString), Double.parseDouble(lngString)))
        .title(strTitle));


    }   // addMyMarker

}   // Main Class
