package com.flying.common;

/**
 * @author: liwushu
 * @description: refresh 过程状态回调
 * @created: 2017/10/15
 * @version: 1.0
 * @modify: liwushu
*/
public interface RefreshStatusListener {

    void onStart(boolean automatic, int headerHeight, int finalHeight);

    void onMove(boolean finished, boolean automatic, int moved);

    void onRefresh();

    void onRelease();

    void onComplete();

    void onReset();
}
