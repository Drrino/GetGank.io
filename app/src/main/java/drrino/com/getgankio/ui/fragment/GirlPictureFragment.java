package drrino.com.getgankio.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import butterknife.Bind;
import drrino.com.getgankio.R;
import drrino.com.getgankio.core.GankApi;
import drrino.com.getgankio.core.GankFactory;
import drrino.com.getgankio.data.entity.Gank;
import drrino.com.getgankio.data.entity.Girl;
import drrino.com.getgankio.ui.activity.GirlPictureActivity;
import drrino.com.getgankio.ui.adapter.GirlPictureAdapter;
import drrino.com.getgankio.ui.util.DateUtils;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Coder on 16/3/8.
 */
public class GirlPictureFragment extends BaseSwipeFragment
    implements GirlPictureAdapter.IClickItem {

  @Bind(R.id.rv_gank) RecyclerView mRecyclerView;

  private GirlPictureAdapter mAdapter;
  private boolean mHasMoreData = true;
  private int mCurrentPage = 1;

  /**
   * the count of the size of one request
   */
  private static final int PAGE_SIZE = 10;

  private static final GankApi mGankApi = GankFactory.getGankApiInstance();

  @Override protected int getLayoutId() {
    return R.layout.fragment_gank;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    new Handler().postDelayed(this::showRefresh, 500);
    setupRecyclerView();
    getGirlsData();
  }

  private void setupRecyclerView() {
    StaggeredGridLayoutManager staggeredGridLayoutManager =
        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    mAdapter = new GirlPictureAdapter(getActivity());
    mAdapter.setIClickItem(this);
    mRecyclerView.setAdapter(mAdapter);

    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        boolean isBottom =
            staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(new int[2])[1]
                >= mAdapter.getItemCount() - 4;
        if (!mSwipeRefreshLayout.isRefreshing() && isBottom && mHasMoreData) {
          showRefresh();
          getDataMore();
        }
      }
    });
  }

  private void getDataMore() {
    mGankApi.getGirlData(PAGE_SIZE, mCurrentPage)
        .map(girlData -> girlData.results)
        .flatMap(Observable::from)
        .toSortedList((girl, girl2) -> girl2.publishedAt.compareTo(girl.publishedAt))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(girls -> {
          if (girls.isEmpty()) {
            hasNoMoreData();
          } else if (girls.size() < PAGE_SIZE) {
            appendMoreDataToView(girls);
            hasNoMoreData();
          } else if (girls.size() == PAGE_SIZE) {
            appendMoreDataToView(girls);
            mCurrentPage++;
          }
          hideRefresh();
        });
  }

  public void appendMoreDataToView(List<Girl> data) {
    mAdapter.update(data);
  }

  public void hasNoMoreData() {
    mHasMoreData = false;
    Snackbar.make(mRecyclerView, R.string.no_more_girls, Snackbar.LENGTH_SHORT)
        .setAction(R.string.action_to_top, v -> {
          (mRecyclerView.getLayoutManager()).smoothScrollToPosition(mRecyclerView, null, 0);
        })
        .show();
  }

  /**
   * reload girls data and clear history girls
   */
  private void getGirlsData() {
    mGankApi.getGirlData(PAGE_SIZE, mCurrentPage)
        .map(girlData -> girlData.results)
        .flatMap(Observable::from)
        .toSortedList((girl, girl2) -> girl2.publishedAt.compareTo(girl.publishedAt))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(girls -> {
          if (girls.isEmpty()) {
            showEmptyView();
          } else if (girls.size() < PAGE_SIZE) {
            fillData(girls);
            hasNoMoreData();
          } else if (girls.size() == PAGE_SIZE) {
            fillData(girls);
            mCurrentPage++;
          }
          hideRefresh();
        });
  }

  private void fillData(List<Girl> girls) {
    mAdapter.updateWithClear(girls);
  }

  private void showEmptyView() {
    Snackbar.make(mRecyclerView, R.string.empty_data_of_girls, Snackbar.LENGTH_SHORT).show();
  }

  @Override protected void onRefreshStarted() {
    getGirlsData();
  }

  @Override protected boolean prepareRefresh() {
    if (shouldRefillGirls()) {
      resetCurrentPage();
      return true;
    } else {
      return false;
    }
  }

  private boolean shouldRefillGirls() {
    return mCurrentPage <= 2;
  }

  public void resetCurrentPage() {
    mCurrentPage = 1;
  }

  private void showRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);
  }

  @Override public void onClickPhoto(int position, View view, View textView) {
    Girl girlDetail = mAdapter.getGirl(position);
    if (girlDetail != null) {
      Gank gank = new Gank();
      gank.type = girlDetail.type;
      gank.url = girlDetail.url;
      gank.publishedAt = girlDetail.publishedAt;
      GirlPictureActivity.gotoGirlDetail(getActivity(), gank.url,
          DateUtils.toDate(gank.publishedAt), view, textView);
    }
  }
}
