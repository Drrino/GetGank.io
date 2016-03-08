package drrino.com.getgankio.ui.view;

import android.view.View;
import drrino.com.getgankio.data.entity.Gank;

/**
 * Created by Administrator on 16/03/03.
 */
public interface IClickItem {
  /**
   * click item of gank girls info
   * @param gank
   * @param viewImage
   * @param viewText
   */
  void onClickGankItemGirl(Gank gank, View viewImage, View viewText);

  /**
   * click gank normal info
   * @param gank
   * @param view
   */
  void onClickGankItemNormal(Gank gank, View view);
}
