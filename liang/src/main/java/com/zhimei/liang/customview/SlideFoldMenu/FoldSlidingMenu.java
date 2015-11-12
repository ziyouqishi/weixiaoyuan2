package com.zhimei.liang.customview.SlideFoldMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/10/12.
 * 自定义侧滑和自定义折叠布局的组合
 */
public class FoldSlidingMenu extends SlidingMenu {

    private FoldLayout foldlayout;
    private LinearLayout lyt;

    public FoldSlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        lyt = (LinearLayout) getChildAt(0);
        View child = lyt.getChildAt(0);
        foldlayout = new FoldLayout(getContext());

        //   设置FoldLayout最终折叠到的位置    1  就是最右边    这样就可以把折叠后的布局完整的显示出来 如果放左边    会隐藏一部分
        foldlayout.setAnchor(1);
        lyt.removeView(child);
        foldlayout.addView(child);
        ViewGroup.LayoutParams layPar = child.getLayoutParams();
        lyt.addView(foldlayout, 0, layPar);

        setOnMenuScrollListener(new OnMenuScrollListener() {
            @Override
            public void onMenuScroll(float x) {
                Log.e("wxy", "" + x);
                foldlayout.setFactor(x);
            }
        });
    }


}
