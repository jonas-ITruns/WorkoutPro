package com.developer.workoutpro.itruns.workoutpro;

public interface ItemTouchHelperAdapter{

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

}
