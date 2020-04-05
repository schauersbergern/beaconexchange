package com.example.beaconexchange.beacon;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class BeaconContactDao_Impl implements BeaconContactDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BeaconContact> __insertionAdapterOfBeaconContact;

  private final EntityInsertionAdapter<BeaconContact> __insertionAdapterOfBeaconContact_1;

  private final EntityDeletionOrUpdateAdapter<BeaconContact> __deletionAdapterOfBeaconContact;

  private final EntityDeletionOrUpdateAdapter<BeaconContact> __updateAdapterOfBeaconContact;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public BeaconContactDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBeaconContact = new EntityInsertionAdapter<BeaconContact>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `BeaconContact` (`beaconId`,`lastSeen`,`minimumDistance`,`duration`,`fit`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BeaconContact value) {
        if (value.getBeaconId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getBeaconId());
        }
        stmt.bindLong(2, value.getLastSeen());
        stmt.bindDouble(3, value.getMinimumDistance());
        stmt.bindDouble(4, value.getDuration());
        final int _tmp;
        _tmp = value.getFit() ? 1 : 0;
        stmt.bindLong(5, _tmp);
      }
    };
    this.__insertionAdapterOfBeaconContact_1 = new EntityInsertionAdapter<BeaconContact>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `BeaconContact` (`beaconId`,`lastSeen`,`minimumDistance`,`duration`,`fit`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BeaconContact value) {
        if (value.getBeaconId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getBeaconId());
        }
        stmt.bindLong(2, value.getLastSeen());
        stmt.bindDouble(3, value.getMinimumDistance());
        stmt.bindDouble(4, value.getDuration());
        final int _tmp;
        _tmp = value.getFit() ? 1 : 0;
        stmt.bindLong(5, _tmp);
      }
    };
    this.__deletionAdapterOfBeaconContact = new EntityDeletionOrUpdateAdapter<BeaconContact>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `BeaconContact` WHERE `beaconId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BeaconContact value) {
        if (value.getBeaconId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getBeaconId());
        }
      }
    };
    this.__updateAdapterOfBeaconContact = new EntityDeletionOrUpdateAdapter<BeaconContact>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `BeaconContact` SET `beaconId` = ?,`lastSeen` = ?,`minimumDistance` = ?,`duration` = ?,`fit` = ? WHERE `beaconId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BeaconContact value) {
        if (value.getBeaconId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getBeaconId());
        }
        stmt.bindLong(2, value.getLastSeen());
        stmt.bindDouble(3, value.getMinimumDistance());
        stmt.bindDouble(4, value.getDuration());
        final int _tmp;
        _tmp = value.getFit() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        if (value.getBeaconId() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getBeaconId());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from beaconcontact";
        return _query;
      }
    };
  }

  @Override
  public void insert(final BeaconContact... beacons) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBeaconContact.insert(beacons);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void upsert(final BeaconContact... beacons) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBeaconContact_1.insert(beacons);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final BeaconContact beacon) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfBeaconContact.handle(beacon);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final BeaconContact... beacons) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfBeaconContact.handleMultiple(beacons);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<BeaconContact> getAll() {
    final String _sql = "SELECT * from beaconcontact";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBeaconId = CursorUtil.getColumnIndexOrThrow(_cursor, "beaconId");
      final int _cursorIndexOfLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeen");
      final int _cursorIndexOfMinimumDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "minimumDistance");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfFit = CursorUtil.getColumnIndexOrThrow(_cursor, "fit");
      final List<BeaconContact> _result = new ArrayList<BeaconContact>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final BeaconContact _item;
        final String _tmpBeaconId;
        _tmpBeaconId = _cursor.getString(_cursorIndexOfBeaconId);
        final long _tmpLastSeen;
        _tmpLastSeen = _cursor.getLong(_cursorIndexOfLastSeen);
        final double _tmpMinimumDistance;
        _tmpMinimumDistance = _cursor.getDouble(_cursorIndexOfMinimumDistance);
        final double _tmpDuration;
        _tmpDuration = _cursor.getDouble(_cursorIndexOfDuration);
        final boolean _tmpFit;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfFit);
        _tmpFit = _tmp != 0;
        _item = new BeaconContact(_tmpBeaconId,_tmpLastSeen,_tmpMinimumDistance,_tmpDuration,_tmpFit);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<BeaconContact>> getAllLiveOrdered() {
    final String _sql = "SELECT * from beaconcontact ORDER BY lastSeen DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"beaconcontact"}, false, new Callable<List<BeaconContact>>() {
      @Override
      public List<BeaconContact> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBeaconId = CursorUtil.getColumnIndexOrThrow(_cursor, "beaconId");
          final int _cursorIndexOfLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeen");
          final int _cursorIndexOfMinimumDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "minimumDistance");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfFit = CursorUtil.getColumnIndexOrThrow(_cursor, "fit");
          final List<BeaconContact> _result = new ArrayList<BeaconContact>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final BeaconContact _item;
            final String _tmpBeaconId;
            _tmpBeaconId = _cursor.getString(_cursorIndexOfBeaconId);
            final long _tmpLastSeen;
            _tmpLastSeen = _cursor.getLong(_cursorIndexOfLastSeen);
            final double _tmpMinimumDistance;
            _tmpMinimumDistance = _cursor.getDouble(_cursorIndexOfMinimumDistance);
            final double _tmpDuration;
            _tmpDuration = _cursor.getDouble(_cursorIndexOfDuration);
            final boolean _tmpFit;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFit);
            _tmpFit = _tmp != 0;
            _item = new BeaconContact(_tmpBeaconId,_tmpLastSeen,_tmpMinimumDistance,_tmpDuration,_tmpFit);
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
  public List<BeaconContact> loadByIds(final String[] beaconIds) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT ");
    _stringBuilder.append("*");
    _stringBuilder.append(" from beaconcontact WHERE beaconId IN (");
    final int _inputSize = beaconIds.length;
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : beaconIds) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBeaconId = CursorUtil.getColumnIndexOrThrow(_cursor, "beaconId");
      final int _cursorIndexOfLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeen");
      final int _cursorIndexOfMinimumDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "minimumDistance");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfFit = CursorUtil.getColumnIndexOrThrow(_cursor, "fit");
      final List<BeaconContact> _result = new ArrayList<BeaconContact>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final BeaconContact _item_1;
        final String _tmpBeaconId;
        _tmpBeaconId = _cursor.getString(_cursorIndexOfBeaconId);
        final long _tmpLastSeen;
        _tmpLastSeen = _cursor.getLong(_cursorIndexOfLastSeen);
        final double _tmpMinimumDistance;
        _tmpMinimumDistance = _cursor.getDouble(_cursorIndexOfMinimumDistance);
        final double _tmpDuration;
        _tmpDuration = _cursor.getDouble(_cursorIndexOfDuration);
        final boolean _tmpFit;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfFit);
        _tmpFit = _tmp != 0;
        _item_1 = new BeaconContact(_tmpBeaconId,_tmpLastSeen,_tmpMinimumDistance,_tmpDuration,_tmpFit);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
