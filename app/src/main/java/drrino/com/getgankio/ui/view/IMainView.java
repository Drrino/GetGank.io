package drrino.com.getgankio.ui.view;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 16/03/03.
 */
public interface IMainView {
  void closeDrawer();
  void replaceFragment(int id,Fragment fragment);
}
