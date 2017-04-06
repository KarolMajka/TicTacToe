package com.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import java.io.File;

public class SingleChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_single_choose);
        File file = new File(getFilesDir() + "/app/tmp_app_stats.txt");
        file.delete();
    }


    public void onClickSingleChoose(View v) {
        int id = v.getId();
        Intent intent = new Intent(this, SingleGameActivity.class);
        Bundle bundle = new Bundle();
        switch(id) {
            case R.id.vsPlayer:
                bundle.putInt("key", 0);
                bundle.putInt("turn", -1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.vsBot:
                bundle.putInt("key", 1);
                bundle.putInt("turn", -1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }

        // let the system handle all other key events
        return super.onKeyDown(keyCode, event);
    }

}
