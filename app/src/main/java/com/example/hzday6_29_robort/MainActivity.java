package com.example.hzday6_29_robort;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Bean> datalist;
    private Button btn_send;
    private EditText et_input;
    private   MyAdapter adapter = new MyAdapter();
    private static final String URL = "http://www.tuling123.com/openapi/api";
    private static final String API_KEY = "080390332ce0704a54cd5a3aaf70f443";

    // 图灵机器人，通过输入的信息，查找相应的答复，返回Json数据的url;
    public static String  getPath(String msg){
        try {
            String path = URL+"?key="+API_KEY+"&info="+ URLEncoder.encode(msg, "utf-8");
            return path;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEven();

    }

    private void initEven() {
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String et_content=et_input.getText().toString();
               Bean bean=new Bean(et_content,1);//输入的信息存入集合中，并标记为自己1，显示在右边
                if(!TextUtils.isEmpty(et_content))
                 {
                    datalist.add(bean);}
                String url=getPath(et_content);//找到回复信息对应的url
                Log.i("url", url);
                new MyAsynctask(datalist,listView,adapter).execute(url);//启动异步任务获取数据
                adapter.notifyDataSetChanged();//更新适配器数据
                listView.setAdapter(adapter);
                listView.setSelection(datalist.size()-1);//将listView调到最后一行的位置

                et_input.setText("");

            }
        });

    }

    private void initData() {
        datalist = new ArrayList<>();


    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView_tuling);
        btn_send= (Button) findViewById(R.id.btn_send);
        et_input= (EditText) findViewById(R.id.et_input);

    }




    public class MyAdapter extends BaseAdapter {

        public MyAdapter() {
        }

        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Object getItem(int position) {
            return datalist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (getItemViewType(position) == 0) {
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_left, null);
            } else
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_right, null);
            TextView tv_robort = (TextView) view.findViewById(R.id.tv_robort);
            tv_robort.setText(datalist.get(position).getContent());
            TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
            String time=getSystemTime();
            tv_time.setText(time);
            return view;
        }

/*
            关键方法
        1、getItemViewType 用于判断当前使用哪个item布局
        2、getViewTypeCount 返回当前Item布局的种数
*
* */

        //获得Item布局类型的方法
        @Override
        public int getItemViewType(int position) {

            if (datalist.get(position).getType() == 0) {
                return 0;
            } else {
                return 1;
            }
        }

        //获得Item布局种类数
        String getSystemTime() {
            long time1=System.currentTimeMillis();
            SimpleDateFormat format=new SimpleDateFormat("MM-dd HH:mm");
            Date date=new Date(time1);
            String time=format.format(date);

            return time;
        }
        public int getViewTypeCount() {
            return 2;
        }
    }
}
