package com.zhimei.liang.customview.SlideFoldMenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nineoldandroids.view.ViewHelper;
import com.zhimei.liang.weixiaoyuan.R;


public class SlidingMenu extends HorizontalScrollView {
    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mScreenWidth;

    private int mMenuWidth;
    // dp
    private int mMenuRightPadding = 50;

    private boolean once;

    private boolean isOpen, isStop;


    public SlidingMenu(Context context) {
        this(context, null);
    }

    /**
     * 未使用自定义属性时，调用
     *
     * @param context
     * @param attrs
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 当使用了自定义属性时，会调用此构造方法
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 获取我们定义的属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingMenu, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightPadding:
                    mMenuRightPadding = a.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, 50, context
                                            .getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
    }

    /**
     * 是用于拦截手势事件的，每个手势事件都会先调用onInterceptTouchEvent
     * return   为 false  的话   ， 不会传递 MotionEvent    则当前layout
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!isOpen) {
            if (ev.getX() < (mMenuWidth / 6)) {
                return true;
            } else {
                return false;
            }
        }
        if (isOpen) {
            if (ev.getX() < mMenuWidth) {
                return false;
            }
        }
        if (isOpen && (ev.getAction() == MotionEvent.ACTION_DOWN)) {
            if (ev.getX() > mMenuWidth) {
                closeMenu();
                return false;
            }
        }

        return super.onInterceptTouchEvent(ev);

    }

    /**
     * 调用   onMeasure()   来确定本身和所包含内容的大小（宽度和高度）。此方法被measure(int,int)唤起
     * <p/>
     * 设置子View的宽和高 设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once) {
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
                    - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 当此视图要给每个子视图赋值大小和位置时，layout会调用   onLayout()
     * <p/>
     * 通过设置偏移量，将menu隐藏
     * changed           表示此视图有新的大小或位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(mMenuWidth, 0);                 //  这将引起onScrollChanged(int,int,int,int)的调用
            // 即默认的getScroll值为mMenuWidth
        }
    }


    /**
     * @param ev
     * @return 若事件被成功处理，则返回true；否则返回false
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                // 隐藏在左边的宽度
                int scrollX = getScrollX();
                if (scrollX >= mMenuWidth / 2) {
                    this.smoothScrollTo(mMenuWidth, 0);
                    if (mOnMenuOpenListener != null) {
                        //第一个参数true：打开菜单，false：关闭菜单;第二个参数 0 代表左侧；1代表右侧
                        mOnMenuOpenListener.onMenuOpen(false);
                    }
                    isOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                    if (mOnMenuOpenListener != null) {
                        //第一个参数true：打开菜单，false：关闭菜单;第二个参数 0 代表左侧；1代表右侧
                        mOnMenuOpenListener.onMenuOpen(true);
                    }
                    disableSubControls(mContent);
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen)
            return;
        disableSubControls(mContent);
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    public void closeMenu() {
        if (!isOpen)
            return;
        SubControls(mContent);
        this.smoothScrollTo(mMenuWidth, 0);
        isOpen = false;
    }

    /**
     * 让一个ViewGroup  内的所有View  或  ViewGroup  全部禁用
     *
     * @param viewGroup
     */
    public static void disableSubControls(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                if (v instanceof ListView) {    //   instanceof  Java 二元运算符     表示是否是右边的实例
                    ((ListView) v).setClickable(false);
                    ((ListView) v).setEnabled(false);

                } else {
                    disableSubControls((ViewGroup) v);
                }
            } else if (v instanceof EditText) {
                ((EditText) v).setEnabled(false);
                ((EditText) v).setClickable(false);

            } else if (v instanceof Button) {
                ((Button) v).setEnabled(false);

            }
        }
    }

    /**
     * 取消一个ViewGroup  内的所有View  或  ViewGroup  的禁用
     *
     * @param viewGroup
     */
    public static void SubControls(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                if (v instanceof ListView) {
                    ((ListView) v).setClickable(true);
                    ((ListView) v).setEnabled(true);

                } else {
                    disableSubControls((ViewGroup) v);
                }
            } else if (v instanceof EditText) {
                ((EditText) v).setEnabled(true);
                ((EditText) v).setClickable(true);

            } else if (v instanceof Button) {
                ((Button) v).setEnabled(true);

            }
        }
    }

    /**
     * 切换菜单
     */
    public void toggle() {
        isStop = false;
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    /**
     * 滚动发生时
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth; // 1 ~ 0

        /**
         * 区别1：内容区域1.0~0.7 缩放的效果 scale : 1.0~0.0 0.7 + 0.3 * scale
         *
         * 区别2：菜单的偏移量需要修改
         *
         * 区别3：菜单的显示时有缩放以及透明度变化 缩放：0.7 ~1.0 1.0 - scale * 0.3 透明度 0.6 ~ 1.0
         * 0.6+ 0.4 * (1- scale) ;
         *
         */
        float rightScale = 0.7f + 0.3f * scale;
        float leftScale = 1.0f - scale * 0.3f;
        float leftAlpha = 0.6f + 0.4f * scale;

        /**
         * 实现了qq的侧滑效果
         */
//        // 调用   属性动画，设置TranslationX
//        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);
//
//        ViewHelper.setScaleX(mMenu, leftScale);
//        ViewHelper.setScaleY(mMenu, leftScale);
//        ViewHelper.setAlpha(mMenu, leftAlpha);
//        // 设置content的缩放的中心点
//        ViewHelper.setPivotX(mContent, 0);
//        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
//        ViewHelper.setScaleX(mContent, rightScale);
//        ViewHelper.setScaleY(mContent, rightScale);

        /**
         *   l   是 getScrollX（） 的值    即是从View 绘制初始化完成 到滑动发生的 滑动距离  范围  ： 0 — mMenuWidth
         *  虽然用了属性动画  但是可以滑动的最大距离   是   mMenuWidth
         *                   实现了菜单在上  且主界面不动的抽屉式菜单
         *
         * 向左偏移   l-mMenuWidth   （为负值）
         * 利用自定义MyLinearLayout   改变了菜单和主界面的绘制顺序  从而使得菜单出现在主界面之上
         */
        if (l == mMenuWidth) {
            SubControls(mContent);
        } else {
            disableSubControls(mContent);
        }

        ViewHelper.setTranslationX(mContent, l - mMenuWidth);
        ViewHelper.setAlpha(mContent, leftAlpha);   //设置主界面的透明度

        // 错误的写法  整型相除得到的 还是整型   比如  1/2  得到 0
//        float slideOffset =(mMenuWidth-l)/mMenuWidth;

        // 正确写法
        float slideOffset = (mMenuWidth-l)*1.0f/mMenuWidth;
        //  滑动距离的回调
        onMenuScrollListener.onMenuScroll(slideOffset);

    }

    /**
     * 菜单打开或关闭回调的接口
     *
     * @author zhy
     */
    public interface OnMenuOpenListener {
        /**
         * @param isOpen true打开菜单，false关闭菜单
         */
        void onMenuOpen(boolean isOpen);
    }

    public OnMenuOpenListener mOnMenuOpenListener;

    public void setOnMenuOpenListener(OnMenuOpenListener mOnMenuOpenListener) {
        this.mOnMenuOpenListener = mOnMenuOpenListener;
    }

    /**
     * 菜单滑动时的回调
     */
    public interface OnMenuScrollListener {
        /**
         * 菜单滑动距离的传递
         *
         * @param x
         */
        void onMenuScroll(float x);
    }

    public OnMenuScrollListener onMenuScrollListener;

    public void setOnMenuScrollListener(OnMenuScrollListener onMenuScrollListener) {

        this.onMenuScrollListener = onMenuScrollListener;
    }

}