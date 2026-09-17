package com.zincstate.manifest.feature.accounts;

import com.zincstate.manifest.core.database.dao.AccountDao;
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
public final class AccountsViewModel_Factory implements Factory<AccountsViewModel> {
  private final Provider<AccountDao> accountDaoProvider;

  public AccountsViewModel_Factory(Provider<AccountDao> accountDaoProvider) {
    this.accountDaoProvider = accountDaoProvider;
  }

  @Override
  public AccountsViewModel get() {
    return newInstance(accountDaoProvider.get());
  }

  public static AccountsViewModel_Factory create(Provider<AccountDao> accountDaoProvider) {
    return new AccountsViewModel_Factory(accountDaoProvider);
  }

  public static AccountsViewModel newInstance(AccountDao accountDao) {
    return new AccountsViewModel(accountDao);
  }
}
