package appewtc.masterung.twschool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ListAllStudent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_student);
    }   // Main Method

    public void clickBackListStudent(View view) {
        finish();
    }

}   // Main Class
