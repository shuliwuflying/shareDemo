package com.flying.common;

/**
 * Created by aspsine on 16/3/7.
 */
public interface RefreshListener {

    void onStart(boolean automatic, int headerHeight, int finalHeight);

    void onMove(boolean finished, boolean automatic, int moved);

    void onRefresh();

    void onRelease();

    void onComplete();

    void onReset();
}
