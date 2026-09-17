package com.zincstate.manifest.core.database.di;

import com.zincstate.manifest.core.database.AppDatabase;
import com.zincstate.manifest.core.database.dao.AccountDao;
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
public final class DatabaseModule_ProvideAccountDaoFactory implements Factory<AccountDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideAccountDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AccountDao get() {
    return provideAccountDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAccountDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAccountDaoFactory(databaseProvider);
  }

  public static AccountDao provideAccountDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAccountDao(database));
  }
}
