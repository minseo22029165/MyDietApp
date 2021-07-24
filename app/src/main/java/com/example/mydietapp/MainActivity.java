package com.example.mydietapp;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.example.mydietapp.ui.CalendarFrag;
import com.example.mydietapp.ui.GraphFrag;
import com.example.mydietapp.ui.SettingFlag;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationView=findViewById(R.id.bottom_navigation);
        //첫 화면 띄우기
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new CalendarFrag()).commit();

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.b_calendar :
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CalendarFrag()).commit();
                        break;
                    case R.id.b_graph:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GraphFrag()).commit();
                        break;
                    case R.id.b_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingFlag()).commit();
                        break;

                }
                return true;
            }
        });

    }

}
