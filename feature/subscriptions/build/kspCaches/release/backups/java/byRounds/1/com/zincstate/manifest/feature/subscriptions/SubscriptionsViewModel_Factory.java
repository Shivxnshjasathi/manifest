package com.zincstate.manifest.feature.subscriptions;

import com.zincstate.manifest.core.database.dao.RecurringTransactionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class SubscriptionsViewModel_Factory implements Factory<SubscriptionsViewModel> {
  private final Provider<RecurringTransactionDao> recurringDaoProvider;

  public SubscriptionsViewModel_Factory(Provider<RecurringTransactionDao> recurringDaoProvider) {
    this.recurringDaoProvider = recurringDaoProvider;
  }

  @Override
  public SubscriptionsViewModel get() {
    return newInstance(recurringDaoProvider.get());
  }

  public static SubscriptionsViewModel_Factory create(
      Provider<RecurringTransactionDao> recurringDaoProvider) {
    return new SubscriptionsViewModel_Factory(recurringDaoProvider);
  }

  public static SubscriptionsViewModel newInstance(RecurringTransactionDao recurringDao) {
    return new SubscriptionsViewModel(recurringDao);
  }
}
