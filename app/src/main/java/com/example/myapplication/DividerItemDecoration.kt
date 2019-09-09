package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecoration(divider: Drawable): RecyclerView.ItemDecoration() {
    private val mDivider: Drawable
    init{
        mDivider = divider
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) === 0)
        {
            return
        }
        outRect.top = mDivider.getIntrinsicHeight()
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.getPaddingLeft()
        val dividerRight = parent.getWidth() - parent.getPaddingRight()
        val childCount = parent.getChildCount()
        for (i in 0 until childCount - 1)
        {
            val child = parent.getChildAt(i)
            val params = child.getLayoutParams() as RecyclerView.LayoutParams
            val dividerTop = child.getBottom() + params.bottomMargin
            val dividerBottom = dividerTop + mDivider.getIntrinsicHeight()
            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider.draw(canvas)
        }
    }

}