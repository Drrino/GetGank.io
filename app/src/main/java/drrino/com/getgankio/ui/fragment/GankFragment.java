package drrino.com.getgankio.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import drrino.com.getgankio.R;
import drrino.com.getgankio.core.GankApi;
import drrino.com.getgankio.core.GankFactory;
import drrino.com.getgankio.data.GankData;
import drrino.com.getgankio.data.entity.Gank;
import drrino.com.getgankio.ui.activity.GirlPictureActivity;
import drrino.com.getgankio.ui.adapter.GankListAdapter;
import drrino.com.getgankio.ui.util.DateUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 16/03/03.
 */
public class GankFragment extends Fragment implements GankListAdapter.IClickItem {
  @Bind(R.id.rv_gank) RecyclerView mRecyclerView;
  @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

  private static final int DAY_OF_MILLISECOND = 24 * 60 * 60 * 1000;
  private int mCountOfGetMoreDataEmpty = 0;

  private boolean hasLoadMoreData = false;
  private boolean mHasMoreData = true;

  private static final GankApi mGankApi = GankFactory.getGankApiInstance();
  private GankListAdapter mAdapter;
  private Date mCurrentDate;
  List<Gank> mGankList = new ArrayList<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_gank, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupRecyclerView();
    setupSwipeRefreshLayout();

    new Handler().postDelayed(this::showRefresh, 1000);
    getData();
  }

  private void getData() {
    getData(new Date(System.currentTimeMillis()));
  }

  private void getData(Date date) {
    mCurrentDate = date;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    mGankApi.getGankData(year, month, day)
        .map(data -> data.results)
        .map(this::addAllResults)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Gank>>() {
          @Override public void onCompleted() {
            mCurrentDate = new Date(date.getTime() - DAY_OF_MILLISECOND);
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onNext(List<Gank> ganks) {
            if (ganks.isEmpty()) {
              getData(new Date(date.getTime() - DAY_OF_MILLISECOND));
            } else {
              mCountOfGetMoreDataEmpty = 0;
              fillData(ganks);
            }
            getDataFinish();
          }
        });
  }

  private void getDataFinish() {
    new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
  }

  private void fillData(List<Gank> data) {
    mAdapter.updateWithClear(data);
  }

  private void showRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);
  }

  private void setupRecyclerView() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(linearLayoutManager);
    mAdapter = new GankListAdapter(getContext());
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
        } else if (!mHasMoreData) {
          hasNoMoreData();
        }
      }
    });
  }

  private void hasNoMoreData() {
    mHasMoreData = false;
    Snackbar.make(mRecyclerView, R.string.no_more_gank, Snackbar.LENGTH_LONG)
        .setAction(R.string.return_top, v -> {
          mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, 0);
        })
        .show();
  }

  private void getDataMore() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(mCurrentDate);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    mGankApi.getGankData(year, month, day)
        .map(data -> data.results)
        .map(this::addAllResults)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Gank>>() {
          @Override public void onCompleted() {
            mCurrentDate = new Date(mCurrentDate.getTime() - DAY_OF_MILLISECOND);
            hasLoadMoreData = true;
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onNext(List<Gank> ganks) {
            if (ganks.isEmpty()) {
              mCountOfGetMoreDataEmpty += 1;
              if (mCountOfGetMoreDataEmpty >= 10) {
                hasNoMoreData();
              } else {
                getDataMore();
              }
            } else {
              mCountOfGetMoreDataEmpty = 0;
              appendMoreDataToView(ganks);
            }
            getDataFinish();
          }
        });
  }

  private void appendMoreDataToView(List<Gank> data) {
    mAdapter.update(data);
  }

  private List<Gank> addAllResults(GankData.Result results) {
    mGankList.clear();
    if (results.androidList != null) mGankList.addAll(results.androidList);
    if (results.iOSList != null) mGankList.addAll(results.iOSList);
    if (results.appList != null) mGankList.addAll(results.appList);
    if (results.拓展资源List != null) mGankList.addAll(results.拓展资源List);
    if (results.瞎推荐List != null) mGankList.addAll(results.瞎推荐List);
    if (results.休息视频List != null) mGankList.addAll(results.休息视频List);
    // make meizi data is in first position
    if (results.妹纸List != null) mGankList.addAll(0, results.妹纸List);
    return mGankList;
  }

  private void setupSwipeRefreshLayout() {
    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
    mSwipeRefreshLayout.setOnRefreshListener(() -> {
      if (prepareRefresh()) {
        onRefreshStarted();
      } else {
        hideRefresh();
      }
    });
  }

  private void hideRefresh() {
    mSwipeRefreshLayout.postDelayed(() -> {
      if (mSwipeRefreshLayout != null) {
        mSwipeRefreshLayout.setRefreshing(false);
      }
    }, 1000);
  }

  private void onRefreshStarted() {
    getData();
  }

  private boolean prepareRefresh() {
    return shouldRefillData();
  }

  private boolean shouldRefillData() {
    return !hasLoadMoreData;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onClickGankItemGirl(Gank gank, View viewImage, View viewText) {
    GirlPictureActivity.gotoGirlDetail(getActivity(), gank.url, DateUtils.toDate(gank.publishedAt),
        viewImage, viewText);
  }

  @Override public void onClickGankItemNormal(Gank gank, View view) {

  }
}