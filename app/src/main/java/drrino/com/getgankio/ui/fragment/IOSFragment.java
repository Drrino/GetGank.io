package drrino.com.getgankio.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import drrino.com.getgankio.R;
import drrino.com.getgankio.core.GankApi;
import drrino.com.getgankio.core.GankFactory;
import drrino.com.getgankio.data.entity.Gank;
import drrino.com.getgankio.ui.activity.WebActivity;
import drrino.com.getgankio.ui.adapter.GankArticleAdapter;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Coder on 16/3/26.
 */
public class IOSFragment extends BaseSwipeFragment implements GankArticleAdapter.IClickItem {
  @Bind(R.id.rv_gank) RecyclerView mRecyclerView;

  private GankArticleAdapter mAdapter;
  private boolean mHasMoreData = true;
  private int mCurrentPage = 1;
  private static final int PAGE_SIZE = 10;

  private static final GankApi mGankApi = GankFactory.getGankApiInstance();

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    new Handler().postDelayed(this::showRefresh, 500);
    setupRecyclerView();
    getIOSData();
  }

  private void setupRecyclerView() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(linearLayoutManager);
    mAdapter = new GankArticleAdapter(getContext());
    mAdapter.setIClickItem(this);
    mRecyclerView.setAdapter(mAdapter);

    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        boolean isBottom = linearLayoutManager.findLastCompletelyVisibleItemPosition()
            >= mAdapter.getItemCount() - 4;
        if (!mSwipeRefreshLayout.isRefreshing() && isBottom && mHasMoreData) {
          showRefresh();
          getDataMore();
        }
      }
    });
  }

  private void getIOSData() {
    mGankApi.getIOSData(PAGE_SIZE, mCurrentPage)
        .map(IOSData -> IOSData.results)
        .flatMap(Observable::from)
        .toSortedList((IOSData1, IOSData2) -> IOSData2.publishedAt.compareTo(
            IOSData1.publishedAt))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(IOSData -> {
          if (IOSData.isEmpty()) {
            showEmptyView();
          } else if (IOSData.size() < PAGE_SIZE) {
            reloadData(IOSData);
            hasNoMoreData();
          } else if (IOSData.size() == PAGE_SIZE) {
            reloadData(IOSData);
            mCurrentPage++;
          }
          hideRefresh();
        });
  }

  private void reloadData(List<Gank> androidData) {
    mAdapter.updateWithClear(androidData);
  }

  private void getDataMore() {
    mGankApi.getIOSData(PAGE_SIZE, mCurrentPage)
        .map(IOSData -> IOSData.results)
        .flatMap(Observable::from)
        .toSortedList((IOSData1, IOSData2) -> IOSData2.publishedAt.compareTo(
            IOSData1.publishedAt))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(IOSData -> {
          if (IOSData.isEmpty()) {
            hasNoMoreData();
          } else if (IOSData.size() < PAGE_SIZE) {
            appendMoreDataToView(IOSData);
            hasNoMoreData();
          } else if (IOSData.size() == PAGE_SIZE) {
            appendMoreDataToView(IOSData);
            mCurrentPage++;
          }
          hideRefresh();
        });
  }

  private void showEmptyView() {
    Snackbar.make(mRecyclerView, R.string.empty_data_of_android, Snackbar.LENGTH_SHORT).show();
  }

  private void appendMoreDataToView(List<Gank> IOSData) {
    mAdapter.update(IOSData);
  }

  private void hasNoMoreData() {
    mHasMoreData = false;
    Snackbar.make(mRecyclerView, R.string.no_more_gank, Snackbar.LENGTH_LONG)
        .setAction(R.string.return_top, v -> {
          mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, 0);
        })
        .show();
  }

  private void showRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);
  }

  @Override protected int getLayoutId() {
    return R.layout.fragment_gank;
  }

  @Override protected void onRefreshStarted() {
    getIOSData();
  }

  private boolean shouldReloadData() {
    return mCurrentPage <= 3;
  }

  public void resetCurrentPage() {
    mCurrentPage = 1;
  }

  @Override protected boolean prepareRefresh() {
    if (shouldReloadData()) {
      resetCurrentPage();
      return true;
    } else {
      return false;
    }
  }

  @Override public void onClickGankItemAndroid(Gank gank, View view) {
    WebActivity.gotoWebActivity(getActivity(), gank.url, gank.desc);
  }
}
