package appewtc.masterung.twschool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class EditStudent extends FragmentActivity implements OnMapReadyCallback {

    //Explicit
    private GoogleMap mMap;
    private EditText nameEditText, surnameEditText, roomEditText;
    private String[] loginStrings;
    private double studentLatADouble = 0, studentLngADouble = 0;
    private static final String urlPHP = "http://swiftcodingthai.com/tw/edit_user_pitawan.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        //Bind Widget
        nameEditText = (EditText) findViewById(R.id.editText8);
        surnameEditText = (EditText) findViewById(R.id.editText9);
        roomEditText = (EditText) findViewById(R.id.editText10);

        //Show View
        loginStrings = getIntent().getStringArrayExtra("Login");
        nameEditText.setText(loginStrings[1]);
        surnameEditText.setText(loginStrings[2]);
        roomEditText.setText(loginStrings[4]);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }   // Main Method

    public void clickEditData(View view) {

        if (studentLatADouble != 0) {

            uploadValue();

        } else {

            Toast.makeText(this, "กรุณาคลิกที่แผนที่ เพื่อกำหนดตำแหน่งของบ้าน",
                    Toast.LENGTH_SHORT).show();

        }   // if


    }   // clickEdit

    private void uploadValue() {

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("isAdd", "true")
                .add("id", loginStrings[0])
                .add("Name", nameEditText.getText().toString().trim())
                .add("Surname", surnameEditText.getText().toString().trim())
                .add("Room", roomEditText.getText().toString().trim())
                .add("Lat", Double.toString(studentLatADouble))
                .add("Lng", Double.toString(studentLngADouble))
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(urlPHP).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                Intent intent = new Intent(EditStudent.this, StudentService.class);
                intent.putExtra("Login", loginStrings);
                startActivity(intent);
                finish();

            }
        });

    }   // upload


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //For Setup Center Map
        final double twLat = 15.350664;   // Latitude ของ ตากฟ้า
        final double twLng = 100.491939;

        LatLng latLng = new LatLng(twLat, twLng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        createTWmarker(latLng);

        //Find Student Home
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.clear();
                createTWmarker(new LatLng(twLat, twLng));

                mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(loginStrings[1])
                .snippet(Double.toString(latLng.latitude) + ", " +
                Double.toString(latLng.longitude)));

                studentLatADouble = latLng.latitude;
                studentLngADouble = latLng.longitude;

                loginStrings[5] = Double.toString(studentLatADouble);
                loginStrings[6] = Double.toString(studentLngADouble);


            }   // onMapClick
        });


    }   // onMap

    private void createTWmarker(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.build4))
                .title("โรงเรียนตากฟ้าวิชาประสิทธิ์"));
    }

}   // Main Class
