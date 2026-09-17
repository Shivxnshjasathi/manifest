package com.zincstate.manifest.feature.goals;

import com.zincstate.manifest.core.database.dao.AccountDao;
import com.zincstate.manifest.core.database.dao.GoalDao;
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
public final class GoalsViewModel_Factory implements Factory<GoalsViewModel> {
  private final Provider<GoalDao> goalDaoProvider;

  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<AccountDao> accountDaoProvider;

  public GoalsViewModel_Factory(Provider<GoalDao> goalDaoProvider,
      Provider<TransactionDao> transactionDaoProvider, Provider<AccountDao> accountDaoProvider) {
    this.goalDaoProvider = goalDaoProvider;
    this.transactionDaoProvider = transactionDaoProvider;
    this.accountDaoProvider = accountDaoProvider;
  }

  @Override
  public GoalsViewModel get() {
    return newInstance(goalDaoProvider.get(), transactionDaoProvider.get(), accountDaoProvider.get());
  }

  public static GoalsViewModel_Factory create(Provider<GoalDao> goalDaoProvider,
      Provider<TransactionDao> transactionDaoProvider, Provider<AccountDao> accountDaoProvider) {
    return new GoalsViewModel_Factory(goalDaoProvider, transactionDaoProvider, accountDaoProvider);
  }

  public static GoalsViewModel newInstance(GoalDao goalDao, TransactionDao transactionDao,
      AccountDao accountDao) {
    return new GoalsViewModel(goalDao, transactionDao, accountDao);
  }
}
