package com.example.mydietapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.mydietapp.MainActivity;
import com.example.mydietapp.R;
import com.example.mydietapp.db.DbHelper;
import com.example.mydietapp.model.MyItem;
import com.example.mydietapp.model.MyItemAdapter;

import java.lang.reflect.Array;
import java.util.*;

public class SettingFrag extends Fragment {
    private ListView listView;
    private MyItemAdapter adapter;
    private List<MyItem> itemArray;

    private DbHelper helper;
    private SQLiteDatabase db;

    public static Stack<Fragment> fragmentStack;
//    public static FragmentManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_setting,container,false);

        fragmentStack = new Stack<>();
//        manager = getSupportFragmentManager();

        listView = v.findViewById(R.id.list_view);
        adapter=new MyItemAdapter(getActivity(),R.layout.list_item,setListViewData());
        listView.setAdapter((ListAdapter) adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyItem item=itemArray.get(position);
                switch(item.getText()) {
                    case "시간 알림":
                        break;
                    case "모든 데이터 삭제":
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("모든 데이터를 삭제하시겠습니까?");
                        builder.setPositiveButton("삭제",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                helper = new DbHelper(getActivity(), "myDiet.db", null, 1);
                                db = helper.getWritableDatabase();
                                helper.onCreate(db);

                                db.delete("myRecord",null,null);
                                Toast.makeText(getActivity(), "모든 데이터가 삭제되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("취소",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    case "앱 정보":
                        MainActivity.manager.beginTransaction().replace(R.id.fragment_container,new AppInfoFrag()).commit();
                        break;
                }
            }
        });
        return v;
    }
    public List<MyItem> setListViewData() {
        itemArray=new ArrayList<>();
        MyItem item1=new MyItem(R.drawable.ic_access_alarm_black_24dp,"시간 알림");
        MyItem item2=new MyItem(R.drawable.ic_delete_black_24dp,"모든 데이터 삭제");
        MyItem item3=new MyItem(R.drawable.ic_info_outline_black_24dp,"앱 정보");
        itemArray.add(item1);
        itemArray.add(item2);
        itemArray.add(item3);
        return itemArray;
    }
}
