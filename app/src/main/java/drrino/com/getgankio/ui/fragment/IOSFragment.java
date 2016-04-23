package drrino.com.getgankio.ui.fragment;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Coder on 16/4/22.
 */
public class IOSFragment extends BaseArticleFragment {

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
}
