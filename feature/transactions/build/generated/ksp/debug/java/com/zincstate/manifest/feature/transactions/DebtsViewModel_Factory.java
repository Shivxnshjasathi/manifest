package com.zincstate.manifest.feature.transactions;

import com.zincstate.manifest.core.database.dao.ContactDao;
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
public final class DebtsViewModel_Factory implements Factory<DebtsViewModel> {
  private final Provider<ContactDao> contactDaoProvider;

  private final Provider<TransactionSplitDao> splitDaoProvider;

  public DebtsViewModel_Factory(Provider<ContactDao> contactDaoProvider,
      Provider<TransactionSplitDao> splitDaoProvider) {
    this.contactDaoProvider = contactDaoProvider;
    this.splitDaoProvider = splitDaoProvider;
  }

  @Override
  public DebtsViewModel get() {
    return newInstance(contactDaoProvider.get(), splitDaoProvider.get());
  }

  public static DebtsViewModel_Factory create(Provider<ContactDao> contactDaoProvider,
      Provider<TransactionSplitDao> splitDaoProvider) {
    return new DebtsViewModel_Factory(contactDaoProvider, splitDaoProvider);
  }

  public static DebtsViewModel newInstance(ContactDao contactDao, TransactionSplitDao splitDao) {
    return new DebtsViewModel(contactDao, splitDao);
  }
}
