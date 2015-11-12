package com.zhimei.liang.weixiaoyuan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhimei.liang.fragment.AllDemandsFragment;
import com.zhimei.liang.fragment.IndividulDemandFragment;


import java.util.ArrayList;
import java.util.List;

public class DemandCenterActivity extends FragmentActivity {
    private ViewPager viewPager;
    private TextView tv1;
    private TextView tv2;
    private ImageView tabline;
    private List<Fragment> list;

    private int tabLineLength;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_center);
        initTabLine();
        initView();
    }
    private void initTabLine() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        tabLineLength = metrics.widthPixels / 2;
        tabline = (ImageView) findViewById(R.id.cursor);
        LayoutParams lp = tabline.getLayoutParams();
        lp.width = tabLineLength;
        tabline.setLayoutParams(lp);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.demand_viewpager);
        tv1 = (TextView) findViewById(R.id.text1);
        tv2 = (TextView) findViewById(R.id.text2);
        list = new ArrayList<Fragment>();

        IndividulDemandFragment fragment1 = new IndividulDemandFragment();
        AllDemandsFragment fragment2 = new AllDemandsFragment();

        list.add(fragment1);
        list.add(fragment2);



        FragmentPagerAdapter adapter = new FragmentPagerAdapter(
                getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return list.get(arg0);
            }
        };


        viewPager.setAdapter(adapter);


        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                tv1.setTextColor(Color.BLACK);
                tv2.setTextColor(Color.BLACK);


                switch (position) {
                    case 0:
                        tv1.setTextColor(Color.rgb(51, 153, 0));
                        break;
                    case 1:
                        tv2.setTextColor(Color.rgb(51, 153, 0));
                        break;

                }

                currentPage = position;

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                Log.i("tuzi", arg0 + "," + arg1 + "," + arg2);

                LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tabline
                        .getLayoutParams();

                if (currentPage == 0 && arg0 == 0) { // 0->1�ƶ�(��һҳ���ڶ�ҳ)
                    ll.leftMargin = (int) (currentPage * tabLineLength + arg1
                            * tabLineLength);
                } /*else if (currentPage == 1 && arg0 == 1) { // 1->2�ƶ����ڶ�ҳ������ҳ��
                    ll.leftMargin = (int) (currentPage * tabLineLength + arg1
                            * tabLineLength);
                }*/ else if (currentPage == 1 && arg0 == 0) { // 1->0�ƶ����ڶ�ҳ����һҳ��
                    ll.leftMargin = (int) (currentPage * tabLineLength - ((1 - arg1) * tabLineLength));
                } /*else if (currentPage == 2 && arg0 == 1) { // 2->1�ƶ�������ҳ���ڶ�ҳ��
                    ll.leftMargin = (int) (currentPage * tabLineLength - (1 - arg1)
                            * tabLineLength);
                }*/

                tabline.setLayoutParams(ll);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });




    }
}
