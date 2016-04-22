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
public class IOSFragment extends BaseArticleFragment {

  private int mCurrentPage = 1;
  private static final int PAGE_SIZE = 15;
  private static final GankApi mGankApi = GankFactory.getGankApiInstance();

  @Override protected void getData() {
    mGankApi.getIOSData(PAGE_SIZE, mCurrentPage)
        .map(IOSData -> IOSData.results)
        .flatMap(Observable::from)
        .toSortedList((IOSData1, IOSData2) -> IOSData2.publishedAt.compareTo(IOSData1.publishedAt))
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

  @Override protected void getDataMore() {
    mGankApi.getIOSData(PAGE_SIZE, mCurrentPage)
        .map(IOSData -> IOSData.results)
        .flatMap(Observable::from)
        .toSortedList((IOSData1, IOSData2) -> IOSData2.publishedAt.compareTo(IOSData1.publishedAt))
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

  private void appendMoreDataToView(List<Gank> IOSData) {
    mAdapter.update(IOSData);
  }

  private void reloadData(List<Gank> IOSData) {
    mAdapter.updateWithClear(IOSData);
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
