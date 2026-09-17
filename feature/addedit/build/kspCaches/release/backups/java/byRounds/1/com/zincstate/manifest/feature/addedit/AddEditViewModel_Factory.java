package com.zincstate.manifest.feature.addedit;

import androidx.lifecycle.SavedStateHandle;
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
public final class AddEditViewModel_Factory implements Factory<AddEditViewModel> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<AccountDao> accountDaoProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public AddEditViewModel_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<AccountDao> accountDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
    this.accountDaoProvider = accountDaoProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public AddEditViewModel get() {
    return newInstance(transactionDaoProvider.get(), categoryDaoProvider.get(), accountDaoProvider.get(), savedStateHandleProvider.get());
  }

  public static AddEditViewModel_Factory create(Provider<TransactionDao> transactionDaoProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<AccountDao> accountDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AddEditViewModel_Factory(transactionDaoProvider, categoryDaoProvider, accountDaoProvider, savedStateHandleProvider);
  }

  public static AddEditViewModel newInstance(TransactionDao transactionDao, CategoryDao categoryDao,
      AccountDao accountDao, SavedStateHandle savedStateHandle) {
    return new AddEditViewModel(transactionDao, categoryDao, accountDao, savedStateHandle);
  }
}
