package com.zincstate.manifest.core.database.di;

import com.zincstate.manifest.core.database.AppDatabase;
import com.zincstate.manifest.core.database.dao.MonthlySummaryDao;
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
public final class DatabaseModule_ProvideMonthlySummaryDaoFactory implements Factory<MonthlySummaryDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideMonthlySummaryDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MonthlySummaryDao get() {
    return provideMonthlySummaryDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMonthlySummaryDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMonthlySummaryDaoFactory(databaseProvider);
  }

  public static MonthlySummaryDao provideMonthlySummaryDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMonthlySummaryDao(database));
  }
}
