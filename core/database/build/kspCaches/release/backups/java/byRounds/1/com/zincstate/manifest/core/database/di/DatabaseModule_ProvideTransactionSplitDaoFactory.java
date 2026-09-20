package com.zincstate.manifest.core.database.di;

import com.zincstate.manifest.core.database.AppDatabase;
import com.zincstate.manifest.core.database.dao.TransactionSplitDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideTransactionSplitDaoFactory implements Factory<TransactionSplitDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideTransactionSplitDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TransactionSplitDao get() {
    return provideTransactionSplitDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTransactionSplitDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTransactionSplitDaoFactory(databaseProvider);
  }

  public static TransactionSplitDao provideTransactionSplitDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTransactionSplitDao(database));
  }
}
