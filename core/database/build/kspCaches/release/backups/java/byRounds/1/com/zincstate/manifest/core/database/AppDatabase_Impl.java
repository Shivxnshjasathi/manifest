package com.zincstate.manifest.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.zincstate.manifest.core.database.dao.AccountDao;
import com.zincstate.manifest.core.database.dao.AccountDao_Impl;
import com.zincstate.manifest.core.database.dao.BudgetDao;
import com.zincstate.manifest.core.database.dao.BudgetDao_Impl;
import com.zincstate.manifest.core.database.dao.CategoryDao;
import com.zincstate.manifest.core.database.dao.CategoryDao_Impl;
import com.zincstate.manifest.core.database.dao.GoalDao;
import com.zincstate.manifest.core.database.dao.GoalDao_Impl;
import com.zincstate.manifest.core.database.dao.MonthlySummaryDao;
import com.zincstate.manifest.core.database.dao.MonthlySummaryDao_Impl;
import com.zincstate.manifest.core.database.dao.RecurringTransactionDao;
import com.zincstate.manifest.core.database.dao.RecurringTransactionDao_Impl;
import com.zincstate.manifest.core.database.dao.TransactionDao;
import com.zincstate.manifest.core.database.dao.TransactionDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TransactionDao _transactionDao;

  private volatile AccountDao _accountDao;

  private volatile CategoryDao _categoryDao;

  private volatile BudgetDao _budgetDao;

  private volatile GoalDao _goalDao;

  private volatile RecurringTransactionDao _recurringTransactionDao;

  private volatile MonthlySummaryDao _monthlySummaryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `amount` REAL NOT NULL, `date` TEXT NOT NULL, `categoryId` TEXT NOT NULL, `accountId` TEXT NOT NULL, `toAccountId` TEXT, `note` TEXT NOT NULL, `description` TEXT NOT NULL, `attachmentPath` TEXT, `superCategory` TEXT, `isSettled` INTEGER NOT NULL, `recurringId` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_date` ON `transactions` (`date`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_accountId` ON `transactions` (`accountId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_categoryId` ON `transactions` (`categoryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_recurringId` ON `transactions` (`recurringId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `accounts` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `group` TEXT NOT NULL, `balance` REAL NOT NULL, `settlementDate` INTEGER NOT NULL, `paymentDate` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `icon` TEXT NOT NULL, `type` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `budgets` (`id` TEXT NOT NULL, `categoryId` TEXT NOT NULL, `amount` REAL NOT NULL, `yearMonth` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `goals` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `targetAmount` REAL NOT NULL, `currentAmount` REAL NOT NULL, `deadline` TEXT, `color` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recurring_transactions` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `amount` REAL NOT NULL, `categoryId` TEXT NOT NULL, `accountId` TEXT NOT NULL, `toAccountId` TEXT, `note` TEXT NOT NULL, `description` TEXT NOT NULL, `superCategory` TEXT, `frequency` TEXT NOT NULL, `startDate` TEXT NOT NULL, `nextRunDate` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `monthly_summaries` (`yearMonth` TEXT NOT NULL, `totalIncome` REAL NOT NULL, `totalExpense` REAL NOT NULL, `byCategory` TEXT NOT NULL, PRIMARY KEY(`yearMonth`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'efd6446b87b3f1af5d0d163bf2c083f7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `transactions`");
        db.execSQL("DROP TABLE IF EXISTS `accounts`");
        db.execSQL("DROP TABLE IF EXISTS `categories`");
        db.execSQL("DROP TABLE IF EXISTS `budgets`");
        db.execSQL("DROP TABLE IF EXISTS `goals`");
        db.execSQL("DROP TABLE IF EXISTS `recurring_transactions`");
        db.execSQL("DROP TABLE IF EXISTS `monthly_summaries`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTransactions = new HashMap<String, TableInfo.Column>(15);
        _columnsTransactions.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("categoryId", new TableInfo.Column("categoryId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("accountId", new TableInfo.Column("accountId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("toAccountId", new TableInfo.Column("toAccountId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("attachmentPath", new TableInfo.Column("attachmentPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("superCategory", new TableInfo.Column("superCategory", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("isSettled", new TableInfo.Column("isSettled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("recurringId", new TableInfo.Column("recurringId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransactions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTransactions = new HashSet<TableInfo.Index>(4);
        _indicesTransactions.add(new TableInfo.Index("index_transactions_date", false, Arrays.asList("date"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_accountId", false, Arrays.asList("accountId"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_categoryId", false, Arrays.asList("categoryId"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_recurringId", false, Arrays.asList("recurringId"), Arrays.asList("ASC")));
        final TableInfo _infoTransactions = new TableInfo("transactions", _columnsTransactions, _foreignKeysTransactions, _indicesTransactions);
        final TableInfo _existingTransactions = TableInfo.read(db, "transactions");
        if (!_infoTransactions.equals(_existingTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "transactions(com.zincstate.manifest.core.database.entity.TransactionEntity).\n"
                  + " Expected:\n" + _infoTransactions + "\n"
                  + " Found:\n" + _existingTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsAccounts = new HashMap<String, TableInfo.Column>(6);
        _columnsAccounts.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("group", new TableInfo.Column("group", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("balance", new TableInfo.Column("balance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("settlementDate", new TableInfo.Column("settlementDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("paymentDate", new TableInfo.Column("paymentDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccounts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccounts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccounts = new TableInfo("accounts", _columnsAccounts, _foreignKeysAccounts, _indicesAccounts);
        final TableInfo _existingAccounts = TableInfo.read(db, "accounts");
        if (!_infoAccounts.equals(_existingAccounts)) {
          return new RoomOpenHelper.ValidationResult(false, "accounts(com.zincstate.manifest.core.database.entity.AccountEntity).\n"
                  + " Expected:\n" + _infoAccounts + "\n"
                  + " Found:\n" + _existingAccounts);
        }
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(4);
        _columnsCategories.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("icon", new TableInfo.Column("icon", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.zincstate.manifest.core.database.entity.CategoryEntity).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsBudgets = new HashMap<String, TableInfo.Column>(4);
        _columnsBudgets.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("categoryId", new TableInfo.Column("categoryId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("yearMonth", new TableInfo.Column("yearMonth", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBudgets = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBudgets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBudgets = new TableInfo("budgets", _columnsBudgets, _foreignKeysBudgets, _indicesBudgets);
        final TableInfo _existingBudgets = TableInfo.read(db, "budgets");
        if (!_infoBudgets.equals(_existingBudgets)) {
          return new RoomOpenHelper.ValidationResult(false, "budgets(com.zincstate.manifest.core.database.entity.BudgetEntity).\n"
                  + " Expected:\n" + _infoBudgets + "\n"
                  + " Found:\n" + _existingBudgets);
        }
        final HashMap<String, TableInfo.Column> _columnsGoals = new HashMap<String, TableInfo.Column>(6);
        _columnsGoals.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("targetAmount", new TableInfo.Column("targetAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("currentAmount", new TableInfo.Column("currentAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("deadline", new TableInfo.Column("deadline", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGoals = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGoals = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGoals = new TableInfo("goals", _columnsGoals, _foreignKeysGoals, _indicesGoals);
        final TableInfo _existingGoals = TableInfo.read(db, "goals");
        if (!_infoGoals.equals(_existingGoals)) {
          return new RoomOpenHelper.ValidationResult(false, "goals(com.zincstate.manifest.core.database.entity.GoalEntity).\n"
                  + " Expected:\n" + _infoGoals + "\n"
                  + " Found:\n" + _existingGoals);
        }
        final HashMap<String, TableInfo.Column> _columnsRecurringTransactions = new HashMap<String, TableInfo.Column>(12);
        _columnsRecurringTransactions.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("categoryId", new TableInfo.Column("categoryId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("accountId", new TableInfo.Column("accountId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("toAccountId", new TableInfo.Column("toAccountId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("superCategory", new TableInfo.Column("superCategory", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("frequency", new TableInfo.Column("frequency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("startDate", new TableInfo.Column("startDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("nextRunDate", new TableInfo.Column("nextRunDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecurringTransactions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRecurringTransactions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRecurringTransactions = new TableInfo("recurring_transactions", _columnsRecurringTransactions, _foreignKeysRecurringTransactions, _indicesRecurringTransactions);
        final TableInfo _existingRecurringTransactions = TableInfo.read(db, "recurring_transactions");
        if (!_infoRecurringTransactions.equals(_existingRecurringTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "recurring_transactions(com.zincstate.manifest.core.database.entity.RecurringTransactionEntity).\n"
                  + " Expected:\n" + _infoRecurringTransactions + "\n"
                  + " Found:\n" + _existingRecurringTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsMonthlySummaries = new HashMap<String, TableInfo.Column>(4);
        _columnsMonthlySummaries.put("yearMonth", new TableInfo.Column("yearMonth", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMonthlySummaries.put("totalIncome", new TableInfo.Column("totalIncome", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMonthlySummaries.put("totalExpense", new TableInfo.Column("totalExpense", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMonthlySummaries.put("byCategory", new TableInfo.Column("byCategory", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMonthlySummaries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMonthlySummaries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMonthlySummaries = new TableInfo("monthly_summaries", _columnsMonthlySummaries, _foreignKeysMonthlySummaries, _indicesMonthlySummaries);
        final TableInfo _existingMonthlySummaries = TableInfo.read(db, "monthly_summaries");
        if (!_infoMonthlySummaries.equals(_existingMonthlySummaries)) {
          return new RoomOpenHelper.ValidationResult(false, "monthly_summaries(com.zincstate.manifest.core.database.entity.MonthlySummaryEntity).\n"
                  + " Expected:\n" + _infoMonthlySummaries + "\n"
                  + " Found:\n" + _existingMonthlySummaries);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "efd6446b87b3f1af5d0d163bf2c083f7", "afec314a9df5cfe2bc5bf908c1d970c9");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "transactions","accounts","categories","budgets","goals","recurring_transactions","monthly_summaries");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `transactions`");
      _db.execSQL("DELETE FROM `accounts`");
      _db.execSQL("DELETE FROM `categories`");
      _db.execSQL("DELETE FROM `budgets`");
      _db.execSQL("DELETE FROM `goals`");
      _db.execSQL("DELETE FROM `recurring_transactions`");
      _db.execSQL("DELETE FROM `monthly_summaries`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TransactionDao.class, TransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AccountDao.class, AccountDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BudgetDao.class, BudgetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GoalDao.class, GoalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecurringTransactionDao.class, RecurringTransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MonthlySummaryDao.class, MonthlySummaryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TransactionDao transactionDao() {
    if (_transactionDao != null) {
      return _transactionDao;
    } else {
      synchronized(this) {
        if(_transactionDao == null) {
          _transactionDao = new TransactionDao_Impl(this);
        }
        return _transactionDao;
      }
    }
  }

  @Override
  public AccountDao accountDao() {
    if (_accountDao != null) {
      return _accountDao;
    } else {
      synchronized(this) {
        if(_accountDao == null) {
          _accountDao = new AccountDao_Impl(this);
        }
        return _accountDao;
      }
    }
  }

  @Override
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }

  @Override
  public BudgetDao budgetDao() {
    if (_budgetDao != null) {
      return _budgetDao;
    } else {
      synchronized(this) {
        if(_budgetDao == null) {
          _budgetDao = new BudgetDao_Impl(this);
        }
        return _budgetDao;
      }
    }
  }

  @Override
  public GoalDao goalDao() {
    if (_goalDao != null) {
      return _goalDao;
    } else {
      synchronized(this) {
        if(_goalDao == null) {
          _goalDao = new GoalDao_Impl(this);
        }
        return _goalDao;
      }
    }
  }

  @Override
  public RecurringTransactionDao recurringTransactionDao() {
    if (_recurringTransactionDao != null) {
      return _recurringTransactionDao;
    } else {
      synchronized(this) {
        if(_recurringTransactionDao == null) {
          _recurringTransactionDao = new RecurringTransactionDao_Impl(this);
        }
        return _recurringTransactionDao;
      }
    }
  }

  @Override
  public MonthlySummaryDao monthlySummaryDao() {
    if (_monthlySummaryDao != null) {
      return _monthlySummaryDao;
    } else {
      synchronized(this) {
        if(_monthlySummaryDao == null) {
          _monthlySummaryDao = new MonthlySummaryDao_Impl(this);
        }
        return _monthlySummaryDao;
      }
    }
  }
}
