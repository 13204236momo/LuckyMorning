package com.example.zhoumohan.luckymorning.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.common.widget.dragBubbleView.DragBubbleViewOnTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QQBubbleActivity extends AppCompatActivity{

    @BindView(R.id.lv_message)
    ListView lvMessage;

    private List<String> count = new ArrayList<>();
    private MessageAdapter adapter;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqbubble);
        ButterKnife.bind(this);

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        count.add("2");
        count.add("3");
        count.add("5");
        count.add("6");
        count.add("2");
        count.add("7");
        count.add("9");
        count.add("5");

        adapter = new MessageAdapter(this,count);
        lvMessage.setAdapter(adapter);
        lvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               finish();
            }
        });


    }

    public class MessageAdapter extends BaseAdapter {

        private List<String> list;
        private Activity context;
        public MessageAdapter(Activity context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
            TextView textView = convertView.findViewById(R.id.number);
            textView.setText(list.get(position));

            textView.setOnTouchListener(new DragBubbleViewOnTouchListener(context));
            return convertView;
        }
    }
}
