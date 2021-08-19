package com.example.mydietapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.mydietapp.ui.CalendarFrag;
import com.example.mydietapp.ui.GraphFrag;
import com.example.mydietapp.ui.SettingFrag;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;

    public static Stack<Fragment> fragmentStack;
    public static FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentStack = new Stack<>();
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragment_container, new CalendarFrag()).commit(); // 첫화면 띄우기

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.app_toolbar);
        tb.setTitleTextColor(Color.BLACK);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Calendar");
        ab.show();
        mBottomNavigationView=findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.b_calendar :
                        manager.beginTransaction().replace(R.id.fragment_container,new CalendarFrag()).commit();
                        ab.setTitle("Calendar");
                        break;
                    case R.id.b_graph:
                        manager.beginTransaction().replace(R.id.fragment_container,new GraphFrag()).commit();
                        ab.setTitle("Chart");
                        break;
                    case R.id.b_setting:
                        manager.beginTransaction().replace(R.id.fragment_container,new SettingFrag()).commit();
                        ab.setTitle("Setting");
                        break;
                }
                ab.show();
                return true;
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (!fragmentStack.isEmpty()) {
            Fragment nextFragment = fragmentStack.pop();
            manager.beginTransaction().replace(R.id.fragment_container, nextFragment).commit();
        } else {
            super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

}
