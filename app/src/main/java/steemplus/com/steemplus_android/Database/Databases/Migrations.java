package steemplus.com.steemplus_android.Database.Databases;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;

public class Migrations {

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE UserAccount "
                    + "ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0; ");
        }
    };
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE UserAccount "
                    + "ADD COLUMN reputation TEXT; ");
        }
    };
    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE UserAccount "
                        + "ADD COLUMN balance REAL NOT NULL DEFAULT 0.; ");
            }
    };
    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE UserAccount "
                    + "ADD COLUMN sbdBalance REAL NOT NULL DEFAULT 0.; ");
            database.execSQL("ALTER TABLE UserAccount "
                    + "ADD COLUMN vestingShares REAL NOT NULL DEFAULT 0.; ");
            database.execSQL("ALTER TABLE UserAccount "
                    + "ADD COLUMN delegatedVestingShares REAL NOT NULL DEFAULT 0.; ");
            database.execSQL("ALTER TABLE UserAccount "
                    + "ADD COLUMN receivedVestingShares REAL NOT NULL DEFAULT 0.; ");
            database.execSQL("ALTER TABLE UserAccount "
                    + "ADD COLUMN savingsSbdBalance REAL NOT NULL DEFAULT 0.; ");
            database.execSQL("ALTER TABLE UserAccount "
                    + "ADD COLUMN savingsSteemBalance REAL NOT NULL DEFAULT 0.; ");
        }
    };

    public static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE UserAccount "
                    + "ADD COLUMN privateMemoKey TEXT; ");
        }
    };

}
