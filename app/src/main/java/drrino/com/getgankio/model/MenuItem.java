package drrino.com.getgankio.model;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 16/03/03.
 */
public class MenuItem {
  public enum FragmentType {
    Gank, Girl, Android, IOS
  }

  private String title;
  private int resourceId;
  private FragmentType type;
  private Class<? extends Fragment> fragment;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getResourceId() {
    return resourceId;
  }

  public Class<? extends Fragment> getFragment() {
    return fragment;
  }

  public FragmentType getType() {
    return type;
  }

  public MenuItem() {
  }

  public MenuItem(String title, int resourceId, Class<? extends Fragment> fragment) {
    this.resourceId = resourceId;
    this.title = title;
    this.fragment = fragment;
  }

  public MenuItem(String title, int resourceId, FragmentType type,
      Class<? extends Fragment> fragment) {
    this.title = title;
    this.resourceId = resourceId;
    this.type = type;
    this.fragment = fragment;
  }
}
