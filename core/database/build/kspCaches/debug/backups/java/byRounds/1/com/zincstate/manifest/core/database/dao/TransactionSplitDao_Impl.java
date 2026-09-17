package com.zincstate.manifest.core.database.dao;

import android.database.Cursor;
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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.zincstate.manifest.core.database.entity.TransactionSplitEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
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
public final class TransactionSplitDao_Impl implements TransactionSplitDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TransactionSplitEntity> __insertionAdapterOfTransactionSplitEntity;

  private final EntityDeletionOrUpdateAdapter<TransactionSplitEntity> __deletionAdapterOfTransactionSplitEntity;

  private final SharedSQLiteStatement __preparedStmtOfSettleSplit;

  public TransactionSplitDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransactionSplitEntity = new EntityInsertionAdapter<TransactionSplitEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `transaction_splits` (`id`,`transactionId`,`contactId`,`amount`,`isSettled`,`createdAt`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionSplitEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTransactionId());
        statement.bindString(3, entity.getContactId());
        statement.bindDouble(4, entity.getAmount());
        final int _tmp = entity.isSettled() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfTransactionSplitEntity = new EntityDeletionOrUpdateAdapter<TransactionSplitEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `transaction_splits` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionSplitEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__preparedStmtOfSettleSplit = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE transaction_splits SET isSettled = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSplits(final List<TransactionSplitEntity> splits,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTransactionSplitEntity.insert(splits);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final TransactionSplitEntity split,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTransactionSplitEntity.handle(split);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object settleSplit(final String splitId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSettleSplit.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, splitId);
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
          __preparedStmtOfSettleSplit.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TransactionSplitEntity>> getSplitsForTransaction(final String transactionId) {
    final String _sql = "SELECT * FROM transaction_splits WHERE transactionId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, transactionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transaction_splits"}, new Callable<List<TransactionSplitEntity>>() {
      @Override
      @NonNull
      public List<TransactionSplitEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionId");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contactId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSettled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TransactionSplitEntity> _result = new ArrayList<TransactionSplitEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionSplitEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTransactionId;
            _tmpTransactionId = _cursor.getString(_cursorIndexOfTransactionId);
            final String _tmpContactId;
            _tmpContactId = _cursor.getString(_cursorIndexOfContactId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new TransactionSplitEntity(_tmpId,_tmpTransactionId,_tmpContactId,_tmpAmount,_tmpIsSettled,_tmpCreatedAt);
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
  public Flow<List<TransactionSplitEntity>> getUnsettledSplitsForContact(final String contactId) {
    final String _sql = "SELECT * FROM transaction_splits WHERE contactId = ? AND isSettled = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, contactId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transaction_splits"}, new Callable<List<TransactionSplitEntity>>() {
      @Override
      @NonNull
      public List<TransactionSplitEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionId");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contactId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSettled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TransactionSplitEntity> _result = new ArrayList<TransactionSplitEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionSplitEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTransactionId;
            _tmpTransactionId = _cursor.getString(_cursorIndexOfTransactionId);
            final String _tmpContactId;
            _tmpContactId = _cursor.getString(_cursorIndexOfContactId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new TransactionSplitEntity(_tmpId,_tmpTransactionId,_tmpContactId,_tmpAmount,_tmpIsSettled,_tmpCreatedAt);
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
  public Flow<List<TransactionSplitEntity>> getSplitsForTransactions(
      final List<String> transactionIds) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM transaction_splits WHERE transactionId IN (");
    final int _inputSize = transactionIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : transactionIds) {
      _statement.bindString(_argIndex, _item);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transaction_splits"}, new Callable<List<TransactionSplitEntity>>() {
      @Override
      @NonNull
      public List<TransactionSplitEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionId");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contactId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSettled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TransactionSplitEntity> _result = new ArrayList<TransactionSplitEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionSplitEntity _item_1;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTransactionId;
            _tmpTransactionId = _cursor.getString(_cursorIndexOfTransactionId);
            final String _tmpContactId;
            _tmpContactId = _cursor.getString(_cursorIndexOfContactId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item_1 = new TransactionSplitEntity(_tmpId,_tmpTransactionId,_tmpContactId,_tmpAmount,_tmpIsSettled,_tmpCreatedAt);
            _result.add(_item_1);
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
  public Flow<List<TransactionSplitEntity>> getSplitsForTransactionsByMonth(
      final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT ts.* FROM transaction_splits ts\n"
            + "        INNER JOIN transactions t ON ts.transactionId = t.id\n"
            + "        WHERE t.date LIKE ? || '%'\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transaction_splits",
        "transactions"}, new Callable<List<TransactionSplitEntity>>() {
      @Override
      @NonNull
      public List<TransactionSplitEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionId");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contactId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSettled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TransactionSplitEntity> _result = new ArrayList<TransactionSplitEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionSplitEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTransactionId;
            _tmpTransactionId = _cursor.getString(_cursorIndexOfTransactionId);
            final String _tmpContactId;
            _tmpContactId = _cursor.getString(_cursorIndexOfContactId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new TransactionSplitEntity(_tmpId,_tmpTransactionId,_tmpContactId,_tmpAmount,_tmpIsSettled,_tmpCreatedAt);
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
  public Flow<Double> getOwedAmountForContact(final String contactId) {
    final String _sql = "SELECT SUM(amount) FROM transaction_splits WHERE contactId = ? AND isSettled = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, contactId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transaction_splits"}, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
