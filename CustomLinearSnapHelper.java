package com.iberdrola.clientes.presentation.views.customviews.horizontalrecyclerview;

import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CustomLinearSnapHelper extends LinearSnapHelper {

    private RecyclerView recyclerView;
    private int selectedPosition;

    public CustomLinearSnapHelper(RecyclerView recyclerView, int selectedPosition)
    {
        super();
        this.recyclerView = recyclerView;
        this.selectedPosition = selectedPosition;
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        View view = super.findSnapView(layoutManager);

        if (view != null) {
            final int newPosition = layoutManager.getPosition(view);

            if ( newPosition != selectedPosition && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                onViewSnapped(newPosition);
                selectedPosition = newPosition;
            }

        }

        return view;
    }

    private void onViewSnapped(int position)
    {
        //...
    }

}

