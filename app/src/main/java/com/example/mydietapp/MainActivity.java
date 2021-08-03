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

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;

    public static Stack<Fragment> fragmentStack;
    public static FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /// 뒤로가기
        fragmentStack = new Stack<>();
//        fragmentStack.push(new CalendarFrag());
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragment_container, new CalendarFrag()).commit();
        ///

        mBottomNavigationView=findViewById(R.id.bottom_navigation);
        //첫 화면 띄우기
//         getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new CalendarFrag()).commit();

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.b_calendar :
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CalendarFrag()).commit();
                        manager.beginTransaction().replace(R.id.fragment_container,new CalendarFrag()).commit();
                        break;
                    case R.id.b_graph:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GraphFrag()).commit();
                        manager.beginTransaction().replace(R.id.fragment_container,new GraphFrag()).commit();
                        break;
                    case R.id.b_setting:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingFrag()).commit();
                        manager.beginTransaction().replace(R.id.fragment_container,new SettingFrag()).commit();
                        break;

                }
                return true;
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (!fragmentStack.isEmpty()) {
            Fragment nextFragment = fragmentStack.pop();
            manager.beginTransaction().replace(R.id.fragment_container, nextFragment).commit();
            System.out.println("[TESTING >>] " + fragmentStack.size());
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
