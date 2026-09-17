package com.zincstate.manifest.core.datastore.di;

import android.content.Context;
import com.zincstate.manifest.core.datastore.UserPreferencesDataStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DataStoreModule_ProvideUserPreferencesDataStoreFactory implements Factory<UserPreferencesDataStore> {
  private final Provider<Context> contextProvider;

  public DataStoreModule_ProvideUserPreferencesDataStoreFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public UserPreferencesDataStore get() {
    return provideUserPreferencesDataStore(contextProvider.get());
  }

  public static DataStoreModule_ProvideUserPreferencesDataStoreFactory create(
      Provider<Context> contextProvider) {
    return new DataStoreModule_ProvideUserPreferencesDataStoreFactory(contextProvider);
  }

  public static UserPreferencesDataStore provideUserPreferencesDataStore(Context context) {
    return Preconditions.checkNotNullFromProvides(DataStoreModule.INSTANCE.provideUserPreferencesDataStore(context));
  }
}
