package com.zincstate.manifest.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.zincstate.manifest.core.database.entity.AccountEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AccountDao_Impl implements AccountDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AccountEntity> __insertionAdapterOfAccountEntity;

  private final EntityDeletionOrUpdateAdapter<AccountEntity> __deletionAdapterOfAccountEntity;

  private final EntityDeletionOrUpdateAdapter<AccountEntity> __updateAdapterOfAccountEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public AccountDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAccountEntity = new EntityInsertionAdapter<AccountEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `accounts` (`id`,`name`,`group`,`balance`,`settlementDate`,`paymentDate`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AccountEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getGroup());
        statement.bindDouble(4, entity.getBalance());
        statement.bindLong(5, entity.getSettlementDate());
        statement.bindLong(6, entity.getPaymentDate());
      }
    };
    this.__deletionAdapterOfAccountEntity = new EntityDeletionOrUpdateAdapter<AccountEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `accounts` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AccountEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfAccountEntity = new EntityDeletionOrUpdateAdapter<AccountEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `accounts` SET `id` = ?,`name` = ?,`group` = ?,`balance` = ?,`settlementDate` = ?,`paymentDate` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AccountEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getGroup());
        statement.bindDouble(4, entity.getBalance());
        statement.bindLong(5, entity.getSettlementDate());
        statement.bindLong(6, entity.getPaymentDate());
        statement.bindString(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM accounts WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM accounts";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final AccountEntity account, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAccountEntity.insert(account);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final AccountEntity account, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAccountEntity.handle(account);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final AccountEntity account, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAccountEntity.handle(account);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AccountEntity>> getAllAccounts() {
    final String _sql = "SELECT * FROM accounts ORDER BY `group`, name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"accounts"}, new Callable<List<AccountEntity>>() {
      @Override
      @NonNull
      public List<AccountEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "group");
          final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
          final int _cursorIndexOfSettlementDate = CursorUtil.getColumnIndexOrThrow(_cursor, "settlementDate");
          final int _cursorIndexOfPaymentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDate");
          final List<AccountEntity> _result = new ArrayList<AccountEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AccountEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpGroup;
            _tmpGroup = _cursor.getString(_cursorIndexOfGroup);
            final double _tmpBalance;
            _tmpBalance = _cursor.getDouble(_cursorIndexOfBalance);
            final int _tmpSettlementDate;
            _tmpSettlementDate = _cursor.getInt(_cursorIndexOfSettlementDate);
            final int _tmpPaymentDate;
            _tmpPaymentDate = _cursor.getInt(_cursorIndexOfPaymentDate);
            _item = new AccountEntity(_tmpId,_tmpName,_tmpGroup,_tmpBalance,_tmpSettlementDate,_tmpPaymentDate);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAccountById(final String id,
      final Continuation<? super AccountEntity> $completion) {
    final String _sql = "SELECT * FROM accounts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AccountEntity>() {
      @Override
      @Nullable
      public AccountEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "group");
          final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
          final int _cursorIndexOfSettlementDate = CursorUtil.getColumnIndexOrThrow(_cursor, "settlementDate");
          final int _cursorIndexOfPaymentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDate");
          final AccountEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpGroup;
            _tmpGroup = _cursor.getString(_cursorIndexOfGroup);
            final double _tmpBalance;
            _tmpBalance = _cursor.getDouble(_cursorIndexOfBalance);
            final int _tmpSettlementDate;
            _tmpSettlementDate = _cursor.getInt(_cursorIndexOfSettlementDate);
            final int _tmpPaymentDate;
            _tmpPaymentDate = _cursor.getInt(_cursorIndexOfPaymentDate);
            _result = new AccountEntity(_tmpId,_tmpName,_tmpGroup,_tmpBalance,_tmpSettlementDate,_tmpPaymentDate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<AccountEntity> getAccountByIdFlow(final String id) {
    final String _sql = "SELECT * FROM accounts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"accounts"}, new Callable<AccountEntity>() {
      @Override
      @Nullable
      public AccountEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "group");
          final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
          final int _cursorIndexOfSettlementDate = CursorUtil.getColumnIndexOrThrow(_cursor, "settlementDate");
          final int _cursorIndexOfPaymentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDate");
          final AccountEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpGroup;
            _tmpGroup = _cursor.getString(_cursorIndexOfGroup);
            final double _tmpBalance;
            _tmpBalance = _cursor.getDouble(_cursorIndexOfBalance);
            final int _tmpSettlementDate;
            _tmpSettlementDate = _cursor.getInt(_cursorIndexOfSettlementDate);
            final int _tmpPaymentDate;
            _tmpPaymentDate = _cursor.getInt(_cursorIndexOfPaymentDate);
            _result = new AccountEntity(_tmpId,_tmpName,_tmpGroup,_tmpBalance,_tmpSettlementDate,_tmpPaymentDate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getAccountCount() {
    final String _sql = "SELECT COUNT(*) FROM accounts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"accounts"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAccountByName(final String name,
      final Continuation<? super AccountEntity> $completion) {
    final String _sql = "SELECT * FROM accounts WHERE name = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, name);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AccountEntity>() {
      @Override
      @Nullable
      public AccountEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "group");
          final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
          final int _cursorIndexOfSettlementDate = CursorUtil.getColumnIndexOrThrow(_cursor, "settlementDate");
          final int _cursorIndexOfPaymentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDate");
          final AccountEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpGroup;
            _tmpGroup = _cursor.getString(_cursorIndexOfGroup);
            final double _tmpBalance;
            _tmpBalance = _cursor.getDouble(_cursorIndexOfBalance);
            final int _tmpSettlementDate;
            _tmpSettlementDate = _cursor.getInt(_cursorIndexOfSettlementDate);
            final int _tmpPaymentDate;
            _tmpPaymentDate = _cursor.getInt(_cursorIndexOfPaymentDate);
            _result = new AccountEntity(_tmpId,_tmpName,_tmpGroup,_tmpBalance,_tmpSettlementDate,_tmpPaymentDate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
