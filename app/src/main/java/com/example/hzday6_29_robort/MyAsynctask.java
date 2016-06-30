package com.example.hzday6_29_robort;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Wakesy on 2016/6/29.
 */
public class MyAsynctask extends AsyncTask<String,Void,List<Bean>> {

    private List<Bean>datalist;
    private ListView listView;
    private MainActivity.MyAdapter adapter;
    //将LIstView ,datalist和adapter传过来
    public MyAsynctask(List<Bean> datalist,ListView listView,MainActivity.MyAdapter adapter) {
        this.datalist = datalist;
        this.listView=listView;
        this.adapter=adapter;
    }

    @Override
    protected List<Bean> doInBackground(String... params) {
        String jsonString=HttpJson.getJsonContent(params[0]);//通过url下载Json 数据
        try {
            JSONObject jsonObject=new JSONObject(jsonString);
            String text=jsonObject.getString("text");
            Bean bean1=new Bean(text,0);//得到机器回复的信息，加入集合中，并加上标识符0，在左边显示
            Log.i("jsonString",text);
            datalist.add(bean1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return datalist;
    }

    @Override
    protected void onPostExecute(List<Bean> datalist) {

        super.onPostExecute(datalist);
        adapter.notifyDataSetChanged();//更新适配器
        listView.setAdapter(adapter);//重新设置设配器
        listView.setSelection(datalist.size()-1);//将listView调到最后一行的位置



    }
}
