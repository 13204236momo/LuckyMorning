package com.example.zhoumohan.luckymorning.base;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zhoumohan.luckymorning.R;

public class BaseActivity extends AppCompatActivity {

    protected View contentView;
    private LinearLayout llContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initUI();
    }

    private void initUI(){
        llContent = findBy(R.id.ll_content);
    }


    /**
     * 加入页面内容布局
     *
     * @param layoutId
     */
    protected void contentView(int layoutId) {
        contentView = getLayoutInflater().inflate(layoutId, null);
        if (llContent.getChildCount() > 0) {
            llContent.removeAllViews();
        }
        if (contentView != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            llContent.addView(contentView, params);
        }
    }


    public <T extends View> T findBy(int resId){
        return ViewUtils.findViewById(this,resId);
    }

    public static final class ViewUtils {

        public static  <T extends View> T findViewById(Activity activity, int resId){
            return (T) activity.findViewById(resId);
        }

        public static <T extends View> T findViewById(View view,int resId){
            return (T) view.findViewById(resId);
        }
    }
}
