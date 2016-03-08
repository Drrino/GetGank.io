package drrino.com.getgankio.ui.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 16/03/04.
 */
public class DateUtils {
  public static String toDate(Date date) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    return dateFormat.format(date);
  }
}
