package drrino.com.getgankio.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import drrino.com.getgankio.R;
import drrino.com.getgankio.core.GankApi;
import drrino.com.getgankio.core.GankFactory;
import drrino.com.getgankio.data.entity.Girl;
import drrino.com.getgankio.ui.adapter.GirlPictureAdapter;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Coder on 16/3/8.
 */
public class GirlPictureFragment extends Fragment implements GirlPictureAdapter.IClickItem {

  @Bind(R.id.recycler_girl) RecyclerView mRecyclerGirl;
  @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

  private GirlPictureAdapter mAdapter;
  private boolean hasLoadMoreData = false;
  private boolean mHasMoreData = true;
  private int mCurrentPage = 1;

  /**
   * the count of the size of one request
   */
  private static final int PAGE_SIZE = 10;

  private static final GankApi mGankApi = GankFactory.getGankApiInstance();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.list_girl_picture, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initRecyclerView();
    setupSwipeRefreshLayout();

    new Handler().postDelayed(() -> showRefresh(), 1000);
    getGirlsData();
  }

  private void initRecyclerView() {
    StaggeredGridLayoutManager staggeredGridLayoutManager =
        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    mRecyclerGirl.setLayoutManager(staggeredGridLayoutManager);
    mAdapter = new GirlPictureAdapter(getActivity());
    mAdapter.setIClickItem(this);
    mRecyclerGirl.setAdapter(mAdapter);

    mRecyclerGirl.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        .flatMap(girls -> Observable.from(girls))
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
        });
  }

  public void appendMoreDataToView(List<Girl> data) {
    mAdapter.update(data);
  }

  public void hasNoMoreData() {
    mHasMoreData = false;
    Snackbar.make(mRecyclerGirl, R.string.no_more_girls, Snackbar.LENGTH_SHORT)
        .setAction(R.string.action_to_top, v -> {
          (mRecyclerGirl.getLayoutManager()).smoothScrollToPosition(mRecyclerGirl, null, 0);
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
          getDataFinish();
        });
  }

  private void getDataFinish() {
    new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
  }

  private void fillData(List<Girl> girls) {
    mAdapter.updateWithClear(girls);
  }

  private void showEmptyView() {
    Snackbar.make(mRecyclerGirl, R.string.empty_data_of_girls,Snackbar.LENGTH_SHORT).show();
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
    getGirlsData();
  }

  private boolean prepareRefresh() {
    if(shouldRefillGirls()){
      resetCurrentPage();
      if(!mSwipeRefreshLayout.isRefreshing()){
        showRefresh();
      }
      return true;
    }else{
      return false;
    }
  }

  private boolean shouldRefillGirls() {
    return mCurrentPage<=2;
  }

  public void resetCurrentPage(){
    mCurrentPage = 1;
  }

  private void showRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onClickPhoto(int position, View view, View textView) {

  }
}
