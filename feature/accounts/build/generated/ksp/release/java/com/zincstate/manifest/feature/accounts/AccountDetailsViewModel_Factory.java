package com.zincstate.manifest.feature.accounts;

import androidx.lifecycle.SavedStateHandle;
import com.zincstate.manifest.core.database.dao.AccountDao;
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
public final class AccountDetailsViewModel_Factory implements Factory<AccountDetailsViewModel> {
  private final Provider<AccountDao> accountDaoProvider;

  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public AccountDetailsViewModel_Factory(Provider<AccountDao> accountDaoProvider,
      Provider<TransactionDao> transactionDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.accountDaoProvider = accountDaoProvider;
    this.transactionDaoProvider = transactionDaoProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public AccountDetailsViewModel get() {
    return newInstance(accountDaoProvider.get(), transactionDaoProvider.get(), savedStateHandleProvider.get());
  }

  public static AccountDetailsViewModel_Factory create(Provider<AccountDao> accountDaoProvider,
      Provider<TransactionDao> transactionDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AccountDetailsViewModel_Factory(accountDaoProvider, transactionDaoProvider, savedStateHandleProvider);
  }

  public static AccountDetailsViewModel newInstance(AccountDao accountDao,
      TransactionDao transactionDao, SavedStateHandle savedStateHandle) {
    return new AccountDetailsViewModel(accountDao, transactionDao, savedStateHandle);
  }
}
