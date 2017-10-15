package com.slive.demo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.flying.common.RefreshRecyclerView;
import com.flying.common.OnLoadMoreListener;
import com.flying.common.OnRefreshListener;
import com.slive.demo.R;
import com.slive.demo.model.Image;
import com.slive.demo.network.NetworkAPI;
import com.slive.demo.ui.adapter.ImageAdapter;
import com.slive.demo.ui.adapter.OnItemClickListener;
import com.slive.demo.ui.widget.BannerView;
import com.slive.demo.ui.widget.footer.LoadMoreFooterView;
import com.slive.demo.ui.widget.header.BatVsSupperHeaderView;
import com.slive.demo.ui.widget.header.ClassicRefreshHeaderView;
import com.slive.demo.utils.DensityUtils;
import com.slive.demo.utils.ListUtils;

import java.util.List;


public class MainActivity extends AppCompatActivity implements OnItemClickListener<Image>, OnRefreshListener, OnLoadMoreListener {

    private RefreshRecyclerView refreshRecyclerView;
    private BannerView bannerView;
    private LoadMoreFooterView loadMoreFooterView;

    private ImageAdapter mAdapter;

    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshRecyclerView = (RefreshRecyclerView) findViewById(R.id.iRecyclerView);
        refreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMoreFooterView = (LoadMoreFooterView) refreshRecyclerView.getLoadMoreFooterView();

        mAdapter = new ImageAdapter();
        refreshRecyclerView.setAdapter(mAdapter);

        refreshRecyclerView.setOnRefreshListener(this);
        refreshRecyclerView.setOnLoadMoreListener(this);

        mAdapter.setOnItemClickListener(this);

        refreshRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                refreshRecyclerView.setRefreshing(true);
            }
        });
    }


    @Override
    public void onItemClick(int position, Image image, View v) {
        mAdapter.remove(position);
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.LOAD_GONE);
        refresh();
    }

    @Override
    public void onLoadMore() {
        Log.e("TAG","onLoadMore");
        if (loadMoreFooterView.canLoadMore() && mAdapter.getItemCount() > 0) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.LOADING);
            loadMore();
        }
    }

    private void toggleRefreshHeader() {
        if (refreshRecyclerView.getRefreshHeaderView() instanceof BatVsSupperHeaderView) {
            ClassicRefreshHeaderView classicRefreshHeaderView = new ClassicRefreshHeaderView(this);
            classicRefreshHeaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(this, 80)));
            // we can set view
            refreshRecyclerView.setRefreshHeaderView(classicRefreshHeaderView);
            Toast.makeText(this, "Classic style", Toast.LENGTH_SHORT).show();
        } else if (refreshRecyclerView.getRefreshHeaderView() instanceof ClassicRefreshHeaderView) {
            // we can also set layout
            refreshRecyclerView.setRefreshHeaderView(R.layout.recyclerview_refresh_header_layout);
            Toast.makeText(this, "Bat man vs Super man style", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBanner() {
        NetworkAPI.requestBanners(new NetworkAPI.Callback<List<Image>>() {
            @Override
            public void onSuccess(List<Image> images) {
                if (!ListUtils.isEmpty(images)) {
                    bannerView.setList(images);
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void refresh() {
        mPage = 1;
        NetworkAPI.requestImages(mPage, new NetworkAPI.Callback<List<Image>>() {
            @Override
            public void onSuccess(List<Image> images) {
                refreshRecyclerView.setRefreshing(false);
                if (ListUtils.isEmpty(images)) {
                    mAdapter.clear();
                } else {
                    mPage = 2;
                    mAdapter.setList(images);
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                refreshRecyclerView.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMore() {
        NetworkAPI.requestImages(mPage, new NetworkAPI.Callback<List<Image>>() {
            @Override
            public void onSuccess(final List<Image> images) {
                if (ListUtils.isEmpty(images)) {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.LOAD_END);
                    Log.e("TAG","images isEmpty");
                } else {
                    Log.e("TAG","images not Empty");
//                    mPage++;
//                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
//                    mAdapter.append(images);
                    /**
                     * FIXME here we post delay to see more animation, you don't need to do this.
                     */
                    loadMoreFooterView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPage++;
                            loadMoreFooterView.setStatus(LoadMoreFooterView.LOAD_GONE);
                            mAdapter.append(images);
                        }
                    }, 2000);
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                loadMoreFooterView.setStatus(LoadMoreFooterView.LOAD_ERROR);
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
