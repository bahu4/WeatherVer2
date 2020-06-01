package com.example.weatherver2.data.dataRoom;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface HistoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHistory(History history);

    @Update
    void update(History history);

    @Delete
    void delete(History history);

    @Query("SELECT COUNT() FROM HISTORY")
    int getCount();

    @Query("DELETE FROM HISTORY")
    void deleteAll();

    @Query("SELECT ID, DATE, NAME, TEMPERATURE FROM HISTORY")
    Cursor getHistories();

    @Query("SELECT ID, DATE, NAME, TEMPERATURE FROM HISTORY WHERE ID=:id")
    History getHistoriesById(long id);

    @Query("SELECT ID, DATE, NAME, TEMPERATURE FROM HISTORY LIMIT 1 OFFSET :position")
    History getHistory(int position);
}
