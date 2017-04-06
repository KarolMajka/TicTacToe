package com.tictactoe;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.graphics.Point;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.AsyncTask;
        import android.os.HandlerThread;
        import android.support.v4.os.AsyncTaskCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.view.Display;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.Toast;

        import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.lang.reflect.InvocationTargetException;
        import java.lang.reflect.Method;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.concurrent.CountDownLatch;


public class MainActivity extends AppCompatActivity {


    private static class newThread extends AsyncTask<Void, Void, Void> {
        int a;
        newThread(int e){
            Log.d("2", "newThread");
            a = e;
        }

        @Override
        protected void onPreExecute(){
            Log.d("1", "onPreExecute");
        }



        @Override
        protected Void doInBackground(Void... params){
            Log.d("3", "doInBackground");
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Log.d("4", "onPostExecute");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

    }


    public void onClickMain(View v) {
        switch(v.getId()) {
            case R.id.singleplayer:
                Intent intent = new Intent(this, SingleChooseActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.multiplayer:
                if(!isOnline(this)) {
                    //Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.exit:
                //android.os.Process.killProcess(android.os.Process.myPid());
                //this.finish();
                new newThread(43).execute();
                break;
        }
    }


    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {
            final boolean[] value = new boolean[1];
            final CountDownLatch latch = new CountDownLatch(1);

            Thread uiThread = new HandlerThread("UIHandler"){
                @Override
                public void run(){
                    HttpURLConnection urlc = null;
                    try {
                        urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
                        //for google.com ResponseCode = 200
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    try {
                        urlc.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        value[0] = (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    latch.countDown(); // Release await() in the test thread.
                }
            };
            uiThread.start();

            //wait for Thread
            try {
                latch.await();
            } catch (InterruptedException e) {
                //e.printStackTrace();
                Log.e("wait", "to long");
            }

            //return value[0];
            if(!value[0])
                Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
            return value[0];
        }
        Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
        return false;
    }


    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        //return px;
    }


    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        //return dp;
    }


    public static int[] getSize(Context context) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        int[] size = new int[3];
        if(android.os.Build.VERSION.SDK_INT < 17) {
            Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
            Method mGetRawH = Display.class.getMethod("getRawHeight");
            Method mGetRawW = Display.class.getMethod("getRawWidth");
            size[0] = (Integer) mGetRawW.invoke(display);
            size[1] = (Integer) mGetRawH.invoke(display);
        }
        else {
            Point tmp_size = new Point();
            ((Activity)context).getWindowManager().getDefaultDisplay().getRealSize(tmp_size);
            size[0] = tmp_size.x;
            size[1] = tmp_size.y;
        }

        Resources resources = ((Activity)context).getResources();
        size[2] = 0;
        if ( resources.getIdentifier("navigation_bar_height", "dimen", "android") > 0) {
            size[2] = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
        }
        return size;
    }


    public static void save(File file, String[] data) {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.length; i++)
                {
                    assert fos != null;
                    fos.write(data[i].getBytes());
                    if (i < data.length-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                assert fos != null;
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }


    public static String[] load(File file) {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String test;
        int anzahl=0;
        try
        {
            while ((test=br.readLine()) != null)
            {
                anzahl++;
            }
        }
        catch (IOException e) {e.printStackTrace();}

        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try
        {
            while((line=br.readLine())!=null)
            {
                array[i] = line;
                i++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return array;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            return true;
 //           this.finish();
 //           return true;
        }

        // let the system handle all other key events
        return super.onKeyDown(keyCode, event);
    }
}
