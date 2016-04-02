package drrino.com.getgankio.ui.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import drrino.com.getgankio.R;
import drrino.com.getgankio.model.MenuItem;
import drrino.com.getgankio.ui.view.IMainView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16/03/03.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {
  public List<MenuItem> menuItems;
  private IMainView mainView;
  private MenuItem.FragmentType currentFragment;

  public MenuAdapter(Activity activity) {
    menuItems = new ArrayList<>();
    currentFragment = MenuItem.FragmentType.Gank;
    if (activity instanceof IMainView) {
      mainView = (IMainView) activity;
    } else {
      throw new IllegalArgumentException("The activity must be a MainActivity !");
    }
  }

  @Override public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false);
    return new MenuHolder(view);
  }

  @Override public void onBindViewHolder(MenuHolder holder, int position) {
    final MenuItem menuItem = menuItems.get(position);

    holder.tv_title.setText(menuItem.getTitle());
    holder.img_menu.setImageResource(menuItem.getResourceId());
    holder.rl_container.setOnClickListener(v -> {
      try {
        if (currentFragment != menuItem.getType()) {
          Fragment fragment =
              (Fragment) Class.forName(menuItem.getFragment().getName()).newInstance();
          mainView.replaceFragment(R.id.frame_container, fragment);
          currentFragment = menuItem.getType();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      mainView.closeDrawer();
    });
  }

  @Override public int getItemCount() {
    return menuItems.size();
  }

  class MenuHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.img_menu) ImageView img_menu;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.rl_container) RelativeLayout rl_container;

    public MenuHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
