package com.example.zhoumohan.luckymorning.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.zhoumohan.luckymorning.R;

import java.util.List;


public class CouponAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;

    public CouponAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HoldView holdView;
        if (view == null){
            holdView = new HoldView();
            view = LayoutInflater.from(context).inflate(R.layout.item_coupon,null);
            holdView.tvTotal = view.findViewById(R.id.tv_total);
            holdView.tvTitle = view.findViewById(R.id.tv_title);
            holdView.tvDate = view.findViewById(R.id.tv_date);
            holdView.checkBox = view.findViewById(R.id.checkbox);
            view.setTag(holdView);
        }else {
            holdView = (HoldView) view.getTag();
            holdView.tvTitle.setText(list.get(i));
        }
        return view;
    }

    class HoldView{
        TextView tvTotal;
        TextView tvTitle;
        TextView tvDate;
        CheckBox checkBox;
    }
}
