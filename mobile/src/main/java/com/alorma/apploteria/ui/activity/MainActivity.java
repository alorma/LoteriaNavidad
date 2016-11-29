package com.alorma.apploteria.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.alorma.apploteria.R;
import com.alorma.apploteria.domain.ResourceCleanUp;
import com.alorma.apploteria.domain.usecase.GetGamesUseCase;
import com.alorma.apploteria.ui.fragment.GamesListFragment;
import javax.inject.Inject;

public class MainActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.content, new GamesListFragment());
    ft.commit();
  }
}
