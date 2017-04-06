package com.tictactoe;

import android.annotation.SuppressLint;
import java.util.Random;

/**
 * Created by Karol on 2016-06-12.
 */
public class Game {
    int[] field = new int[10];
    public Game() {
        for(int i = 0; i < 10; i++) {
                field[i] = 0;
        }

    }


    @SuppressLint("Assert")
    public int move(Integer action, Integer player) {
        assert field[action] != 0;
        field[action] = player;
        field[9]++;
        if(check()) {
            return player;
        }
        if(field[9] == 9) {
            return 3;
        }
        return 0;
    }


    protected Boolean check() {
        return (field[0] == field[1] && field[1] == field[2] && field[0] != 0) ||
                (field[0] == field[3] && field[3] == field[6] && field[0] != 0) ||
                (field[0] == field[4] && field[4] == field[8] && field[0] != 0) ||
                (field[1] == field[4] && field[4] == field[7] && field[1] != 0) ||
                (field[2] == field[5] && field[5] == field[8] && field[2] != 0) ||
                (field[2] == field[4] && field[4] == field[6] && field[2] != 0) ||
                (field[3] == field[4] && field[4] == field[5] && field[3] != 0) ||
                (field[6] == field[7] && field[7] == field[8] && field[6] != 0);
    }


    public int[] moveBot(Random action) {
        //moveBot[0] - result
        //moveBot[1] - field
        int[] move = new int[2];
        field[9]++;


        move[1] = canWin();
        if(move[1] != -1) {
            field[move[1]] = 2;
            move[0] = 2;
            return move;
        }

        move[1] = haveToBlock();
        if(move[1] != -1) {
            field[move[1]] = 2;
            if(field[9] == 9)
                move[0] = 3;
            else
                move[0] = 0;
            return move;
        }

        move[1] = findMove(action);
        if(move[1] == -1) {
            move[0] = -1;
            return move;
        }
        field[move[1]] = 2;

        if(field[9] == 9)
            move[0] = 3;
        else
            move[0] = 0;
        return move;
    }


    protected int canWin() {
        //field[0]
        if((field[0] == 0) && (
                (field[1] == field[2] && field[1] == 2) ||
                (field[3] == field[6] && field[3] == 2) ||
                (field[4] == field[8] && field[4] == 2)))
            return 0;
        //field[1]
        if((field[1] == 0) && (
                (field[0] == field[2] && field[0] == 2) ||
                (field[4] == field[7] && field[4] == 2)))
            return 1;
        //field[2]
        if((field[2] == 0) && (
                (field[0] == field[1] && field[0] == 2) ||
                (field[5] == field[8] && field[5] == 2) ||
                (field[4] == field[6] && field[4] == 2)))
            return 2;
        //field[3]
        if((field[3] == 0) && (
                (field[0] == field[6] && field[0] == 2) ||
                (field[4] == field[5] && field[4] == 2)))
            return 3;
        //field[4]
        if((field[4] == 0) && (
                (field[3] == field[5] && field[3] == 2) ||
                (field[1] == field[7] && field[1] == 2) ||
                (field[0] == field[8] && field[0] == 2)))
            return 4;
        //field[5]
        if((field[5] == 0) && (
                (field[3] == field[4] && field[3] == 2) ||
                (field[2] == field[8] && field[2] == 2)))
            return 5;
        //field[6]
        if((field[6] == 0) && (
                (field[7] == field[8] && field[7] == 2) ||
                (field[0] == field[3] && field[0] == 2) ||
                (field[4] == field[2] && field[4] == 2)))
            return 6;
        //field[7]
        if((field[7] == 0) && (
                (field[6] == field[8] && field[6] == 2) ||
                (field[1] == field[4] && field[1] == 2)))
            return 7;
        //field[8]
        if((field[8] == 0) && (
                (field[6] == field[7] && field[6] == 2) ||
                (field[2] == field[5] && field[2] == 2) ||
                (field[0] == field[4] && field[0] == 2)))
            return 8;

        return -1;
    }


    protected int haveToBlock() {
        //field[0]
        if(field[0] == 0 && (
                (field[1] == field[2] && field[1] == 1) ||
                    (field[3] == field[6] && field[3] == 1) ||
                    (field[4] == field[8] && field[4] == 1)))
            return 0;
        //field[1]
        if((field[1] == 0) && (
                (field[0] == field[2] && field[0] == 1) ||
                        (field[4] == field[7] && field[4] == 1)))
            return 1;
        //field[2]
        if((field[2] == 0) && (
                (field[0] == field[1] && field[0] == 1) ||
                        (field[5] == field[8] && field[5] == 1) ||
                        (field[4] == field[6] && field[4] == 1)))
            return 2;
        //field[3]
        if((field[3] == 0) && (
                (field[0] == field[6] && field[0] == 1) ||
                        (field[4] == field[5] && field[4] == 1)))
            return 3;

        /*
    * 0 1 2
    * 3 4 5
    * 6 7 8
    * */

        //field[4]
        if((field[4] == 0) && (
                (field[3] == field[5] && field[3] == 1) ||
                        (field[1] == field[7] && field[1] == 1) ||
                        (field[2] == field[6] && field[2] == 1) ||
                        (field[0] == field[8] && field[0] == 1)))
            return 4;
        //field[5]
        if((field[5] == 0) && (
                (field[3] == field[4] && field[3] == 1) ||
                        (field[2] == field[8] && field[2] == 1)))
            return 5;
        //field[6]
        if((field[6] == 0) && (
                (field[7] == field[8] && field[7] == 1) ||
                        (field[0] == field[3] && field[0] == 1) ||
                        (field[4] == field[2] && field[4] == 1)))
            return 6;
        //field[7]
        if((field[7] == 0) && (
                (field[6] == field[8] && field[6] == 1) ||
                        (field[1] == field[4] && field[1] == 1)))
            return 7;
        //field[8]
        if((field[8] == 0) && (
                (field[6] == field[7] && field[6] == 1) ||
                        (field[2] == field[5] && field[2] == 1) ||
                        (field[0] == field[4] && field[0] == 1)))
            return 8;

        return -1;
    }


    protected int findMove(Random action) {
        //Random action = new Random(System.currentTimeMillis());
        int move;

        //first action always randomly
        if(field[9] == 1) {
            move = action.nextInt(9);
            return move;
        }


        if(field[9] == 2) {
            //put in corner or in center if he put in corner or in center
            if(field[0] != 0 || field[2] != 0 || field[4] != 0 || field[6] != 0 || field[8] != 0) {
                for(;;) {
                    move = action.nextInt(9);
                    if(field[move] == 0 && (move == 0 || move == 2 || move == 4 || move == 6 || move == 8)) {
                        return move;
                    }
                }
            }
            //if he put on one of sides then put random
            else {
                for(;;) {
                    move = action.nextInt(9);
                    if(field[move] == 0) {
                        return move;
                    }
                }
            }
        }

        if(field[9] == 3) {

            //if bot put in corner, and player on center, then put on the other side in corner
            if(field[4] == 1 && field[0] == 2) {
                move = 8;
                return move;
            }
            if(field[4] == 1 && field[2] == 2) {
                move = 6;
                return move;
            }
            if(field[4] == 1 && field[6] == 2) {
                move = 2;
                return move;
            }
            if(field[4] == 1 && field[8] == 2) {
                move = 0;
                return move;
            }

            //if bot put in corner, and player put in the opposite corner then put in corner
            if( field[6] != 0 && field[2] != 0) {
                for(;;) {
                    move = action.nextInt(9);
                    if(move == 0 || move == 8) {
                        return move;
                    }
                }
            }
            if(field[0] != 0 && field[8] != 0) {
                for(;;) {
                    move = action.nextInt(9);
                    if(move == 2 || move == 6) {
                        return move;
                    }
                }
            }

            //if bot put in corner, and player put in one of closest corners, then put in the opposite corner to bot
            if(field[0] == 2 && (field[2] == 1 || field[6] == 1) ) {
                move = 8;
                return move;
            }
            if(field[8] == 2 && (field[2] == 1 || field[6] == 1) ) {
                move = 0;
                return move;
            }
            if(field[2] == 2 && (field[0] == 1 || field[8] == 1) ) {
                move = 6;
                return move;
            }
            if(field[6] == 2 && (field[0] == 1 || field[8] == 1) ) {
                move = 2;
                return move;
            }

            //if bot put in center, and player in one of the corners, then put randomly
            if(field[4] == 2 && (field[0] == 1 || field[2] == 1 || field[6] == 1 || field[8] == 1) ) {
                for(;;) {
                    move = action.nextInt(9);
                    if(field[move] == 0) {
                        return move;
                    }
                }
            }

            //if bot put in corner, and player on one of sides, then put in center
            if( (field[0] == 2 || field[2] == 2 || field[6] == 2 || field[8] == 2) &&
                    (field[1] == 1 || field[3] == 1 || field[5] == 1 || field[7] == 1) ) {
                move = 4;
                return move;
            }

            //if bot put in one of sides and player put in center, then put on one of the closest sides
            if( (field[1] == 2 || field[3] == 2 || field[5] == 2 || field[7] == 2) && field[4] == 1 ) {
                if(field[1] == 2 || field[7] == 2) {
                    for(;;) {
                        move = action.nextInt(9);
                        if(move == 3 || move == 5) {
                            return move;
                        }
                    }
                }
                else { //if(field[3] == 2 || field[5] == 2)
                    for(;;) {
                        move = action.nextInt(9);
                        if(move == 1 || move == 7) {
                            return move;
                        }
                    }
                }
            }

            //if bot put in one of sides, and center is free, then put there
            if( (field[1] == 2 || field[3] == 2 || field[5] == 2 || field[7] == 2) && field[4] == 0 ) {
                move = 4;
                return move;
            }
        }


        if(field[9] == 4) {
            //if bot put on center and player on two opposite corners, then put on one of sides
            if(  (field[4] == 2) && ((field[2] == field[6] && field[2] == 1) || (field[0] == field[8] && field[0] == 1)) ) {
                for(;;) {
                    move = action.nextInt(9);
                    if(move == 1 || move == 3 || move == 5 || move == 7)
                        return move;
                }

            }

            //if bot put on opposite side to player, and player also put on center then put randomly
            if(field[4] == 1 &&
                ((field[1] == 1 && field[7] == 2) ||
                        (field[7] == 1 && field[1] == 2) ||
                        (field[3] == 1 && field[5] == 2) ||
                        (field[5] == 1 && field[3] == 2)) ) {
                for(;;) {
                    move = action.nextInt(9);
                    if(field[move] == 0)
                        return move;
                }
            }

            //if bot put on opposite side to player, and player also put in corner from bot side, then put in corner close to player
            if(field[1] == 1 && field[7] == 2 && (field[8] == 1 || field[6] == 1) ) {
                for(;;) {
                    move = action.nextInt(9);
                    if(move == 2 || move == 0)
                        return move;
                }
            }
            if(field[1] == 2 && field[7] == 1 && (field[0] == 1 || field[2] == 1) ) {
                for(;;) {
                    move = action.nextInt(9);
                    if(move == 8 || move == 6)
                        return move;
                }
            }
            if(field[3] == 2 && field[5] == 1 && (field[0] == 1 || field[6] == 1) ) {
                for(;;) {
                    move = action.nextInt(9);
                    if(move == 2 || move == 8)
                        return move;
                }
            }
            if(field[3] == 1 && field[5] == 2 && (field[2] == 1 || field[8] == 1) ) {
                for(;;) {
                    move = action.nextInt(9);
                    if(move == 0 || move == 6)
                        return move;
                }
            }

            //if bot put on opposite corner to player, and player also put on center, then put in corner
            if( field[1] == 1 &&
                    (field[6] == 1 && field[2] == 2) ||
                    (field[6] == 2 && field[2] == 1) ||
                    (field[0] == 1 && field[8] == 2) ||
                    (field[0] == 2 && field[8] == 1)) {
                for(;;){
                    move = action.nextInt(9);
                    if(field[move] == 0 && (move == 0 || move == 2 || move == 6 || move == 8))
                        return move;
                }
            }

            //if bot put on left/right side to player and player also put in corner, then put on center
            if( (field[1] == 1 && (field[3] == 2 || field[5] == 2) && (field[6] == 1 || field[8] == 1)) ||
                    (field[3] == 1 && (field[1] == 2 || field[7] == 2) && (field[2] == 1 || field[8] == 1)) ||
                    (field[5] == 1 && (field[1] == 2 || field[7] == 2) && (field[0] == 1 || field[6] == 1)) ||
                    (field[7] == 1 && (field[3] == 2 || field[5] == 2) && (field[0] == 1 || field[2] == 1))) {
                move = 4;
                return move;
            }

    /*
    * 0 1 2
    * 3 4 5
    * 6 7 8
    * */
            //if bot put on center and player put on two closest sides then put in corner close to player puts
            if(field[4] == 2) {
                if(field[1] == 1 && field[3] == 1) {
                    move = 0;
                    return move;
                }
                if(field[1] == 1 && field[5] == 1) {
                    move = 2;
                    return move;
                }
                if(field[3] == 1 && field[7] == 1) {
                    move = 6;
                    return move;
                }
                if(field[5] == 1 && field[7] == 1) {
                    move = 8;
                    return move;
                }
            }
        }

        if(field[4] == 0) {
            move = 4;
            return move;
        }
        for(;;) {
            move = action.nextInt(9);
            if (field[move] == 0)
                return move;
        }
    }

}
