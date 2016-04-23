package drrino.com.getgankio.ui.fragment;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Coder on 16/4/22.
 */
public class AndroidFragment extends BaseArticleFragment {

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
}
