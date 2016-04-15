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
import drrino.com.getgankio.ui.adapter.GankAndroidAdapter;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Coder on 16/3/26.
 */
public class AndroidFragment extends BaseSwipeFragment implements GankAndroidAdapter.IClickItem {
  @Bind(R.id.rv_android) RecyclerView mRecyclerView;

  private GankAndroidAdapter mAdapter;
  private boolean mHasMoreData = true;
  private int mCurrentPage = 1;
  private static final int PAGE_SIZE = 10;

  private static final GankApi mGankApi = GankFactory.getGankApiInstance();

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    new Handler().postDelayed(this::showRefresh, 500);
    setupRecyclerView();
    getAndroidData();
  }

  private void setupRecyclerView() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(linearLayoutManager);
    mAdapter = new GankAndroidAdapter(getContext());
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
        } else {
          hasNoMoreData();
        }
      }
    });
  }

  private void getAndroidData() {
    mGankApi.getAndroidData(PAGE_SIZE, mCurrentPage)
        .map(androidData -> androidData.results)
        .flatMap(Observable::from)
        .toSortedList((androidData1, androidData2) -> androidData2.publishedAt.compareTo(
            androidData1.publishedAt))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(androidData -> {
          if (androidData.isEmpty()) {
            showEmptyView();
          } else if (androidData.size() < PAGE_SIZE) {
            reloadData(androidData);
            hasNoMoreData();
          } else if (androidData.size() == PAGE_SIZE) {
            reloadData(androidData);
            mCurrentPage++;
          }
          hideRefresh();
        });
  }

  private void reloadData(List<Gank> androidData) {
    mAdapter.updateWithClear(androidData);
  }

  private void getDataMore() {
    mGankApi.getAndroidData(PAGE_SIZE, mCurrentPage)
        .map(androidData -> androidData.results)
        .flatMap(Observable::from)
        .toSortedList((androidData1, androidData2) -> androidData2.publishedAt.compareTo(
            androidData1.publishedAt))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(androidData -> {
          if (androidData.isEmpty()) {
            hasNoMoreData();
          } else if (androidData.size() < PAGE_SIZE) {
            appendMoreDataToView(androidData);
            hasNoMoreData();
          } else if (androidData.size() == PAGE_SIZE) {
            appendMoreDataToView(androidData);
            mCurrentPage++;
          }
          hideRefresh();
        });
  }

  private void showEmptyView() {
    Snackbar.make(mRecyclerView, R.string.empty_data_of_android, Snackbar.LENGTH_SHORT).show();
  }

  private void appendMoreDataToView(List<Gank> androidData) {
    mAdapter.update(androidData);
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
    return R.layout.fragment_android;
  }

  @Override protected void onRefreshStarted() {
    getAndroidData();
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

  }
}
