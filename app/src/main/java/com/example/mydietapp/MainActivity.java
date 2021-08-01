package com.example.mydietapp;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.mydietapp.ui.CalendarFrag;
import com.example.mydietapp.ui.GraphFrag;
import com.example.mydietapp.ui.SettingFrag;
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingFrag()).commit();
                        break;

                }
                return true;
            }
        });

    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

}
