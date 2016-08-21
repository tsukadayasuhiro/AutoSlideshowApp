package jp.techacademy.yasuhiro.tsukada.autoslideshowapp;


import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    boolean isPlaying = false;

    private Handler mHandler = new Handler();
    Cursor cursor_;
    private Timer mainTimer;

    Button button1;
    Button button2;
    Button button3;



    private static final int PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);


//このcursor_.moveToFirst();は、カーソルの位置が最初にありますよという宣言みたいなもの。



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                getContentsInfo();
            } else {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }

        } else {
            getContentsInfo();
        }




   }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }


    private void getContentsInfo() {

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
        );
        cursor_ = cursor;
        cursor_.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                Uri imageUri1 = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (cursor.moveToFirst()) {
            do {
                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                Uri imageUri1 = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            } while (cursor.moveToNext());
        }
        cursor.close();



    }

    public void onClick(View v) {


        if (v.getId() == R.id.button1) {
            if (!cursor_.moveToNext()) {
                cursor_.moveToFirst();
            }
            //!cursor_.moveToNext()は論理演算子の、否定である.
            // !true => false
            // !false => true
            // となるもの。つまり、
            //if (!cursor_.moveToNext()) {
            //cursor_.moveToFirst();
            // }
            // がtrueの時は処理がfalseになるので、何もしないという判定になり,
            //!cursor_.moveToNext()が実行される。
            //次にいけない。つまりfalseの時は、trueになり cursor_.moveToFirst()が実行され
            //最初にcursorが動くというもの。

/*

if (!cursor_.moveToNext() ) の中の cursor_.moveToNext() が最初に呼ばれ


その処理によって 画像が次に行けたら true, 行けなかったら false が返り


それに対して ! が付いているので true/false が反転し ifの条件分岐がされます。


１行目の cursor_.moveToNext() で次に行けなかった場合 false が返り
! で反転し true となり if 文の中に入ります。

それで、cursor_.moveToFirst();　が呼ばれて初めてカーソルが最初に戻ります。
            */




        } else if (v.getId() == R.id.button2) {
            if (!cursor_.moveToPrevious()) {
                cursor_.moveToLast();
            }
        } else if (v.getId() == R.id.button3) {
       {
                if( isPlaying == false) {
                    button1.setVisibility(View.INVISIBLE);
                    button2.setVisibility(View.INVISIBLE);
                    this.mainTimer = new Timer();
                    //タイマーは一回キャンセルしたら二度と同じタイマーは作れないので、
                    //button3を押すごとに、新しいタイマーを作ってもらわなきゃいけないということ。
                    //そしてまたキャンセルするというもの。
                   this.mainTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            {
                                mHandler.post(new Runnable() {
                                    public void run() {
                                       int fieldIndex = cursor_.getColumnIndex(MediaStore.Images.Media._ID);
                                        Long id = cursor_.getLong(fieldIndex);

                                       Uri imageUri1 = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                                    ImageView imageVIew = (ImageView) findViewById(R.id.imageView1);
                                        imageVIew.setImageURI(imageUri1);


                                       if (!cursor_.moveToNext()) {
                                       cursor_.moveToFirst();
                                            }


                                        }
                                   });
                               }
                            }

                       }, 0, 2000);

                  isPlaying = true; // 再生を開始したらフラグをtrueにする。

              }else{
                   mainTimer.cancel();
                    button1.setVisibility(View.VISIBLE);
                   button2.setVisibility(View.VISIBLE);

                   isPlaying = false; // 再生を止めたらフラグをfalseにする。
                   }

                }
          }
      int fieldIndex = cursor_.getColumnIndex(MediaStore.Images.Media._ID);
      Long id = cursor_.getLong(fieldIndex);
     Uri imageUri1 = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
     ImageView imageVIew = (ImageView) findViewById(R.id.imageView1);
      imageVIew.setImageURI(imageUri1);

        }


    }




//cursor.moveToPreviousは前に移動する
//Timerは文字通りある時間間隔でなにかをしたいときに使います。
//Handlerは別スレッドからあるスレッド上でなにか操作をしたいときに使います。


//timerクラスの使い方

//１）TimerTaskを継承させたクラスを使用するクラス内（MainActivity）に作成する。
//２）run()をオーバーライドする。
//３）ハンドラー経由で処理を実行する（画像の表示）
//です。

//タイマーを起動する場合は、

//１）作成したクラスのインスタンスを作成（new）する。
//２）Timerのインスタンスを作成する。
//３）２）で作成したタイマーをスケジュールする。

//System.out.println("");    システムアウトのやつ

//　アイコン　Google用の画面　一番上の画面　そのゲームのスクショ
//　その他もろもろ・・・