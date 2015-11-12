package com.zhimei.liang.customview.SlideFoldMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * MyLinearLayout 里面包含三个View 而且由于代码顺序是先绘制左边 -》 右边 所以中间的视图默认 是在左边之上 即覆盖了左边的侧滑菜单
 * 为了解决覆盖覆盖 所以改变这三个View 绘制的先后顺序 用重写getChildDrawingOrder 的方式
 */
public class MyLinearLayout extends LinearLayout {

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// Log.e("TAG", "MyLinearLayout");
		setChildrenDrawingOrderEnabled(true);
	}

	/**
	 * 返回要绘制的子元素在当前迭代中的索引。如果你需要修改子元素的绘制顺序，可以覆写此方法。默认情况下返回参数i值。
	 * 注意：为使此方法被调用，你必须先通过调用setChildrenDrawingOrderEnabled(true)启用子元素排序 
	 * 参数 int  i   :当前的迭代值 
	 * 返回值               :将要绘制的子元素在当前迭代的索引
	 */
	@Override
		protected int getChildDrawingOrder(int childCount, int i) {
		// Log.e("tag", "getChildDrawingOrder" + i + " , " + childCount);

		if (i == 0)
			return 1;
		if (i == 1)
			return 0;
		return super.getChildDrawingOrder(childCount, i);

	}

}
