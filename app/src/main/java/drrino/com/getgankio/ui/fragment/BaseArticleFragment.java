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

/**
 * Created by Coder on 16/4/22.
 */
public abstract class BaseArticleFragment extends BaseSwipeFragment
    implements GankArticleAdapter.IClickItem {
  @Bind(R.id.rv_gank) RecyclerView mRecyclerView;

  protected static final int PAGE_SIZE = 15;
  protected static final GankApi mGankApi = GankFactory.getGankApiInstance();
  protected GankArticleAdapter mAdapter;
  protected boolean mHasMoreData = true;
  protected int mCurrentPage = 1;

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    new Handler().postDelayed(this::showRefresh, 500);
    setupRecyclerView();
    getData();
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

  @Override protected int getLayoutId() {
    return R.layout.fragment_gank;
  }

  @Override protected void onRefreshStarted() {
    getData();
  }

  protected abstract void getData();

  protected abstract void getDataMore();

  private void showRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);
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

  protected void appendMoreDataToView(List<Gank> data) {
    mAdapter.update(data);
  }

  protected void reloadData(List<Gank> data) {
    mAdapter.updateWithClear(data);
  }

  protected void showEmptyView() {
    Snackbar.make(mRecyclerView, R.string.empty_data_of_article, Snackbar.LENGTH_SHORT).show();
  }

  protected void hasNoMoreData() {
    mHasMoreData = false;
    Snackbar.make(mRecyclerView, R.string.no_more_gank, Snackbar.LENGTH_LONG)
        .setAction(R.string.return_top, v -> {
          mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, 0);
        })
        .show();
  }

  @Override public void onClickGankItem(Gank gank, View view) {
    WebActivity.gotoWebActivity(getActivity(), gank.url, gank.desc);
  }
}
