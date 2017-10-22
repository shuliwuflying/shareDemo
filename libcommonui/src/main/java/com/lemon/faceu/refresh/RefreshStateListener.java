package com.lemon.faceu.refresh;

/**
 * @author: liwushu
 * @description: 滑动状态监听
 * @created: 2017/10/22
 * @version: 1.0
 * @modify: liwushu
*/
public interface RefreshStateListener {

    void onStart(boolean automatic, int headerHeight, int finalHeight);

    void onMove(boolean finished, boolean automatic, int moved);

    void onRefresh();

    void onRelease();

    void onComplete();

    void onReset();
}
