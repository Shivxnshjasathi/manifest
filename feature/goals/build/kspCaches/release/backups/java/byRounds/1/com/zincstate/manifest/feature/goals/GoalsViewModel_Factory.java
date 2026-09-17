package com.zincstate.manifest.feature.goals;

import com.zincstate.manifest.core.database.dao.GoalDao;
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
public final class GoalsViewModel_Factory implements Factory<GoalsViewModel> {
  private final Provider<GoalDao> goalDaoProvider;

  public GoalsViewModel_Factory(Provider<GoalDao> goalDaoProvider) {
    this.goalDaoProvider = goalDaoProvider;
  }

  @Override
  public GoalsViewModel get() {
    return newInstance(goalDaoProvider.get());
  }

  public static GoalsViewModel_Factory create(Provider<GoalDao> goalDaoProvider) {
    return new GoalsViewModel_Factory(goalDaoProvider);
  }

  public static GoalsViewModel newInstance(GoalDao goalDao) {
    return new GoalsViewModel(goalDao);
  }
}
