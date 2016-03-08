package drrino.com.getgankio.ui.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;
import drrino.com.getgankio.R;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 16/03/04.
 */
public class GankUtils {
  public static boolean isAndroidL() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
  }

  /**
   * get accent color
   */
  public static int getAccentColor(Context context) {
    TypedValue typedValue = new TypedValue();
    Resources.Theme theme = context.getTheme();
    theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
    return typedValue.data;
  }

  /**
   * base on Toolbar get the TitleView by reflect way
   */
  public static TextView getTitleViewInToolbar(Toolbar toolbar) {
    TextView textView = null;
    try {
      Field title = toolbar.getClass().getDeclaredField("mTitleTextView");
      title.setAccessible(true);
      textView = (TextView) title.get(toolbar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return textView;
  }

  public static void copyToClipBoard(Context context, String text, String success) {
    ClipData clipData = ClipData.newPlainText("gank_copy", text);
    ClipboardManager manager =
        (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    manager.setPrimaryClip(clipData);
    Toast.makeText(context, success, Toast.LENGTH_SHORT).show();
  }
}
