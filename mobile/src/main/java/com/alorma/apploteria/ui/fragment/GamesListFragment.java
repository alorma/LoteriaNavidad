package com.alorma.apploteria.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.alorma.apploteria.R;
import com.alorma.apploteria.domain.bean.Game;
import com.alorma.apploteria.inject.component.ApplicationComponent;
import com.alorma.apploteria.inject.module.GamesModule;
import com.alorma.apploteria.ui.presenter.impl.GamesListPresenter;
import java.util.List;
import javax.inject.Inject;

public class GamesListFragment extends BaseFragment implements com.alorma.apploteria.ui.presenter.View<List<Game>> {

  @Inject GamesListPresenter gamesListPresenter;

  @BindView(R.id.text) TextView textView;

  @Override
  protected void injectComponents(ApplicationComponent applicationComponent) {
    super.injectComponents(applicationComponent);
    applicationComponent.plus(new GamesModule()).inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    return inflater.inflate(R.layout.fragment_games_list, null, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
  }

  @Override
  public void onStart() {
    super.onStart();

    gamesListPresenter.attachView(this);
    gamesListPresenter.execute(null);
  }

  @Override
  public void onStop() {
    gamesListPresenter.detachView();
    super.onStop();
  }

  @Override
  public void showLoading() {

  }

  @Override
  public void hideLoading() {

  }

  @Override
  public void onDataReceived(List<Game> data) {
    if (data != null) {
      textView.setText("Data received: " + data.size());
    } else {
      textView.setText("Data received is null");
    }
  }

  @Override
  public void showError(Throwable throwable) {

  }
}