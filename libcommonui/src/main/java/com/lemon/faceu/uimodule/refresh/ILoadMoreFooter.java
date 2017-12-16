package com.lemon.faceu.uimodule.refresh;


/**
 * @author:shuliwu
 * @description: footview 接口
 * @version:1.0
 * @created:2017/10/23
 * @modify:
 */
public interface ILoadMoreFooter {
    void setStatus(LoadMoreStatus status);
    boolean canLoadMore();
}
