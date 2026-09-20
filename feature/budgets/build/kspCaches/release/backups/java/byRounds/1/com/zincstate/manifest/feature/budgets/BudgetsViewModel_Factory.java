package com.zincstate.manifest.feature.budgets;

import com.zincstate.manifest.core.database.dao.BudgetDao;
import com.zincstate.manifest.core.database.dao.TransactionDao;
import com.zincstate.manifest.core.database.dao.TransactionSplitDao;
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
public final class BudgetsViewModel_Factory implements Factory<BudgetsViewModel> {
  private final Provider<BudgetDao> budgetDaoProvider;

  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<TransactionSplitDao> splitDaoProvider;

  public BudgetsViewModel_Factory(Provider<BudgetDao> budgetDaoProvider,
      Provider<TransactionDao> transactionDaoProvider,
      Provider<TransactionSplitDao> splitDaoProvider) {
    this.budgetDaoProvider = budgetDaoProvider;
    this.transactionDaoProvider = transactionDaoProvider;
    this.splitDaoProvider = splitDaoProvider;
  }

  @Override
  public BudgetsViewModel get() {
    return newInstance(budgetDaoProvider.get(), transactionDaoProvider.get(), splitDaoProvider.get());
  }

  public static BudgetsViewModel_Factory create(Provider<BudgetDao> budgetDaoProvider,
      Provider<TransactionDao> transactionDaoProvider,
      Provider<TransactionSplitDao> splitDaoProvider) {
    return new BudgetsViewModel_Factory(budgetDaoProvider, transactionDaoProvider, splitDaoProvider);
  }

  public static BudgetsViewModel newInstance(BudgetDao budgetDao, TransactionDao transactionDao,
      TransactionSplitDao splitDao) {
    return new BudgetsViewModel(budgetDao, transactionDao, splitDao);
  }
}
