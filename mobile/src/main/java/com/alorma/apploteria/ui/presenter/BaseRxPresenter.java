package com.alorma.apploteria.ui.presenter;

import android.support.annotation.Nullable;
import com.alorma.apploteria.domain.usecase.UseCase;
import com.alorma.apploteria.inject.named.MainScheduler;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;

/**
 * Represents base RxJava presenter.
 * Just only one Observable could be subscribed to it.
 */
public abstract class BaseRxPresenter<REQUEST, RESPONSE, VIEW extends View<RESPONSE>> extends BasePresenter<VIEW> {

  protected final Scheduler mainScheduler;
  protected final Scheduler ioScheduler;
  private UseCase<REQUEST, RESPONSE> useCase;
  protected Subscriber<RESPONSE> subscriber;
  private Integer page;

  public BaseRxPresenter(@MainScheduler Scheduler mainScheduler, Scheduler ioScheduler,
      UseCase<REQUEST, RESPONSE> useCase) {
    this.mainScheduler = mainScheduler;
    this.ioScheduler = ioScheduler;
    this.useCase = useCase;
  }

  /**
   * Executes request doesn't assign page to request.
   *
   * @param request item to execute
   */
  public void execute(REQUEST request) {
    if (!isViewAttached()) return;

    subscribe(useCase.execute(request), false);
  }

  /**
   * Creates internal subscriber and attaches it to observable argument.
   *
   * @param observable the object to subscribe
   * @param isFromPaginated indicates that request is paginated or not
   */
  protected void subscribe(Observable<RESPONSE> observable, boolean isFromPaginated) {
    if (!isViewAttached()) return;

    getView().showLoading();

    unsubscribe();

    subscriber = new Subscriber<RESPONSE>() {
      @Override
      public void onCompleted() {
        BaseRxPresenter.this.onCompleted();
      }

      @Override
      public void onError(Throwable e) {
        BaseRxPresenter.this.onError(e);
      }

      @Override
      public void onNext(RESPONSE response) {
        BaseRxPresenter.this.onNext(response, isFromPaginated);
      }
    };

    observable.subscribeOn(ioScheduler).observeOn(mainScheduler).timeout(20, TimeUnit.SECONDS).retry(3)
        .subscribe(subscriber);
  }

  /**
   * Unsubscribes internal subscriber and set it to null.
   */
  protected void unsubscribe() {
    if (subscriber != null && !subscriber.isUnsubscribed()) {
      subscriber.unsubscribe();
    }

    subscriber = null;
  }

  protected void onCompleted() {
    if (isViewAttached()) {
      getView().hideLoading();
    }
    unsubscribe();
  }

  protected void onError(Throwable throwable) {
    if (isViewAttached()) {
      getView().showError(throwable);
      getView().hideLoading();
      throwable.printStackTrace();
    }
    unsubscribe();
  }

  protected void onNext(RESPONSE response, boolean isFromPaginated) {
    if (isViewAttached()) {
      getView().onDataReceived(response);
    }
  }

  @Override
  public void detachView() {
    super.detachView();
    if (isViewAttached()) {
      getView().hideLoading();
    }
    unsubscribe();
  }

  @Nullable
  public Integer getPage() {
    return page;
  }
}