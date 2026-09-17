package com.zincstate.manifest.feature.settings;

import com.zincstate.manifest.core.database.dao.AccountDao;
import com.zincstate.manifest.core.database.dao.BudgetDao;
import com.zincstate.manifest.core.database.dao.CategoryDao;
import com.zincstate.manifest.core.database.dao.GoalDao;
import com.zincstate.manifest.core.database.dao.MonthlySummaryDao;
import com.zincstate.manifest.core.database.dao.RecurringTransactionDao;
import com.zincstate.manifest.core.database.dao.TransactionDao;
import com.zincstate.manifest.core.datastore.UserPreferencesDataStore;
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
public final class MoreViewModel_Factory implements Factory<MoreViewModel> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<AccountDao> accountDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<BudgetDao> budgetDaoProvider;

  private final Provider<GoalDao> goalDaoProvider;

  private final Provider<RecurringTransactionDao> recurringDaoProvider;

  private final Provider<MonthlySummaryDao> monthlySummaryDaoProvider;

  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  public MoreViewModel_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<AccountDao> accountDaoProvider, Provider<CategoryDao> categoryDaoProvider,
      Provider<BudgetDao> budgetDaoProvider, Provider<GoalDao> goalDaoProvider,
      Provider<RecurringTransactionDao> recurringDaoProvider,
      Provider<MonthlySummaryDao> monthlySummaryDaoProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.accountDaoProvider = accountDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
    this.budgetDaoProvider = budgetDaoProvider;
    this.goalDaoProvider = goalDaoProvider;
    this.recurringDaoProvider = recurringDaoProvider;
    this.monthlySummaryDaoProvider = monthlySummaryDaoProvider;
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
  }

  @Override
  public MoreViewModel get() {
    return newInstance(transactionDaoProvider.get(), accountDaoProvider.get(), categoryDaoProvider.get(), budgetDaoProvider.get(), goalDaoProvider.get(), recurringDaoProvider.get(), monthlySummaryDaoProvider.get(), preferencesDataStoreProvider.get());
  }

  public static MoreViewModel_Factory create(Provider<TransactionDao> transactionDaoProvider,
      Provider<AccountDao> accountDaoProvider, Provider<CategoryDao> categoryDaoProvider,
      Provider<BudgetDao> budgetDaoProvider, Provider<GoalDao> goalDaoProvider,
      Provider<RecurringTransactionDao> recurringDaoProvider,
      Provider<MonthlySummaryDao> monthlySummaryDaoProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    return new MoreViewModel_Factory(transactionDaoProvider, accountDaoProvider, categoryDaoProvider, budgetDaoProvider, goalDaoProvider, recurringDaoProvider, monthlySummaryDaoProvider, preferencesDataStoreProvider);
  }

  public static MoreViewModel newInstance(TransactionDao transactionDao, AccountDao accountDao,
      CategoryDao categoryDao, BudgetDao budgetDao, GoalDao goalDao,
      RecurringTransactionDao recurringDao, MonthlySummaryDao monthlySummaryDao,
      UserPreferencesDataStore preferencesDataStore) {
    return new MoreViewModel(transactionDao, accountDao, categoryDao, budgetDao, goalDao, recurringDao, monthlySummaryDao, preferencesDataStore);
  }
}
