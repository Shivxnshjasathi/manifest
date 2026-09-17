package com.zincstate.manifest.feature.budgets;

import com.zincstate.manifest.core.database.dao.BudgetDao;
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
public final class BudgetsViewModel_Factory implements Factory<BudgetsViewModel> {
  private final Provider<BudgetDao> budgetDaoProvider;

  private final Provider<TransactionDao> transactionDaoProvider;

  public BudgetsViewModel_Factory(Provider<BudgetDao> budgetDaoProvider,
      Provider<TransactionDao> transactionDaoProvider) {
    this.budgetDaoProvider = budgetDaoProvider;
    this.transactionDaoProvider = transactionDaoProvider;
  }

  @Override
  public BudgetsViewModel get() {
    return newInstance(budgetDaoProvider.get(), transactionDaoProvider.get());
  }

  public static BudgetsViewModel_Factory create(Provider<BudgetDao> budgetDaoProvider,
      Provider<TransactionDao> transactionDaoProvider) {
    return new BudgetsViewModel_Factory(budgetDaoProvider, transactionDaoProvider);
  }

  public static BudgetsViewModel newInstance(BudgetDao budgetDao, TransactionDao transactionDao) {
    return new BudgetsViewModel(budgetDao, transactionDao);
  }
}
