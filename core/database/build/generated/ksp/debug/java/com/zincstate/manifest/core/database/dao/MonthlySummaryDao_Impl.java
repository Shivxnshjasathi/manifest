package com.zincstate.manifest.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.zincstate.manifest.core.database.entity.MonthlySummaryEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MonthlySummaryDao_Impl implements MonthlySummaryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MonthlySummaryEntity> __insertionAdapterOfMonthlySummaryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public MonthlySummaryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMonthlySummaryEntity = new EntityInsertionAdapter<MonthlySummaryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `monthly_summaries` (`yearMonth`,`totalIncome`,`totalExpense`,`byCategory`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MonthlySummaryEntity entity) {
        statement.bindString(1, entity.getYearMonth());
        statement.bindDouble(2, entity.getTotalIncome());
        statement.bindDouble(3, entity.getTotalExpense());
        statement.bindString(4, entity.getByCategory());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM monthly_summaries";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrUpdate(final MonthlySummaryEntity summary,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMonthlySummaryEntity.insert(summary);
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
  public Flow<MonthlySummaryEntity> getSummaryForMonth(final String yearMonth) {
    final String _sql = "SELECT * FROM monthly_summaries WHERE yearMonth = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"monthly_summaries"}, new Callable<MonthlySummaryEntity>() {
      @Override
      @Nullable
      public MonthlySummaryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfYearMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "yearMonth");
          final int _cursorIndexOfTotalIncome = CursorUtil.getColumnIndexOrThrow(_cursor, "totalIncome");
          final int _cursorIndexOfTotalExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "totalExpense");
          final int _cursorIndexOfByCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "byCategory");
          final MonthlySummaryEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpYearMonth;
            _tmpYearMonth = _cursor.getString(_cursorIndexOfYearMonth);
            final double _tmpTotalIncome;
            _tmpTotalIncome = _cursor.getDouble(_cursorIndexOfTotalIncome);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final String _tmpByCategory;
            _tmpByCategory = _cursor.getString(_cursorIndexOfByCategory);
            _result = new MonthlySummaryEntity(_tmpYearMonth,_tmpTotalIncome,_tmpTotalExpense,_tmpByCategory);
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
  public Object getSummaryForMonthSync(final String yearMonth,
      final Continuation<? super MonthlySummaryEntity> $completion) {
    final String _sql = "SELECT * FROM monthly_summaries WHERE yearMonth = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MonthlySummaryEntity>() {
      @Override
      @Nullable
      public MonthlySummaryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfYearMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "yearMonth");
          final int _cursorIndexOfTotalIncome = CursorUtil.getColumnIndexOrThrow(_cursor, "totalIncome");
          final int _cursorIndexOfTotalExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "totalExpense");
          final int _cursorIndexOfByCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "byCategory");
          final MonthlySummaryEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpYearMonth;
            _tmpYearMonth = _cursor.getString(_cursorIndexOfYearMonth);
            final double _tmpTotalIncome;
            _tmpTotalIncome = _cursor.getDouble(_cursorIndexOfTotalIncome);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final String _tmpByCategory;
            _tmpByCategory = _cursor.getString(_cursorIndexOfByCategory);
            _result = new MonthlySummaryEntity(_tmpYearMonth,_tmpTotalIncome,_tmpTotalExpense,_tmpByCategory);
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
