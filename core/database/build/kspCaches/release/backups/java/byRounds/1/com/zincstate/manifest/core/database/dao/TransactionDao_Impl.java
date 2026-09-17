package com.zincstate.manifest.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.paging.LimitOffsetPagingSource;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.zincstate.manifest.core.database.entity.TransactionEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class TransactionDao_Impl implements TransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TransactionEntity> __insertionAdapterOfTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<TransactionEntity> __deletionAdapterOfTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<TransactionEntity> __updateAdapterOfTransactionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public TransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransactionEntity = new EntityInsertionAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `transactions` (`id`,`type`,`amount`,`date`,`categoryId`,`accountId`,`toAccountId`,`note`,`description`,`attachmentPath`,`superCategory`,`isSettled`,`recurringId`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getType());
        statement.bindDouble(3, entity.getAmount());
        statement.bindString(4, entity.getDate());
        statement.bindString(5, entity.getCategoryId());
        statement.bindString(6, entity.getAccountId());
        if (entity.getToAccountId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getToAccountId());
        }
        statement.bindString(8, entity.getNote());
        statement.bindString(9, entity.getDescription());
        if (entity.getAttachmentPath() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getAttachmentPath());
        }
        if (entity.getSuperCategory() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getSuperCategory());
        }
        final int _tmp = entity.isSettled() ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.getRecurringId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getRecurringId());
        }
        statement.bindLong(14, entity.getCreatedAt());
        statement.bindLong(15, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfTransactionEntity = new EntityDeletionOrUpdateAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfTransactionEntity = new EntityDeletionOrUpdateAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `transactions` SET `id` = ?,`type` = ?,`amount` = ?,`date` = ?,`categoryId` = ?,`accountId` = ?,`toAccountId` = ?,`note` = ?,`description` = ?,`attachmentPath` = ?,`superCategory` = ?,`isSettled` = ?,`recurringId` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getType());
        statement.bindDouble(3, entity.getAmount());
        statement.bindString(4, entity.getDate());
        statement.bindString(5, entity.getCategoryId());
        statement.bindString(6, entity.getAccountId());
        if (entity.getToAccountId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getToAccountId());
        }
        statement.bindString(8, entity.getNote());
        statement.bindString(9, entity.getDescription());
        if (entity.getAttachmentPath() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getAttachmentPath());
        }
        if (entity.getSuperCategory() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getSuperCategory());
        }
        final int _tmp = entity.isSettled() ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.getRecurringId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getRecurringId());
        }
        statement.bindLong(14, entity.getCreatedAt());
        statement.bindLong(15, entity.getUpdatedAt());
        statement.bindString(16, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transactions";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final TransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTransactionEntity.insert(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<TransactionEntity> transactions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTransactionEntity.insert(transactions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final TransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTransactionEntity.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final TransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTransactionEntity.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
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
  public PagingSource<Integer, TransactionEntity> getTransactionsByMonthPaged(
      final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        WHERE date LIKE ? || '%'\n"
            + "        ORDER BY date DESC, createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    return new LimitOffsetPagingSource<TransactionEntity>(_statement, __db, "transactions") {
      @Override
      @NonNull
      protected List<TransactionEntity> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
        final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(cursor, "type");
        final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(cursor, "amount");
        final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(cursor, "date");
        final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(cursor, "categoryId");
        final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(cursor, "accountId");
        final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(cursor, "toAccountId");
        final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(cursor, "note");
        final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(cursor, "description");
        final int _cursorIndexOfAttachmentPath = CursorUtil.getColumnIndexOrThrow(cursor, "attachmentPath");
        final int _cursorIndexOfSuperCategory = CursorUtil.getColumnIndexOrThrow(cursor, "superCategory");
        final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(cursor, "isSettled");
        final int _cursorIndexOfRecurringId = CursorUtil.getColumnIndexOrThrow(cursor, "recurringId");
        final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "createdAt");
        final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "updatedAt");
        final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(cursor.getCount());
        while (cursor.moveToNext()) {
          final TransactionEntity _item;
          final String _tmpId;
          _tmpId = cursor.getString(_cursorIndexOfId);
          final String _tmpType;
          _tmpType = cursor.getString(_cursorIndexOfType);
          final double _tmpAmount;
          _tmpAmount = cursor.getDouble(_cursorIndexOfAmount);
          final String _tmpDate;
          _tmpDate = cursor.getString(_cursorIndexOfDate);
          final String _tmpCategoryId;
          _tmpCategoryId = cursor.getString(_cursorIndexOfCategoryId);
          final String _tmpAccountId;
          _tmpAccountId = cursor.getString(_cursorIndexOfAccountId);
          final String _tmpToAccountId;
          if (cursor.isNull(_cursorIndexOfToAccountId)) {
            _tmpToAccountId = null;
          } else {
            _tmpToAccountId = cursor.getString(_cursorIndexOfToAccountId);
          }
          final String _tmpNote;
          _tmpNote = cursor.getString(_cursorIndexOfNote);
          final String _tmpDescription;
          _tmpDescription = cursor.getString(_cursorIndexOfDescription);
          final String _tmpAttachmentPath;
          if (cursor.isNull(_cursorIndexOfAttachmentPath)) {
            _tmpAttachmentPath = null;
          } else {
            _tmpAttachmentPath = cursor.getString(_cursorIndexOfAttachmentPath);
          }
          final String _tmpSuperCategory;
          if (cursor.isNull(_cursorIndexOfSuperCategory)) {
            _tmpSuperCategory = null;
          } else {
            _tmpSuperCategory = cursor.getString(_cursorIndexOfSuperCategory);
          }
          final boolean _tmpIsSettled;
          final int _tmp;
          _tmp = cursor.getInt(_cursorIndexOfIsSettled);
          _tmpIsSettled = _tmp != 0;
          final String _tmpRecurringId;
          if (cursor.isNull(_cursorIndexOfRecurringId)) {
            _tmpRecurringId = null;
          } else {
            _tmpRecurringId = cursor.getString(_cursorIndexOfRecurringId);
          }
          final long _tmpCreatedAt;
          _tmpCreatedAt = cursor.getLong(_cursorIndexOfCreatedAt);
          final long _tmpUpdatedAt;
          _tmpUpdatedAt = cursor.getLong(_cursorIndexOfUpdatedAt);
          _item = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpDate,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpNote,_tmpDescription,_tmpAttachmentPath,_tmpSuperCategory,_tmpIsSettled,_tmpRecurringId,_tmpCreatedAt,_tmpUpdatedAt);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  @Override
  public Flow<List<TransactionEntity>> getTransactionsByMonth(final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        WHERE date LIKE ? || '%'\n"
            + "        ORDER BY date DESC, createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfAttachmentPath = CursorUtil.getColumnIndexOrThrow(_cursor, "attachmentPath");
          final int _cursorIndexOfSuperCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "superCategory");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSettled");
          final int _cursorIndexOfRecurringId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
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
            final String _tmpAttachmentPath;
            if (_cursor.isNull(_cursorIndexOfAttachmentPath)) {
              _tmpAttachmentPath = null;
            } else {
              _tmpAttachmentPath = _cursor.getString(_cursorIndexOfAttachmentPath);
            }
            final String _tmpSuperCategory;
            if (_cursor.isNull(_cursorIndexOfSuperCategory)) {
              _tmpSuperCategory = null;
            } else {
              _tmpSuperCategory = _cursor.getString(_cursorIndexOfSuperCategory);
            }
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            final String _tmpRecurringId;
            if (_cursor.isNull(_cursorIndexOfRecurringId)) {
              _tmpRecurringId = null;
            } else {
              _tmpRecurringId = _cursor.getString(_cursorIndexOfRecurringId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpDate,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpNote,_tmpDescription,_tmpAttachmentPath,_tmpSuperCategory,_tmpIsSettled,_tmpRecurringId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<TransactionEntity>> getTransactionsByAccount(final String accountId) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        WHERE accountId = ?\n"
            + "        ORDER BY date DESC, createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, accountId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfAttachmentPath = CursorUtil.getColumnIndexOrThrow(_cursor, "attachmentPath");
          final int _cursorIndexOfSuperCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "superCategory");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSettled");
          final int _cursorIndexOfRecurringId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
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
            final String _tmpAttachmentPath;
            if (_cursor.isNull(_cursorIndexOfAttachmentPath)) {
              _tmpAttachmentPath = null;
            } else {
              _tmpAttachmentPath = _cursor.getString(_cursorIndexOfAttachmentPath);
            }
            final String _tmpSuperCategory;
            if (_cursor.isNull(_cursorIndexOfSuperCategory)) {
              _tmpSuperCategory = null;
            } else {
              _tmpSuperCategory = _cursor.getString(_cursorIndexOfSuperCategory);
            }
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            final String _tmpRecurringId;
            if (_cursor.isNull(_cursorIndexOfRecurringId)) {
              _tmpRecurringId = null;
            } else {
              _tmpRecurringId = _cursor.getString(_cursorIndexOfRecurringId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpDate,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpNote,_tmpDescription,_tmpAttachmentPath,_tmpSuperCategory,_tmpIsSettled,_tmpRecurringId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getTransactionById(final String id,
      final Continuation<? super TransactionEntity> $completion) {
    final String _sql = "SELECT * FROM transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TransactionEntity>() {
      @Override
      @Nullable
      public TransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfAttachmentPath = CursorUtil.getColumnIndexOrThrow(_cursor, "attachmentPath");
          final int _cursorIndexOfSuperCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "superCategory");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSettled");
          final int _cursorIndexOfRecurringId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final TransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
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
            final String _tmpAttachmentPath;
            if (_cursor.isNull(_cursorIndexOfAttachmentPath)) {
              _tmpAttachmentPath = null;
            } else {
              _tmpAttachmentPath = _cursor.getString(_cursorIndexOfAttachmentPath);
            }
            final String _tmpSuperCategory;
            if (_cursor.isNull(_cursorIndexOfSuperCategory)) {
              _tmpSuperCategory = null;
            } else {
              _tmpSuperCategory = _cursor.getString(_cursorIndexOfSuperCategory);
            }
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            final String _tmpRecurringId;
            if (_cursor.isNull(_cursorIndexOfRecurringId)) {
              _tmpRecurringId = null;
            } else {
              _tmpRecurringId = _cursor.getString(_cursorIndexOfRecurringId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpDate,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpNote,_tmpDescription,_tmpAttachmentPath,_tmpSuperCategory,_tmpIsSettled,_tmpRecurringId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<TransactionEntity> getTransactionByIdFlow(final String id) {
    final String _sql = "SELECT * FROM transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<TransactionEntity>() {
      @Override
      @Nullable
      public TransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfAttachmentPath = CursorUtil.getColumnIndexOrThrow(_cursor, "attachmentPath");
          final int _cursorIndexOfSuperCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "superCategory");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSettled");
          final int _cursorIndexOfRecurringId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final TransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
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
            final String _tmpAttachmentPath;
            if (_cursor.isNull(_cursorIndexOfAttachmentPath)) {
              _tmpAttachmentPath = null;
            } else {
              _tmpAttachmentPath = _cursor.getString(_cursorIndexOfAttachmentPath);
            }
            final String _tmpSuperCategory;
            if (_cursor.isNull(_cursorIndexOfSuperCategory)) {
              _tmpSuperCategory = null;
            } else {
              _tmpSuperCategory = _cursor.getString(_cursorIndexOfSuperCategory);
            }
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            final String _tmpRecurringId;
            if (_cursor.isNull(_cursorIndexOfRecurringId)) {
              _tmpRecurringId = null;
            } else {
              _tmpRecurringId = _cursor.getString(_cursorIndexOfRecurringId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpDate,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpNote,_tmpDescription,_tmpAttachmentPath,_tmpSuperCategory,_tmpIsSettled,_tmpRecurringId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<TransactionEntity>> searchTransactions(final String query) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        WHERE note LIKE '%' || ? || '%' \n"
            + "           OR description LIKE '%' || ? || '%'\n"
            + "           OR categoryId IN (SELECT id FROM categories WHERE name LIKE '%' || ? || '%')\n"
            + "           OR accountId IN (SELECT id FROM accounts WHERE name LIKE '%' || ? || '%')\n"
            + "        ORDER BY date DESC, createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    _argIndex = 3;
    _statement.bindString(_argIndex, query);
    _argIndex = 4;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions", "categories",
        "accounts"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfAttachmentPath = CursorUtil.getColumnIndexOrThrow(_cursor, "attachmentPath");
          final int _cursorIndexOfSuperCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "superCategory");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSettled");
          final int _cursorIndexOfRecurringId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
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
            final String _tmpAttachmentPath;
            if (_cursor.isNull(_cursorIndexOfAttachmentPath)) {
              _tmpAttachmentPath = null;
            } else {
              _tmpAttachmentPath = _cursor.getString(_cursorIndexOfAttachmentPath);
            }
            final String _tmpSuperCategory;
            if (_cursor.isNull(_cursorIndexOfSuperCategory)) {
              _tmpSuperCategory = null;
            } else {
              _tmpSuperCategory = _cursor.getString(_cursorIndexOfSuperCategory);
            }
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            final String _tmpRecurringId;
            if (_cursor.isNull(_cursorIndexOfRecurringId)) {
              _tmpRecurringId = null;
            } else {
              _tmpRecurringId = _cursor.getString(_cursorIndexOfRecurringId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpDate,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpNote,_tmpDescription,_tmpAttachmentPath,_tmpSuperCategory,_tmpIsSettled,_tmpRecurringId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getTotalIncomeForMonth(final String yearMonth,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(amount), 0.0) FROM transactions \n"
            + "        WHERE type = 'INCOME' AND date LIKE ? || '%'\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Object getTotalExpenseForMonth(final String yearMonth,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(amount), 0.0) FROM transactions \n"
            + "        WHERE type = 'EXPENSE' AND date LIKE ? || '%'\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Flow<List<TransactionEntity>> getTransactionsByDate(final String date) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        WHERE date = ? \n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfAttachmentPath = CursorUtil.getColumnIndexOrThrow(_cursor, "attachmentPath");
          final int _cursorIndexOfSuperCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "superCategory");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSettled");
          final int _cursorIndexOfRecurringId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
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
            final String _tmpAttachmentPath;
            if (_cursor.isNull(_cursorIndexOfAttachmentPath)) {
              _tmpAttachmentPath = null;
            } else {
              _tmpAttachmentPath = _cursor.getString(_cursorIndexOfAttachmentPath);
            }
            final String _tmpSuperCategory;
            if (_cursor.isNull(_cursorIndexOfSuperCategory)) {
              _tmpSuperCategory = null;
            } else {
              _tmpSuperCategory = _cursor.getString(_cursorIndexOfSuperCategory);
            }
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            final String _tmpRecurringId;
            if (_cursor.isNull(_cursorIndexOfRecurringId)) {
              _tmpRecurringId = null;
            } else {
              _tmpRecurringId = _cursor.getString(_cursorIndexOfRecurringId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpDate,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpNote,_tmpDescription,_tmpAttachmentPath,_tmpSuperCategory,_tmpIsSettled,_tmpRecurringId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<String>> getTransactionDatesForMonth(final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT DISTINCT date FROM transactions \n"
            + "        WHERE date LIKE ? || '%'\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
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
  public Flow<List<DailyTotal>> getDailyTotalsForMonth(final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT date, \n"
            + "               SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) as totalExpense,\n"
            + "               SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) as totalIncome\n"
            + "        FROM transactions \n"
            + "        WHERE date LIKE ? || '%'\n"
            + "        GROUP BY date\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<DailyTotal>>() {
      @Override
      @NonNull
      public List<DailyTotal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = 0;
          final int _cursorIndexOfTotalExpense = 1;
          final int _cursorIndexOfTotalIncome = 2;
          final List<DailyTotal> _result = new ArrayList<DailyTotal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyTotal _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final double _tmpTotalIncome;
            _tmpTotalIncome = _cursor.getDouble(_cursorIndexOfTotalIncome);
            _item = new DailyTotal(_tmpDate,_tmpTotalExpense,_tmpTotalIncome);
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
  public Flow<List<CategoryTotal>> getCategoryBreakdown(final String yearMonth, final String type) {
    final String _sql = "\n"
            + "        SELECT categoryId, SUM(amount) as total \n"
            + "        FROM transactions \n"
            + "        WHERE type = ? AND date LIKE ? || '%'\n"
            + "        GROUP BY categoryId \n"
            + "        ORDER BY total DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    _argIndex = 2;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<CategoryTotal>>() {
      @Override
      @NonNull
      public List<CategoryTotal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCategoryId = 0;
          final int _cursorIndexOfTotal = 1;
          final List<CategoryTotal> _result = new ArrayList<CategoryTotal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategoryTotal _item;
            final String _tmpCategoryId;
            _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            _item = new CategoryTotal(_tmpCategoryId,_tmpTotal);
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
  public Flow<Integer> getTransactionCount() {
    final String _sql = "SELECT COUNT(*) FROM transactions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<Integer>() {
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
  public Object getCategorySuggestionsForNote(final String noteKeyword,
      final Continuation<? super List<CategorySuggestion>> $completion) {
    final String _sql = "\n"
            + "        SELECT categoryId, COUNT(*) as count \n"
            + "        FROM transactions \n"
            + "        WHERE note LIKE '%' || ? || '%'\n"
            + "        GROUP BY categoryId \n"
            + "        ORDER BY count DESC \n"
            + "        LIMIT 5\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, noteKeyword);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CategorySuggestion>>() {
      @Override
      @NonNull
      public List<CategorySuggestion> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCategoryId = 0;
          final int _cursorIndexOfCount = 1;
          final List<CategorySuggestion> _result = new ArrayList<CategorySuggestion>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategorySuggestion _item;
            final String _tmpCategoryId;
            _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            _item = new CategorySuggestion(_tmpCategoryId,_tmpCount);
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
  public Flow<List<DailyTotal>> getDailyExpensesSince(final String startDate) {
    final String _sql = "\n"
            + "        SELECT date, SUM(amount) as totalExpense, 0.0 as totalIncome \n"
            + "        FROM transactions \n"
            + "        WHERE type = 'EXPENSE' AND date >= ?\n"
            + "        GROUP BY date \n"
            + "        ORDER BY date ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<DailyTotal>>() {
      @Override
      @NonNull
      public List<DailyTotal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = 0;
          final int _cursorIndexOfTotalExpense = 1;
          final int _cursorIndexOfTotalIncome = 2;
          final List<DailyTotal> _result = new ArrayList<DailyTotal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyTotal _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final double _tmpTotalIncome;
            _tmpTotalIncome = _cursor.getDouble(_cursorIndexOfTotalIncome);
            _item = new DailyTotal(_tmpDate,_tmpTotalExpense,_tmpTotalIncome);
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
  public Object deleteByIds(final List<String> ids, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("DELETE FROM transactions WHERE id IN (");
        final int _inputSize = ids.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (String _item : ids) {
          _stmt.bindString(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
