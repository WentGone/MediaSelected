package wg.com.photoselected.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpanCount;
    private int mSpacing;
    private int mTopMargin = -1;

    private boolean isIncludeEdge = true;

    public GridSpacingItemDecoration(int spanCount, int spacing) {
        this(spanCount, spacing, true);
    }

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        mSpanCount = spanCount;
        mSpacing = spacing;
        isIncludeEdge = includeEdge;
    }

    public GridSpacingItemDecoration(int spanCount, int spacing, int topMargin) {
        this(spanCount, spacing);
        mTopMargin = topMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % mSpanCount; // item column

        if (isIncludeEdge) {
            outRect.left = mSpacing - column * mSpacing / mSpanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * mSpacing / mSpanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < mSpanCount) { // top edge
                if (mTopMargin <= -1) outRect.top = mSpacing;
                else outRect.top = mTopMargin;
            }
            outRect.bottom = mSpacing; // item bottom
        } else {
            outRect.left = column * mSpacing / mSpanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= mSpanCount) {
                if (mTopMargin <= -1) outRect.top = mSpacing;
                else outRect.top = mTopMargin;
            }
        }
    }

}
