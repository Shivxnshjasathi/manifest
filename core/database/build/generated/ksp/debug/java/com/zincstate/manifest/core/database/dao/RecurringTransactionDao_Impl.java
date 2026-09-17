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
import com.zincstate.manifest.core.database.entity.RecurringTransactionEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class RecurringTransactionDao_Impl implements RecurringTransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecurringTransactionEntity> __insertionAdapterOfRecurringTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<RecurringTransactionEntity> __deletionAdapterOfRecurringTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<RecurringTransactionEntity> __updateAdapterOfRecurringTransactionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RecurringTransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecurringTransactionEntity = new EntityInsertionAdapter<RecurringTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `recurring_transactions` (`id`,`type`,`amount`,`categoryId`,`accountId`,`toAccountId`,`note`,`description`,`superCategory`,`frequency`,`startDate`,`nextRunDate`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurringTransactionEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getType());
        statement.bindDouble(3, entity.getAmount());
        statement.bindString(4, entity.getCategoryId());
        statement.bindString(5, entity.getAccountId());
        if (entity.getToAccountId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getToAccountId());
        }
        statement.bindString(7, entity.getNote());
        statement.bindString(8, entity.getDescription());
        if (entity.getSuperCategory() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSuperCategory());
        }
        statement.bindString(10, entity.getFrequency());
        statement.bindString(11, entity.getStartDate());
        statement.bindString(12, entity.getNextRunDate());
      }
    };
    this.__deletionAdapterOfRecurringTransactionEntity = new EntityDeletionOrUpdateAdapter<RecurringTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `recurring_transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurringTransactionEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfRecurringTransactionEntity = new EntityDeletionOrUpdateAdapter<RecurringTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `recurring_transactions` SET `id` = ?,`type` = ?,`amount` = ?,`categoryId` = ?,`accountId` = ?,`toAccountId` = ?,`note` = ?,`description` = ?,`superCategory` = ?,`frequency` = ?,`startDate` = ?,`nextRunDate` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurringTransactionEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getType());
        statement.bindDouble(3, entity.getAmount());
        statement.bindString(4, entity.getCategoryId());
        statement.bindString(5, entity.getAccountId());
        if (entity.getToAccountId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getToAccountId());
        }
        statement.bindString(7, entity.getNote());
        statement.bindString(8, entity.getDescription());
        if (entity.getSuperCategory() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSuperCategory());
        }
        statement.bindString(10, entity.getFrequency());
        statement.bindString(11, entity.getStartDate());
        statement.bindString(12, entity.getNextRunDate());
        statement.bindString(13, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM recurring_transactions WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM recurring_transactions";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final RecurringTransactionEntity recurring,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRecurringTransactionEntity.insert(recurring);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RecurringTransactionEntity recurring,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRecurringTransactionEntity.handle(recurring);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final RecurringTransactionEntity recurring,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRecurringTransactionEntity.handle(recurring);
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
  public Flow<List<RecurringTransactionEntity>> getAllRecurring() {
    final String _sql = "SELECT * FROM recurring_transactions ORDER BY nextRunDate";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recurring_transactions"}, new Callable<List<RecurringTransactionEntity>>() {
      @Override
      @NonNull
      public List<RecurringTransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSuperCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "superCategory");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfNextRunDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRunDate");
          final List<RecurringTransactionEntity> _result = new ArrayList<RecurringTransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecurringTransactionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpCategoryId;
            _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            final String _tmpAccountId;
            _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            final String _tmpToAccountId;
            if (_cursor.isNull(_cursorIndexOfToAccountId)) {
              _tmpToAccountId = null;
            } else {
              _tmpToAccountId = _cursor.getString(_cursorIndexOfToAccountId);
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpSuperCategory;
            if (_cursor.isNull(_cursorIndexOfSuperCategory)) {
              _tmpSuperCategory = null;
            } else {
              _tmpSuperCategory = _cursor.getString(_cursorIndexOfSuperCategory);
            }
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpNextRunDate;
            _tmpNextRunDate = _cursor.getString(_cursorIndexOfNextRunDate);
            _item = new RecurringTransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpNote,_tmpDescription,_tmpSuperCategory,_tmpFrequency,_tmpStartDate,_tmpNextRunDate);
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
  public Object getDueRecurring(final String today,
      final Continuation<? super List<RecurringTransactionEntity>> $completion) {
    final String _sql = "SELECT * FROM recurring_transactions WHERE nextRunDate <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, today);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RecurringTransactionEntity>>() {
      @Override
      @NonNull
      public List<RecurringTransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSuperCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "superCategory");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfNextRunDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRunDate");
          final List<RecurringTransactionEntity> _result = new ArrayList<RecurringTransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecurringTransactionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpCategoryId;
            _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            final String _tmpAccountId;
            _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            final String _tmpToAccountId;
            if (_cursor.isNull(_cursorIndexOfToAccountId)) {
              _tmpToAccountId = null;
            } else {
              _tmpToAccountId = _cursor.getString(_cursorIndexOfToAccountId);
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpSuperCategory;
            if (_cursor.isNull(_cursorIndexOfSuperCategory)) {
              _tmpSuperCategory = null;
            } else {
              _tmpSuperCategory = _cursor.getString(_cursorIndexOfSuperCategory);
            }
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpNextRunDate;
            _tmpNextRunDate = _cursor.getString(_cursorIndexOfNextRunDate);
            _item = new RecurringTransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpNote,_tmpDescription,_tmpSuperCategory,_tmpFrequency,_tmpStartDate,_tmpNextRunDate);
            _result.add(_item);
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
  public Object getRecurringById(final String id,
      final Continuation<? super RecurringTransactionEntity> $completion) {
    final String _sql = "SELECT * FROM recurring_transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RecurringTransactionEntity>() {
      @Override
      @Nullable
      public RecurringTransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSuperCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "superCategory");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfNextRunDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRunDate");
          final RecurringTransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpCategoryId;
            _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            final String _tmpAccountId;
            _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            final String _tmpToAccountId;
            if (_cursor.isNull(_cursorIndexOfToAccountId)) {
              _tmpToAccountId = null;
            } else {
              _tmpToAccountId = _cursor.getString(_cursorIndexOfToAccountId);
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpSuperCategory;
            if (_cursor.isNull(_cursorIndexOfSuperCategory)) {
              _tmpSuperCategory = null;
            } else {
              _tmpSuperCategory = _cursor.getString(_cursorIndexOfSuperCategory);
            }
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpNextRunDate;
            _tmpNextRunDate = _cursor.getString(_cursorIndexOfNextRunDate);
            _result = new RecurringTransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpNote,_tmpDescription,_tmpSuperCategory,_tmpFrequency,_tmpStartDate,_tmpNextRunDate);
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
