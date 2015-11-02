package net.masonliu.gridviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumeng on 10/30/15.
 */
public class GridViewPager extends ViewPager {
    private List<GridView> mLists = new ArrayList<>();
    private GridViewPagerAdapter adapter;
    private int sizeAll;
    private int rowInOnePage;
    private int columnInOnePage;
    private GridViewPagerDataAdapter gridViewPagerDataAdapter;
    public GridViewPager(Context context) {
        super(context);
    }

    public GridViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewPagerDataAdapter getGridViewPagerDataAdapter() {
        return gridViewPagerDataAdapter;
    }

    public void setGridViewPagerDataAdapter(GridViewPagerDataAdapter gridViewPagerDataAdapter) {
        this.gridViewPagerDataAdapter = gridViewPagerDataAdapter;
        sizeAll = gridViewPagerDataAdapter.sizeAll;
        rowInOnePage = gridViewPagerDataAdapter.rowInOnePage;
        columnInOnePage = gridViewPagerDataAdapter.columnInOnePage;
        init();
    }

    public void init() {
        int sizeInOnePage = rowInOnePage * columnInOnePage;
        int pageCount = sizeAll / sizeInOnePage;
        pageCount += sizeAll % sizeInOnePage == 0 ? 0 : 1;
        for (int i = 0; i < pageCount; i++) {
            final int pageIndex = i;
            WrapContentGridView gv = new WrapContentGridView(getContext());
            int end = Math.min((i + 1) * sizeInOnePage, sizeAll);
            gv.setAdapter(gridViewPagerDataAdapter.getGridViewAdapter(sizeInOnePage, i, i * sizeInOnePage, end));
            gv.setGravity(Gravity.CENTER);
            gv.setClickable(true);
            gv.setFocusable(true);
            gv.setNumColumns(columnInOnePage);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    gridViewPagerDataAdapter.onItemClick(arg0, arg1, arg2, arg3, pageIndex);
                }
            });
            mLists.add(gv);
        }
        adapter = new GridViewPagerAdapter(getContext(), mLists);
        setAdapter(adapter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height+getPaddingBottom()+getPaddingTop(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
