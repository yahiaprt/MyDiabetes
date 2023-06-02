package com.mydiabetesprt.diabetes.partage.view.recyclerview.decorations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mydiabetesprt.diabetes.R;public class LiniarDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;

    public LiniarDividerItemDecoration(Context context) {
        divider = ContextCompat.getDrawable(context, R.drawable.separator);
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int index = 0; index < childCount; index++) {
            View child = parent.getChildAt(index);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
    }
}