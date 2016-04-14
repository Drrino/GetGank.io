package drrino.com.getgankio.data.entity;

import drrino.com.getgankio.core.GankCategory;
import java.util.Date;

/**
 * Created by Administrator on 16/03/03.
 */
public class Gank extends Soul implements Cloneable {
  public boolean used;
  public String type;
  public String url;
  public String who;
  public String desc;
  public Date updatedAt;
  public Date createdAt;
  public Date publishedAt;

  public boolean isHeader;

  public boolean isGirl() {
    return type.equals(GankCategory.福利.name());
  }

  public boolean isAndroid() {
    return type.equals(GankCategory.Android.name());
  }

  @Override public Gank clone() {
    Gank gank = null;
    try {
      gank = (Gank) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return gank;
  }
}
