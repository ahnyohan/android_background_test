package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final String dbName = "webnautes";
    private final String tableName = "person";

    private String names[];
    {
        names = new String[]{"Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich", "Jelly Bean", "Kitkat"};
    }

    private final String phones[];
    {
        phones = new String[]{"Android 1.5", "Android 1.6", "Android 2.0", "Android 2.2", "Android 2.3", "Android  3.0", "Android  4.0", "Android  4.1", "Android  4.4"};
    }


    ArrayList<HashMap<String, String>> personList;
    ListView list;
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONE ="phone";

    SQLiteDatabase sampleDB = null;
    ListAdapter adapter;

//    Handler handler = new Handler();
//
//    private Runnable updateData = new Runnable(){
//        public void run(){
//            //call the service here
//            ////// set the interval time here
//            System.out.println("@@@");
//            handler.postDelayed(updateData,1000);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        updateData.run();

//        final AppDatabase appDb = Room.databaseBuilder(getApplicationContext(),
//                AppDatabase.class, "userTest.db")
//                .fallbackToDestructiveMigration()
//                .build();



//        final AppDatabase appDb = AppDatabase.getAppDatabase(this);


        PeriodicWorkRequest mWorkManager = new PeriodicWorkRequest.Builder(WorkerTest.class, 15, TimeUnit.MINUTES).build();
        WorkManager.getInstance(this).enqueue(mWorkManager);

//        new PeriodicWorkRequest.Builder(WorkerTest.class);



//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        Log.i("tag", "This'll run 1000 milliseconds later");
//
//                    }
//                },
//                5000);
//        User user = new User();
//        user.setId(new Date().getTime());
//        user.setFirstName("1111");
//        user.setLastName("2222");
//        new InsertAsyncTask(appDb.userDao()).execute(user);


        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String,String>>();



//        try {
//
//            sampleDB = openOrCreateDatabase(dbName, MODE_PRIVATE, null);
//
//            //테이블이 존재하지 않으면 새로 생성합니다.
//            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
//                    + " (name VARCHAR(20), phone VARCHAR(20) );");
//
////            //테이블이 존재하는 경우 기존 데이터를 지우기 위해서 사용합니다.
////            sampleDB.execSQL("DELETE FROM " + tableName  );
////
////            //새로운 데이터를 테이블에 집어넣습니다..
////            for (int i=0; i<names.length; i++ ) {
////                sampleDB.execSQL("INSERT INTO " + tableName
////                        + " (name, phone)  Values ('" + names[i] + "', '" + phones[i]+"');");
////            }
//
//            sampleDB.execSQL("INSERT INTO " + tableName
//                        + " (name, phone)  Values ('1', '2');");
//
//            sampleDB.close();
//
//        } catch (SQLiteException se) {
//            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
//            Log.e("", se.getMessage());
//        }
//
//        showList();

    }

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao mUserDao;

        public  InsertAsyncTask(UserDao userDao){
            this.mUserDao = userDao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(User... user) {
            //추가만하고 따로 SELECT문을 안해도 라이브데이터로 인해
            //getAll()이 반응해서 데이터를 갱신해서 보여줄 것이다,  메인액티비티에 옵저버에 쓴 코드가 실행된다. (라이브데이터는 스스로 백그라운드로 처리해준다.)
            System.out.println("@@@@@@@@@@@@@@@@@ inser!!");
            System.out.println(user[0].getLastName());

            mUserDao.insert(user[0]);

            System.out.println(mUserDao.getAll().size());

            return null;
        }
    }



    protected void showList(){

        try {



            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);


            //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
            Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName, null);

            if (c != null) {


                if (c.moveToFirst()) {
                    do {

                        //테이블에서 두개의 컬럼값을 가져와서
                        String Name = c.getString(c.getColumnIndex("name"));
                        String Phone = c.getString(c.getColumnIndex("phone"));

                        //HashMap에 넣습니다.
                        HashMap<String,String> persons = new HashMap<String,String>();

                        persons.put(TAG_NAME,Name);
                        persons.put(TAG_PHONE,Phone);

                        //ArrayList에 추가합니다..
                        personList.add(persons);

                    } while (c.moveToNext());
                }
            }

            ReadDB.close();


            //새로운 apapter를 생성하여 데이터를 넣은 후..
            adapter = new SimpleAdapter(
                    this, personList, R.layout.list_item,
                    new String[]{TAG_NAME,TAG_PHONE},
                    new int[]{ R.id.name, R.id.phone}
            );


            //화면에 보여주기 위해 Listview에 연결합니다.
            list.setAdapter(adapter);


        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("",  se.getMessage());
        }

    }
}
