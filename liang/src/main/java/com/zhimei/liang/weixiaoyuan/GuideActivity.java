package com.zhimei.liang.weixiaoyuan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhimei.liang.fragment.GuideFragment1;
import com.zhimei.liang.fragment.GuideFragment2;
import com.zhimei.liang.fragment.GuideFragment3;
import com.zhimei.liang.fragment.GuideFragment4;
import com.zhimei.liang.utitls.DepthPageTransformer;
import com.zhimei.liang.utitls.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends FragmentActivity {
    private ViewPager viewPage;
    private GuideFragment1 mFragment1;
    private GuideFragment2 mFragment2;
    private GuideFragment3 mFragment3;
    private GuideFragment4 mFragment4;
    private PagerAdapter mPgAdapter;
    private RadioGroup dotLayout;
    private List<Fragment> mListFragment = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        viewPage.setOnPageChangeListener(new MyPagerChangeListener());
        viewPage.setPageTransformer(true, new DepthPageTransformer());
    }

    private void initView() {
        dotLayout = (RadioGroup) findViewById(R.id.advertise_point_group);
        viewPage = (ViewPager) findViewById(R.id.viewpager);
        mFragment1 = new GuideFragment1();
        mFragment2 = new GuideFragment2();
        mFragment3 = new GuideFragment3();
        mFragment4 = new GuideFragment4();
        mListFragment.add(mFragment1);
        mListFragment.add(mFragment2);
        mListFragment.add(mFragment3);
        mListFragment.add(mFragment4);
        mPgAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                mListFragment);
        viewPage.setAdapter(mPgAdapter);

    }

    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageSelected(int position) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            ((RadioButton) dotLayout.getChildAt(position)).setChecked(true);
        }

    }

}
