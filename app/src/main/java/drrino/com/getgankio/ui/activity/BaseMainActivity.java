package drrino.com.getgankio.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 16/03/03.
 */
public abstract class BaseMainActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initView(savedInstanceState);
  }

  protected abstract void initView(Bundle savedInstanceState);

  protected void addFragment(int containerId, Fragment fragment, String tag) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.add(containerId, fragment, tag);
    transaction.commit();
  }

  public void replaceFragment(int id_content, Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(id_content, fragment);
    transaction.commit();
  }
}
