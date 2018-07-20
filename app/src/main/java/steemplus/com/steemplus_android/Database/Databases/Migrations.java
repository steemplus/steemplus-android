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
}
