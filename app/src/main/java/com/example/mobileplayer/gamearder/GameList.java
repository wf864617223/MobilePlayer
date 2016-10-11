package com.example.mobileplayer.gamearder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;
import com.example.mobileplayer.network.WebView_Net;

public class GameList extends BaseActivity {

    private ListView gamelistview;
    private String[] ids = {"打飞机","宝石迷阵","数字连连看","扫雷"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("休闲游戏");
        setRightButton(View.GONE);
        gamelistview = (ListView) findViewById(R.id.gamelistview);
        gamelistview.setAdapter(new MyAdapter());
        gamelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                String uri;
                switch (position){
                    case 0:
                        uri = "http://sandbox.runjs.cn/show/lp6rqcsr";
                        bundle.putString("uri",uri);
                        break;
                    case 1:
                        uri = "http://sandbox.runjs.cn/show/xluo7u7u";
                        bundle.putString("uri",uri);
                        break;
                    case 2:
                        uri = "http://runjs.cn/detail/bymk6wak";
                        bundle.putString("uri",uri);
                        break;
                    case 3:
                        uri = "http://sandbox.runjs.cn/show/vubgwcta";
                        bundle.putString("uri",uri);
                        break;
                }
                intent.setClass(GameList.this, WebView_Net.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(GameList.this).inflate(R.layout.help_text,null);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) convertView.findViewById(R.id.textView_help);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.text.setText(ids[position]);
            return convertView;
        }
        class ViewHolder{
            TextView text;
        }
    }
    @Override
    public View setContentView() {
        return View.inflate(GameList.this,R.layout.activity_game_list,null);
    }

    @Override
    public void rightButtonClick() {

    }

    @Override
    public void leftButtonClick() {
        finish();
    }
}
