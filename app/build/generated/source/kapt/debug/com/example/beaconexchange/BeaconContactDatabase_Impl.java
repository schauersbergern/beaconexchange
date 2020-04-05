package com.example.beaconexchange;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.example.beaconexchange.beacon.BeaconContactDao;
import com.example.beaconexchange.beacon.BeaconContactDao_Impl;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class BeaconContactDatabase_Impl extends BeaconContactDatabase {
  private volatile BeaconContactDao _beaconContactDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `BeaconContact` (`beaconId` TEXT NOT NULL, `lastSeen` INTEGER NOT NULL, `minimumDistance` REAL NOT NULL, `duration` REAL NOT NULL, `fit` INTEGER NOT NULL, PRIMARY KEY(`beaconId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '88201053a13c543d3fdd8ae41d9ce117')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `BeaconContact`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsBeaconContact = new HashMap<String, TableInfo.Column>(5);
        _columnsBeaconContact.put("beaconId", new TableInfo.Column("beaconId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBeaconContact.put("lastSeen", new TableInfo.Column("lastSeen", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBeaconContact.put("minimumDistance", new TableInfo.Column("minimumDistance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBeaconContact.put("duration", new TableInfo.Column("duration", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBeaconContact.put("fit", new TableInfo.Column("fit", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBeaconContact = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBeaconContact = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBeaconContact = new TableInfo("BeaconContact", _columnsBeaconContact, _foreignKeysBeaconContact, _indicesBeaconContact);
        final TableInfo _existingBeaconContact = TableInfo.read(_db, "BeaconContact");
        if (! _infoBeaconContact.equals(_existingBeaconContact)) {
          return new RoomOpenHelper.ValidationResult(false, "BeaconContact(com.example.beaconexchange.beacon.BeaconContact).\n"
                  + " Expected:\n" + _infoBeaconContact + "\n"
                  + " Found:\n" + _existingBeaconContact);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "88201053a13c543d3fdd8ae41d9ce117", "037d7b67bcf34ef2b7260065e721cdf8");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "BeaconContact");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `BeaconContact`");
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
  public BeaconContactDao beaconContactDao() {
    if (_beaconContactDao != null) {
      return _beaconContactDao;
    } else {
      synchronized(this) {
        if(_beaconContactDao == null) {
          _beaconContactDao = new BeaconContactDao_Impl(this);
        }
        return _beaconContactDao;
      }
    }
  }
}
