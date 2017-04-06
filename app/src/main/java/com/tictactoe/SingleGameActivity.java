package com.tictactoe;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class SingleGameActivity extends AppCompatActivity{
    Random random = new Random(System.currentTimeMillis());
    private Map<Integer, Integer> action = new HashMap<>();
    private Game game;
    private int bot;
    private int turn, turnInNextGame;
    private int Player1Wins = 0, Player2Wins = 0, sumWins = 0, sumGames = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        this.getWindow().setFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE, View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_single_game);

        savedInstanceState = getIntent().getExtras();
        bot = savedInstanceState.getInt("key");
        turn = savedInstanceState.getInt("turn");
        if(turn == -1)
            turn = random.nextInt(2);

        turnInNextGame = turn == 0 ? 1 : 0;
        loadStats();

        action.put(R.id.left_top, 0);
        action.put(R.id.top, 1);
        action.put(R.id.right_top, 2);
        action.put(R.id.left, 3);
        action.put(R.id.center, 4);
        action.put(R.id.right, 5);
        action.put(R.id.left_bottom, 6);
        action.put(R.id.bottom, 7);
        action.put(R.id.right_bottom, 8);
        setLayout();

        game = new Game();

        //lottery who start
        startGame();
    }


    protected void loadStats() {
        final String path = getFilesDir() + "/app";
        if(bot == 1) {
            final File dir = new File(path);
            dir.mkdirs();
            final File file = new File(path + "/app_stats.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            String[] items = MainActivity.load(file);
            if (items.length != 4) {
                Toast.makeText(this, "Loading from file problems", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                Player1Wins = Integer.parseInt(items[0], 5);
                Player2Wins = Integer.parseInt(items[1], 5);
                sumWins = Integer.parseInt(items[2], 5);
                sumGames = Integer.parseInt(items[3], 5);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Fail, while loading stats", Toast.LENGTH_SHORT).show();
                sumWins = sumGames = Player1Wins = Player2Wins = 0;
            }
            if (sumWins != (Player1Wins + Player2Wins) || sumWins > sumGames) {
                Toast.makeText(this, "Loading from file problems", Toast.LENGTH_SHORT).show();
                Player1Wins = 0;
                Player2Wins = 0;
                sumWins = 0;
                sumGames = 0;
            }
            return;
        }

        final File dir = new File(path);
        dir.mkdirs();
        final File file = new File(path + "/tmp_app_stats.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        String[] items = MainActivity.load(file);
        if (items.length != 2) {
            Toast.makeText(this, "Loading from file problems", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Player1Wins = Integer.parseInt(items[0], 5);
            Player2Wins = Integer.parseInt(items[1], 5);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Fail, while loading stats", Toast.LENGTH_SHORT).show();
            Player1Wins = Player2Wins = 0;
        }

}


    protected void saveStats() {
        final String path = getFilesDir() + "/app";
        if(bot == 1) {
            File file = new File(path + "/app_stats.txt");
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Integer.toString(Player1Wins, 5);

            try {
                final String[] items = {
                        Integer.toString(Player1Wins, 5),
                        Integer.toString(Player2Wins, 5),
                        Integer.toString(sumWins, 5),
                        Integer.toString(sumGames, 5)
                };
                MainActivity.save(file, items);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Fail, while saving stats", Toast.LENGTH_SHORT).show();
            }
            return;
        }


        File file = new File(path + "/tmp_app_stats.txt");
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            final String[] items = {
                    Integer.toString(Player1Wins, 5),
                    Integer.toString(Player2Wins, 5)
            };
            MainActivity.save(file, items);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Fail, while saving stats", Toast.LENGTH_SHORT).show();
        }
    }


    protected void setLayout(){


        //Get width, height phone and height navigation bar
        int[] size = new int[3];
        try {
            size = MainActivity.getSize(this);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //Set ImageButton size and margins
        int buttonMargin = size[0]/300;
        int buttonSize =
                (size[0] -
                        getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) -
                        getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin) -
                        MainActivity.dpToPx(buttonMargin, this)*4)/3;
        for (Map.Entry<Integer, Integer> e : action.entrySet()) {
            ImageButton b = (ImageButton)findViewById(e.getKey());
            assert b != null;
            android.view.ViewGroup.LayoutParams params = b.getLayoutParams();
            params.height = buttonSize;
            params.width = buttonSize;
           ((ViewGroup.MarginLayoutParams) b.getLayoutParams()).setMargins(
                   ((ViewGroup.MarginLayoutParams) b.getLayoutParams()).leftMargin * buttonMargin,
                   ((ViewGroup.MarginLayoutParams) b.getLayoutParams()).topMargin * buttonMargin,
                   ((ViewGroup.MarginLayoutParams) b.getLayoutParams()).rightMargin * buttonMargin,
                   ((ViewGroup.MarginLayoutParams) b.getLayoutParams()).bottomMargin * buttonMargin);
            b.setLayoutParams(params);
            b.setEnabled(true);
            b.setImageResource(0);
        }

        //Set GridLayout margins (top and bottom)
        if(bot == 1) {
            TextView tv = (TextView) findViewById(R.id.turn);
            assert tv != null;
            tv.setText("Games: " + Integer.toString(sumGames));
            tv.setTextColor(getResources().getColor(R.color.lines));
        }
        TextView t1 =(TextView) findViewById(R.id.player1_score);
        assert t1 != null;
        t1.setText(Integer.toString(Player1Wins));
        TextView t2 =(TextView) findViewById(R.id.player2_score);
        assert t2 != null;
        t2.setText(Integer.toString(Player2Wins));

    }


    protected void startGame() {
        if(bot != 1) {
            TextView t1 = (TextView) findViewById(R.id.turn);
            assert t1 != null;
            //turn = random.nextBoolean();
            //t1.setVisibility(View.VISIBLE);
            if (turn == 1) {
                t1.setTextColor(getResources().getColor(R.color.player1));
                t1.setText(getResources().getText(R.string.player_red));
            } else {
                t1.setTextColor(getResources().getColor(R.color.player2));
                t1.setText(getResources().getText(R.string.player_blue));
            }
        }
        else{
            if (turn == 1) {
                int[] moveBot;
                moveBot = game.moveBot(random);
                if(moveBot[1] >= 0) {
                    disableButton(moveBot[1], false, true);
                }
                else {
                    Toast.makeText(this ,"SOMETHING GOES WRONG :(", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    this.finish();
                }
            }
        }
    }


    public void onClickSingleGame(View v) {
        int id = v.getId();
        move(id);
    }


    protected void move(int id) {
        ImageButton b = (ImageButton)findViewById(id);
        assert b!= null;
        int action = this.action.get(id);
        int winner;
        if(bot == 0) {
            //TextView t1 = (TextView) findViewById(R.id.Turn1);
            //assert t1 != null;
            //TextView t2 = (TextView) findViewById(R.id.Turn2);
            //assert t2 != null;
            if (turn == 1) {
                disableButton(id, true, false);
                winner = game.move(action, 1);
            } else {
                disableButton(id, true, true);
                winner = game.move(action, 2);
            }
        }
        else {
            winner = game.move(action, 1);
            disableButton(id, true, false);
            if(winner == 0) {
                int[] moveBot;
                moveBot = game.moveBot(random);
                winner = moveBot[0];
                if(winner >= 0) {
                    disableButton(moveBot[1], false, true);
                    if (winner == 0)
                        return;
                }
            }
        }

        switch (winner) {

            //still playing
            case 0:
                nextMove();
                break;

            //player 1 won
            case 1:
                Toast.makeText(this ,"Red player won!", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(this, MainActivity.class);
                //startActivity(intent);
                //this.finish();
                reset(winner);
                break;

            //player 2 won
            case 2:
                Toast.makeText(this ,"Blue player won!", Toast.LENGTH_SHORT).show();
                //intent = new Intent(this, MainActivity.class);
                //startActivity(intent);
                //this.finish();
                reset(winner);
                break;

            //draw
            case 3:
                Toast.makeText(this ,"DRAW!", Toast.LENGTH_SHORT).show();
                //intent = new Intent(this, MainActivity.class);
                //startActivity(intent);
                //this.finish();
                reset(winner);
                break;

            default:
                Toast.makeText(this ,"SOMETHING GOES WRONG :(", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                break;

        }
    }


    protected void disableButton(int button, boolean id, boolean circle) {
        if(id) {
            ImageButton b = (ImageButton) findViewById(button);
            assert b != null;
            b.setEnabled(false);
            if(circle)
                b.setImageResource(R.drawable.circle);
            else
                b.setImageResource(R.drawable.x_mark);
        }
        else
            for (Map.Entry<Integer, Integer> e : action.entrySet())
                if(e.getValue() == button) {
                    ImageButton b = (ImageButton) findViewById(e.getKey());
                    assert b != null;
                    b.setEnabled(false);
                    if(circle)
                        b.setImageResource(R.drawable.circle);
                    else
                        b.setImageResource(R.drawable.x_mark);
                    return;
                }
    }


    protected void nextMove() {
         TextView t1 = (TextView) findViewById(R.id.turn);
         assert t1 != null;
         //TextView t2 = (TextView) findViewById(R.id.Turn2);
         //assert t2 != null;
         if (turn == 1) {
             t1.setTextColor(getResources().getColor(R.color.player2));
             t1.setText(getResources().getText(R.string.player_blue));
             turn = 0;
         } else {
             t1.setTextColor(getResources().getColor(R.color.player1));
             t1.setText(getResources().getText(R.string.player_red));
             turn = 1;
         }
        //turn = !turn;
    }


    protected void reset(int winner) {
        game = null;

        for (Map.Entry<Integer, Integer> e : action.entrySet()) {
            ImageButton b = (ImageButton)findViewById(e.getKey());
            assert b != null;
            b.setEnabled(false);
        }

        switch(winner) {
            case 1:
                Player1Wins++;
                sumWins++;
                break;
            case 2:
                Player2Wins++;
                sumWins++;
                break;
        }
        sumGames++;
        saveStats();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //overridePendingTransition( 0, 0);
                //turn = turn == 1 ? 0 : 1;
                //turn = turnInNextGame;

                Bundle bundle = new Bundle();
                bundle.putInt("turn", turnInNextGame);
                Intent intent = getIntent();
                intent.putExtras(bundle);
                startActivity(intent);

                //overridePendingTransition( 0, 0);
                SingleGameActivity.this.finish();
                //SingleGameActivity.this.recreate();
            }
        };
        thread.start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //android.os.Process.killProcess(android.os.Process.myPid());
            Intent intent = new Intent(this, SingleChooseActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }

        // let the system handle all other key events
        return super.onKeyDown(keyCode, event);
    }

}
