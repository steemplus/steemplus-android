package steemplus.com.steemplus_android.Database.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import steemplus.com.steemplus_android.Models.UserAccount;

@Dao
public interface UserAccountDao {

    @Query("SELECT * FROM UserAccount")
    List<UserAccount> getAll();

    @Query("SELECT * FROM UserAccount where username LIKE  :username")
    UserAccount findByName(String username);

    @Query("SELECT * FROM UserAccount where userAccountId LIKE  :userAccountId")
    UserAccount findById(String userAccountId);

    @Query("SELECT * FROM UserAccount where isFavorite = 1")
    UserAccount getFavorite();

    @Update
    void setFavorite(UserAccount... users);

    @Update
    void update(UserAccount... users);

    @Query("SELECT COUNT(*) from UserAccount")
    int countUsers();

    @Insert
    long[] insertAll(UserAccount... users);

    @Insert
    long insert(UserAccount user);

    @Delete
    void delete(UserAccount user);
}
