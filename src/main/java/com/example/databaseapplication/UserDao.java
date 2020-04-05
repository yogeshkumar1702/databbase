package com.example.databaseapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid = :uid")
    User getUserById(int uid);

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "phone_number LIKE :phone LIMIT 1")
    User findByName(String first, String phone);

    @Update
    void update(User user);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}