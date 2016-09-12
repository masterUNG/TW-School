package appewtc.masterung.twschool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    //Explicit
    private EditText nameEditText, surnameEditText,
            userEditText, passwordEditText;
    private RadioGroup radioGroup;
    private RadioButton studentRadioButton, teacherRadioButton;
    private String nameString, surnameString, roomString, userString,
            passwordString, statusString = "1", QRcodeString;
    private static final String urlPHP = "http://swiftcodingthai.com/tw/add_user_pitawan.php";
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Initial Widget
        nameEditText = (EditText) findViewById(R.id.editText);
        surnameEditText = (EditText) findViewById(R.id.editText2);
        spinner = (Spinner) findViewById(R.id.spnRoom);
        userEditText = (EditText) findViewById(R.id.editText4);
        passwordEditText = (EditText) findViewById(R.id.editText5);
        radioGroup = (RadioGroup) findViewById(R.id.ragStatus);
        studentRadioButton = (RadioButton) findViewById(R.id.radioButton);
        teacherRadioButton = (RadioButton) findViewById(R.id.radioButton2);

        //Spinner Controller
        SynRoom synRoom = new SynRoom(this, spinner);
        synRoom.execute();

        //Radio Controller
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {

                    case R.id.radioButton:
                        //for Student
                        statusString = "1";

                        break;
                    case R.id.radioButton2:
                        //for Teacher
                        statusString = "0";

                        break;

                }   // switch

            }   // onCheck
        });


    }   // Main Method

    private class SynRoom extends AsyncTask<Void, Void, String> {

        //Explicit
        private Context context;
        private Spinner mySpinner;
        private static final String urlRoomJSON = "http://swiftcodingthai.com/tw/get_room_master.php";
        private String[] roomStrings;

        public SynRoom(Context context, Spinner mySpinner) {
            this.context = context;
            this.mySpinner = mySpinner;
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlRoomJSON).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {
                Log.d("12SepV1", "e doIn ==> " + e.toString());
                return null;
            }

        }   // doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("12SepV1", "JSON ==> " + s);

            try {

                JSONArray jsonArray = new JSONArray(s);
                roomStrings = new String[jsonArray.length()];
                for (int i=0;i<jsonArray.length();i+=1) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    roomStrings[i] = jsonObject.getString("Room");

                }   // for

                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, roomStrings);



                mySpinner.setAdapter(stringArrayAdapter);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }   // onPost

    }   // SynRoom Class

    public void clickSignUpSign(View view) {

        //Get Value From Edit Text
        nameString = nameEditText.getText().toString().trim();
        surnameString = surnameEditText.getText().toString().trim();

        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //Check Space
        if (nameString.equals("") || surnameString.equals("") ||
                 userString.equals("") ||
                passwordString.equals("")) {
            //Have Space

            Toast.makeText(this, "กรุณากรอก ทุกช่องคะ", Toast.LENGTH_SHORT).show();

        } else {
            //No Space
            switch (Integer.parseInt(statusString)) {

                case 0:
                    QRcodeString = "teacher_" + userString;
                    break;
                case 1:
                    QRcodeString = "student_" + userString;
                    break;

            }   // switch

            confirmValue();

        }   // if


    }   // clickSignUp

    private void confirmValue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.doremon48);
        builder.setTitle("โปรดตรวจสอบข้อมูล");
        builder.setMessage("Name = " + nameString + "\n" +
        "Surname = " + surnameString + "\n" +
        "Status = " + showStatus(statusString) + "\n" +
        "Room = " + "test" + "\n" +
        "User = " + userString + "\n" +
        "Password = " + passwordString + "\n" +
        "QRcode = " + QRcodeString);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                upLoadValueToServer();
                dialogInterface.dismiss();
            }
        });
        builder.show();


    }   // confirmValue

    private void upLoadValueToServer() {

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("isAdd", "true")
                .add("Name", nameString)
                .add("Surname", surnameString)
                .add("Status", statusString)
                .add("Room", "test")
                .add("Lat", "0")
                .add("Lng", "0")
                .add("User", userString)
                .add("Password", passwordString)
                .add("QRcode", QRcodeString)
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
                finish();
            }
        });


    }   // upLoad

    private String showStatus(String statusString) {

        String result = null;

        switch (Integer.parseInt(statusString)) {
            case 0:
                result = "Teacher";
                break;
            case 1:
                result = "Student";
                break;
        }

        return result;
    }

}   // Main Class
