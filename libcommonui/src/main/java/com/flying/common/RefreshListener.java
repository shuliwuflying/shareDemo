package com.flying.common;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/10/15
 * @version: 1.0
 * @modify: liwushu
*/
public interface RefreshListener {

    void onStart(boolean automatic, int headerHeight, int finalHeight);

    void onMove(boolean finished, boolean automatic, int moved);

    void onRefresh();

    void onRelease();

    void onComplete();

    void onReset();
}
