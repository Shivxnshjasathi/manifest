package com.zincstate.manifest.feature.sms;

import com.zincstate.manifest.core.database.dao.AccountDao;
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
public final class SmsImportViewModel_Factory implements Factory<SmsImportViewModel> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<AccountDao> accountDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  public SmsImportViewModel_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<AccountDao> accountDaoProvider, Provider<CategoryDao> categoryDaoProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.accountDaoProvider = accountDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
  }

  @Override
  public SmsImportViewModel get() {
    return newInstance(transactionDaoProvider.get(), accountDaoProvider.get(), categoryDaoProvider.get());
  }

  public static SmsImportViewModel_Factory create(Provider<TransactionDao> transactionDaoProvider,
      Provider<AccountDao> accountDaoProvider, Provider<CategoryDao> categoryDaoProvider) {
    return new SmsImportViewModel_Factory(transactionDaoProvider, accountDaoProvider, categoryDaoProvider);
  }

  public static SmsImportViewModel newInstance(TransactionDao transactionDao, AccountDao accountDao,
      CategoryDao categoryDao) {
    return new SmsImportViewModel(transactionDao, accountDao, categoryDao);
  }
}
