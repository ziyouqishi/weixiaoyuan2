package com.zhimei.liang.customview.SlideFoldMenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author zhy http://blog.csdn.net/lmj623565791/
 */
public class FoldLayout extends ViewGroup {

    private static final int NUM_OF_POINT = 8;
    /**
     * 图片的折叠后的总宽度
     */
    private float mTranslateDis;

    //  折叠布局的缩放比   默认不缩放
    protected float mFactor = 1f;

    private int mNumOfFolds = 8;

    // 平移矩阵数组
    private Matrix[] mMatrices = new Matrix[mNumOfFolds];

    // 画笔   布局被分成八份  对于0 2 4 6  四个区域 调用mSolidPaintpaint画笔
    private Paint mSolidPaint;

    //                                     对于1 3 5 7  四个区域  调用  mShadowPaint 画笔      渐变的阴影
    private Paint mShadowPaint;

    // 阴影绘制矩阵
    private Matrix mShadowGradientMatrix;

    private LinearGradient mShadowGradientShader;

    //  平分八份之后的宽度
    private float mFlodWidth;
    private float mTranslateDisPerFlod;

    private float mAnchor = 0;

    private Canvas mCanvas = new Canvas();
    private Bitmap mBitmap;
    private boolean isReady;

    public FoldLayout(Context context) {
        this(context, null);
    }

    /**
     * 构造方法中初始化成员变量
     *
     * @param context
     * @param attrs
     */
    public FoldLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        for (int i = 0; i < mNumOfFolds; i++) {
            mMatrices[i] = new Matrix();
        }

        mSolidPaint = new Paint();
        mShadowPaint = new Paint();
        mShadowPaint.setStyle(Style.FILL);
        mShadowGradientShader = new LinearGradient(0, 0, 0.5f, 0, Color.BLACK,
                Color.TRANSPARENT, TileMode.CLAMP);
        mShadowPaint.setShader(mShadowGradientShader);
        mShadowGradientMatrix = new Matrix();
        //
        this.setWillNotDraw(false);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child = getChildAt(0);
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(child.getMeasuredWidth(),
                child.getMeasuredHeight());

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = getChildAt(0);
        child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());

        //  Config.ARGB_8888 是 Bitmap.Config的 几个常量之一  表示 代表32位ARGB位图
        //  当然位图的位数  越高   则画面越逼真
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);
        updateFold();

    }

    private void updateFold() {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        mTranslateDis = w * mFactor;
        mFlodWidth = w / mNumOfFolds;
        mTranslateDisPerFlod = mTranslateDis / mNumOfFolds;

        int alpha = (int) (255 * (1 - mFactor));
        //   给0  2  4  6 四片区域的全部像素加上变化的透明颜色
        mSolidPaint.setColor(Color.argb((int) (alpha * 0.8F), 0, 0, 0));

        mShadowGradientMatrix.setScale(mFlodWidth, 1);
        mShadowGradientShader.setLocalMatrix(mShadowGradientMatrix);
        //   1  3  5  7四个区域的渐变的阴影     也需要随滑动改变Alpha
        mShadowPaint.setAlpha(alpha);

        float depth = (float) (Math.sqrt(mFlodWidth * mFlodWidth
                - mTranslateDisPerFlod * mTranslateDisPerFlod) / 2);

        float anchorPoint = mAnchor * w;
        float midFold = (anchorPoint / mFlodWidth);

        float[] src = new float[NUM_OF_POINT];  //代表变换前的坐标
        float[] dst = new float[NUM_OF_POINT]; // 代表变换后的坐标

        for (int i = 0; i < mNumOfFolds; i++) {
            mMatrices[i].reset();
            src[0] = i * mFlodWidth;
            src[1] = 0;
            src[2] = src[0] + mFlodWidth;
            src[3] = 0;
            src[4] = src[2];
            src[5] = h;
            src[6] = src[0];
            src[7] = src[5];

            boolean isEven = i % 2 == 0;

            dst[0] = i * mTranslateDisPerFlod;
            dst[1] = isEven ? 0 : depth;

            dst[2] = dst[0] + mTranslateDisPerFlod;
            // 引入anchor
            dst[0] = (anchorPoint > i * mFlodWidth) ? anchorPoint
                    + (i - midFold) * mTranslateDisPerFlod : anchorPoint
                    - (midFold - i) * mTranslateDisPerFlod;
            dst[2] = (anchorPoint > (i + 1) * mFlodWidth) ? anchorPoint
                    + (i + 1 - midFold) * mTranslateDisPerFlod : anchorPoint
                    - (midFold - i - 1) * mTranslateDisPerFlod;

            dst[3] = isEven ? depth : 0;
            dst[4] = dst[2];
            dst[5] = isEven ? h - depth : h;
            dst[6] = dst[0];
            dst[7] = isEven ? h : h - depth;

            for (int y = 0; y < 8; y++) {
                dst[y] = Math.round(dst[y]);
            }

            mMatrices[i].setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        }
    }


    /**
     * ViewGroup 中绘制工作的处理     方法中直接 return  不绘制
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (mFactor == 0)  //  全部折叠不绘制
            return;
        if (mFactor == 1)  // 不折叠不做特殊绘制处理   交给父类方法处理
        {
            super.dispatchDraw(canvas);
            return;
        }
        for (int i = 0; i < mNumOfFolds; i++) {
            canvas.save();

            canvas.concat(mMatrices[i]);
            canvas.clipRect(mFlodWidth * i, 0, mFlodWidth * i + mFlodWidth,
                    getHeight());
            if (isReady) {
                canvas.drawBitmap(mBitmap, 0, 0, null);
            } else {
                // super.dispatchDraw(canvas);
                super.dispatchDraw(mCanvas);
                canvas.drawBitmap(mBitmap, 0, 0, null);
                isReady = true;
            }
            canvas.translate(mFlodWidth * i, 0);
            if (i % 2 == 0) {
                canvas.drawRect(0, 0, mFlodWidth, getHeight(), mSolidPaint);
            } else {
                canvas.drawRect(0, 0, mFlodWidth, getHeight(), mShadowPaint);
            }
            canvas.restore();
        }
    }

    /**
     * 设置折叠布局的缩放比
     *
     * @param factor
     */
    public void setFactor(float factor) {
        this.mFactor = factor;
        updateFold();
        invalidate();
//		postInvalidate();
    }

    public float getFactor() {
        return mFactor;
    }


    /**
     * 设置折叠布局  是向左还是向右折叠   0 （最左边） ？   1（最右边）
     *
     * @param anchor
     */
    public void setAnchor(float anchor) {
        this.mAnchor = anchor;
        updateFold();
        invalidate();
//		postInvalidate();    在UI线程中作用和invalidate() 一样
//     区别：postInvalidate可以在子线程中直接调用  而invalidate不可以  但可以借用handle
    }

}
