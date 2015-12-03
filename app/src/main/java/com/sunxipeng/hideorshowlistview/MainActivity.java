package com.sunxipeng.hideorshowlistview;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private  BottomFloatListView mBottomFloatListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomFloatListView = (BottomFloatListView)findViewById(R.id.listView)  ;
        mBottomFloatListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
        ViewGroup bottomView = (ViewGroup)findViewById(R.id.bottombar) ;
        mBottomFloatListView.setBottomBar(bottomView);
    }

    private List<String> getData(){

        List<String> data = new ArrayList<String>();

        for(int i = 0; i <100; i++)      {
            data.add("测试代码" + i);
        }

        return data;

    }


}
