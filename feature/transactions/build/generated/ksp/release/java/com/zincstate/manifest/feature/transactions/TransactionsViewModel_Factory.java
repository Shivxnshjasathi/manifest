package com.zincstate.manifest.feature.transactions;

import com.zincstate.manifest.core.database.dao.AccountDao;
import com.zincstate.manifest.core.database.dao.CategoryDao;
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
public final class TransactionsViewModel_Factory implements Factory<TransactionsViewModel> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<AccountDao> accountDaoProvider;

  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  public TransactionsViewModel_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<AccountDao> accountDaoProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
    this.accountDaoProvider = accountDaoProvider;
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
  }

  @Override
  public TransactionsViewModel get() {
    return newInstance(transactionDaoProvider.get(), categoryDaoProvider.get(), accountDaoProvider.get(), preferencesDataStoreProvider.get());
  }

  public static TransactionsViewModel_Factory create(
      Provider<TransactionDao> transactionDaoProvider, Provider<CategoryDao> categoryDaoProvider,
      Provider<AccountDao> accountDaoProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    return new TransactionsViewModel_Factory(transactionDaoProvider, categoryDaoProvider, accountDaoProvider, preferencesDataStoreProvider);
  }

  public static TransactionsViewModel newInstance(TransactionDao transactionDao,
      CategoryDao categoryDao, AccountDao accountDao,
      UserPreferencesDataStore preferencesDataStore) {
    return new TransactionsViewModel(transactionDao, categoryDao, accountDao, preferencesDataStore);
  }
}
