package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    //데이터베이스를 매번 생성하는건 리소스를 많이사용하므로 싱글톤이 권장된다고한다.
    private static AppDatabase INSTANCE;

    //디비객체생성 가져오기
    public static AppDatabase getAppDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class , "userTest.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return  INSTANCE;
    }

    //디비객체제거
    public static void destroyInstance() {
        INSTANCE = null;
    }
}