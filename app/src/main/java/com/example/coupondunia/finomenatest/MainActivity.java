package com.example.coupondunia.finomenatest;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements CellView.OnToggledListener, CellView.OnTouchRemove, ViewTreeObserver.OnGlobalLayoutListener {

    private GridLayout myGridLayout;
    private CellView[] myViews; //stores each CellView in position dx and dy for a n*n grid matrix. CellView[dy * numOfCol + dx].
    private int numRows = 5;
    private int numColumns = 5;

    //nex randomly generated row and column ti get the CellView position from the CellView array.
    public int randomROw, randomCol;
    public static Random r = new Random();
    private CellView newView;
    private boolean onTouchRemove = false;
    private boolean gameOver = false;
    int playerNumber = CellView.PLAYER_1;
    int totalNumberOfTouch;
    Player player1, player2;
    ArrayList<CellView> highlightedSquare; // to keep track of all the points in a n*n matrix which the user is currently placing his finger at.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player1 = new Player();
        player2 = new Player();
        highlightedSquare = new ArrayList<>();

        myGridLayout = (GridLayout) findViewById(R.id.mygrid);

        myGridLayout.setColumnCount(numColumns);
        myGridLayout.setRowCount(numRows);


        if (Integer.parseInt(Build.VERSION.SDK) >= 7) {
            PackageManager pm = getPackageManager();
            boolean hasMultitouch =
                    pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
            if (hasMultitouch) {
                if (pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_JAZZHAND)) {
                    totalNumberOfTouch = 5;
                    Log.d("touch", "");
                }
                else if (pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT)) {
                    totalNumberOfTouch = 2;

                }
                else if (pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)) {
                    totalNumberOfTouch = 2;
                }
            }
            else {
                // set zoom buttons
            }
        }
        else {
            // set zoom buttons
        }
        showDialog();

        myViews = new CellView[numColumns * numRows];
        for (int yPos = 0; yPos < numRows; yPos++) {
            for (int xPos = 0; xPos < numColumns; xPos++) {
                CellView tView = new CellView(this, xPos, yPos);
                tView.setOnToggledListener(this);
                tView.setOnTouchRemoveListener(this);
                myViews[yPos * numColumns + xPos] = tView;
                myGridLayout.addView(tView);
            }
        }

    }


    @Override
    public void OnToggled(CellView v, boolean touchOn, int player) {

        highlightedSquare.add(v);
        if (player == CellView.PLAYER_1) {
            player1.row = v.getIdY();
            player1.column = v.getIdX();
            player1.view = v;
//
        }
        else {
            player2.row = v.getIdY();
            player2.column = v.getIdX();
            player2.view = v;
//
        }

        if (!onTouchRemove) {
            if (v.getIdX() == randomCol && v.getIdY() == randomROw) {
                if (player == CellView.PLAYER_1) {
                    player1.count += 1;
                }
                else {
                    player2.count += 1;
                }
                if (player1.count > (int) totalNumberOfTouch / 2) {
                    showFinalScoreDialog(player, "player 1 is allowed only 2 fingers at a time. GAME OVER!!!");
                    gameOver = true;
                }
                else if (player2.count > (int) totalNumberOfTouch / 2) {
                    showFinalScoreDialog(player, "player 2 is allowed only 2 fingers at a time. GAME OVER!!!");
                    gameOver = true;
                }
                generateRandomIndex(playerNumber);
            }
            else {
                gameOver = true;
                Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
                if (playerNumber == CellView.PLAYER_1) {
                    showFinalScoreDialog(CellView.PLAYER_2, null);
                }
                else {
                    showFinalScoreDialog(CellView.PLAYER_1, null);
                }
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        myGridLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        generateRandomIndex(playerNumber);
    }

    public void generateRandomIndex(int player) {

        newView = generateRandomNumber();
        newView.setColor(true);
        newView.setPlayer(player);
        newView.invalidate();

        if (player == CellView.PLAYER_1) {
            playerNumber = CellView.PLAYER_2;
        }
        else {
            playerNumber = CellView.PLAYER_1;
        }
    }

    @Override
    public void onTouchRemove(CellView v, MotionEvent touchOff, int player) {
        if (!gameOver && !onTouchRemove) {
            if (player == CellView.PLAYER_1) {
                if (v.getIdY() == player1.row && v.getIdX() == player1.column) {
//                    Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
                    showFinalScoreDialog(player, null);
                    onTouchRemove = true;
                }
                else {
                    if (highlightedSquare.contains(v)) {
                        highlightedSquare.remove(v);
                    }
                    onTouchRemove = false;
                    player1.count -= 1;
                }
            }
            else {
                if (v.getIdY() == player2.row && v.getIdX() == player2.column) {
//                    Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
                    showFinalScoreDialog(player, null);
                    onTouchRemove = true;
                }
                else {
                    if (highlightedSquare.contains(v)) {
                        highlightedSquare.remove(v);
                    }
                    onTouchRemove = false;
                    player2.count -= 1;
                }
            }
        }
    }

    public CellView generateRandomNumber() {
        randomROw = r.nextInt(numRows);
        randomCol = r.nextInt(numColumns);

        CellView view = myViews[randomROw * numColumns + randomCol];
        if (!highlightedSquare.contains(view)) {
            return view;
        }
        else {
            return generateRandomNumber();
        }
    }

    public void showDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.opening_info_layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView limit = (TextView) dialog.findViewById(R.id.totalLimit);
        limit.setText("Each player can use only " + (int) (totalNumberOfTouch / 2) + " fingers to hold onto the device at a particular time. ");

        dialog.show();
    }

    public void showFinalScoreDialog(int type, String text) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.final_score_dialog);
        TextView winner;
        winner = (TextView) dialog.findViewById(R.id.tvWinner);

        winner.setText(TextUtils.isEmpty(text) ? ((type == CellView.PLAYER_1) ? "Player 1 LOST " : "Player 2 LOST ") + "GAME OVER!!!!!!!!!" : text);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        ((TextView) dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                refreshViews();
            }
        });

        dialog.show();
    }

    public void refreshViews() {
        for (int yPos = 0; yPos < numRows; yPos++) {
            for (int xPos = 0; xPos < numColumns; xPos++) {
                myViews[yPos * numColumns + xPos].setColor(false);
            }
        }
        myGridLayout.invalidate();
        playerNumber = CellView.PLAYER_1;
        generateRandomIndex(playerNumber);
        onTouchRemove = false;
        gameOver = false;
        player1 = new Player();
        player2 = new Player();
        newView = null;
        highlightedSquare.clear();
    }


    @Override
    public void onGlobalLayout() {

        final int MARGIN = 5;

        int pWidth = myGridLayout.getWidth();
        int pHeight = myGridLayout.getHeight();
        int numOfCol = myGridLayout.getColumnCount();
        int numOfRow = myGridLayout.getRowCount();
        int w = pWidth / numOfCol;
        int h = pHeight / numOfRow;

        for (int yPos = 0; yPos < numOfRow; yPos++) {
            for (int xPos = 0; xPos < numOfCol; xPos++) {
                GridLayout.LayoutParams params =
                        (GridLayout.LayoutParams) myViews[yPos * numOfCol + xPos].getLayoutParams();
                params.width = w - 2 * MARGIN;
                params.height = h - 2 * MARGIN;
                params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                myViews[yPos * numOfCol + xPos].setLayoutParams(params);
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            myGridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        else {
            myGridLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }
}
