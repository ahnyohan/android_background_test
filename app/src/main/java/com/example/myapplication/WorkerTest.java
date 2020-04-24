package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WorkerTest extends Worker {
    private final String dbName = "webnautes";
    private final String tableName = "person";
    AppDatabase appDb = null;
    Context context;
    SQLiteDatabase sampleDB = null;
    Timer timer;

    public WorkerTest(
            Context context,
            WorkerParameters params
    ) {
        super(context, params);
        appDb =  AppDatabase.getAppDatabase(context);
    }




    @NonNull
    @Override
    public Result doWork() {


        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                // 반복실행할 구문
                User user = new User();
                user.setId(new Date().getTime());
                user.setFirstName("1111");
                user.setLastName("2222");
                new InsertAsyncTask(appDb.userDao()).execute(user);

            }

        };



        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();


//        if (TT.scheduledExecutionTime() > 0) {
//            System.out.println("@@@@@@111111 " + TT.scheduledExecutionTime());
//            timer.cancel();
//            timer = new Timer();
//        }else {
//            System.out.println("@@@@@@22222 " + TT.scheduledExecutionTime());
//            timer = new Timer();
//        }
        timer.schedule(TT, 0, 10000);

//        updateData.run();
//        User user = new User();
//        user.setFirstName("333");
//        user.setLastName("444");
//        System.out.println("@@@@@@@@@ background!!!");


//        appDb.userDao().insert(user);
//        System.out.println(appDb.userDao().getAll().size());


        return Result.success();
    }



    public static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao mUserDao;

        public  InsertAsyncTask(UserDao userDao){
            this.mUserDao = userDao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(User... user) {
            //추가만하고 따로 SELECT문을 안해도 라이브데이터로 인해
            //getAll()이 반응해서 데이터를 갱신해서 보여줄 것이다,  메인액티비티에 옵저버에 쓴 코드가 실행된다. (라이브데이터는 스스로 백그라운드로 처리해준다.)
            System.out.println("@@@@@@@@@@@@@@@@@ background!!");

            mUserDao.insert(user[0]);

            System.out.println(mUserDao.getAll().size());

            return null;
        }
    }
}
