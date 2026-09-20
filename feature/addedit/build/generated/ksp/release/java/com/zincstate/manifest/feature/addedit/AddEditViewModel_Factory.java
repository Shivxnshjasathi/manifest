package com.zincstate.manifest.feature.addedit;

import androidx.lifecycle.SavedStateHandle;
import com.zincstate.manifest.core.database.dao.AccountDao;
import com.zincstate.manifest.core.database.dao.CategoryDao;
import com.zincstate.manifest.core.database.dao.ContactDao;
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
public final class AddEditViewModel_Factory implements Factory<AddEditViewModel> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<AccountDao> accountDaoProvider;

  private final Provider<ContactDao> contactDaoProvider;

  private final Provider<TransactionSplitDao> splitDaoProvider;

  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public AddEditViewModel_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<AccountDao> accountDaoProvider,
      Provider<ContactDao> contactDaoProvider, Provider<TransactionSplitDao> splitDaoProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
    this.accountDaoProvider = accountDaoProvider;
    this.contactDaoProvider = contactDaoProvider;
    this.splitDaoProvider = splitDaoProvider;
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public AddEditViewModel get() {
    return newInstance(transactionDaoProvider.get(), categoryDaoProvider.get(), accountDaoProvider.get(), contactDaoProvider.get(), splitDaoProvider.get(), preferencesDataStoreProvider.get(), savedStateHandleProvider.get());
  }

  public static AddEditViewModel_Factory create(Provider<TransactionDao> transactionDaoProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<AccountDao> accountDaoProvider,
      Provider<ContactDao> contactDaoProvider, Provider<TransactionSplitDao> splitDaoProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AddEditViewModel_Factory(transactionDaoProvider, categoryDaoProvider, accountDaoProvider, contactDaoProvider, splitDaoProvider, preferencesDataStoreProvider, savedStateHandleProvider);
  }

  public static AddEditViewModel newInstance(TransactionDao transactionDao, CategoryDao categoryDao,
      AccountDao accountDao, ContactDao contactDao, TransactionSplitDao splitDao,
      UserPreferencesDataStore preferencesDataStore, SavedStateHandle savedStateHandle) {
    return new AddEditViewModel(transactionDao, categoryDao, accountDao, contactDao, splitDao, preferencesDataStore, savedStateHandle);
  }
}
