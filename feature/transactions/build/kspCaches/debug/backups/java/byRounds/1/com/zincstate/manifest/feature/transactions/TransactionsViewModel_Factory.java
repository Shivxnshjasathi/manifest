package com.zincstate.manifest.feature.transactions;

import com.zincstate.manifest.core.database.dao.AccountDao;
import com.zincstate.manifest.core.database.dao.CategoryDao;
import com.zincstate.manifest.core.database.dao.TransactionDao;
import com.zincstate.manifest.core.database.dao.TransactionSplitDao;
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
public final class TransactionsViewModel_Factory implements Factory<TransactionsViewModel> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<AccountDao> accountDaoProvider;

  private final Provider<TransactionSplitDao> splitDaoProvider;

  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  public TransactionsViewModel_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<AccountDao> accountDaoProvider,
      Provider<TransactionSplitDao> splitDaoProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
    this.accountDaoProvider = accountDaoProvider;
    this.splitDaoProvider = splitDaoProvider;
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
  }

  @Override
  public TransactionsViewModel get() {
    return newInstance(transactionDaoProvider.get(), categoryDaoProvider.get(), accountDaoProvider.get(), splitDaoProvider.get(), preferencesDataStoreProvider.get());
  }

  public static TransactionsViewModel_Factory create(
      Provider<TransactionDao> transactionDaoProvider, Provider<CategoryDao> categoryDaoProvider,
      Provider<AccountDao> accountDaoProvider, Provider<TransactionSplitDao> splitDaoProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    return new TransactionsViewModel_Factory(transactionDaoProvider, categoryDaoProvider, accountDaoProvider, splitDaoProvider, preferencesDataStoreProvider);
  }

  public static TransactionsViewModel newInstance(TransactionDao transactionDao,
      CategoryDao categoryDao, AccountDao accountDao, TransactionSplitDao splitDao,
      UserPreferencesDataStore preferencesDataStore) {
    return new TransactionsViewModel(transactionDao, categoryDao, accountDao, splitDao, preferencesDataStore);
  }
}
