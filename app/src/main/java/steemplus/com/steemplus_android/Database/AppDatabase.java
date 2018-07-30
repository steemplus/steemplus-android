package steemplus.com.steemplus_android.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import steemplus.com.steemplus_android.Database.Dao.UserAccountDao;
import steemplus.com.steemplus_android.Models.UserAccount;

@Database(entities = {UserAccount.class}, version = 6,  exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserAccountDao userDao();

}
