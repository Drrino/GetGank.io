package drrino.com.getgankio.ui.fragment;

import android.support.design.widget.Snackbar;
import drrino.com.getgankio.R;
import drrino.com.getgankio.core.GankApi;
import drrino.com.getgankio.core.GankFactory;
import drrino.com.getgankio.data.entity.Gank;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Coder on 16/4/22.
 */
public class AndroidFragment extends BaseArticleFragment {

  private int mCurrentPage = 1;
  private static final int PAGE_SIZE = 15;
  private static final GankApi mGankApi = GankFactory.getGankApiInstance();

  @Override protected void getData() {
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

  @Override protected void getDataMore() {
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

  private void appendMoreDataToView(List<Gank> androidData) {
    mAdapter.update(androidData);
  }

  private void reloadData(List<Gank> androidData) {
    mAdapter.updateWithClear(androidData);
  }

  private void showEmptyView() {
    Snackbar.make(mRecyclerView, R.string.empty_data_of_article, Snackbar.LENGTH_SHORT).show();
  }

  private void hasNoMoreData() {
    mHasMoreData = false;
    Snackbar.make(mRecyclerView, R.string.no_more_gank, Snackbar.LENGTH_LONG)
        .setAction(R.string.return_top, v -> {
          mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, 0);
        })
        .show();
  }
}
