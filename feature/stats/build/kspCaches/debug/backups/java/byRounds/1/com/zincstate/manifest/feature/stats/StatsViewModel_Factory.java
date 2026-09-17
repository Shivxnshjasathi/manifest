package com.zincstate.manifest.feature.stats;

import com.zincstate.manifest.core.database.dao.CategoryDao;
import com.zincstate.manifest.core.database.dao.TransactionDao;
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
public final class StatsViewModel_Factory implements Factory<StatsViewModel> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  public StatsViewModel_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<CategoryDao> categoryDaoProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
  }

  @Override
  public StatsViewModel get() {
    return newInstance(transactionDaoProvider.get(), categoryDaoProvider.get());
  }

  public static StatsViewModel_Factory create(Provider<TransactionDao> transactionDaoProvider,
      Provider<CategoryDao> categoryDaoProvider) {
    return new StatsViewModel_Factory(transactionDaoProvider, categoryDaoProvider);
  }

  public static StatsViewModel newInstance(TransactionDao transactionDao, CategoryDao categoryDao) {
    return new StatsViewModel(transactionDao, categoryDao);
  }
}
