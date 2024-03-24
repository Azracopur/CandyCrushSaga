package com.example.candycrush;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.OnSwipe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int[] candies= {
            R.drawable.blue,
            R.drawable.green,
            R.drawable.orange,
            R.drawable.purple,
            R.drawable.red,
            R.drawable.yelllow

    };
    int widthOfBlock,noOfBLocks=8 , widthOfScreen;
    ArrayList<ImageView> candy =new ArrayList<>();
    int candyToBeDragged,candyToBeReplaced;
    int notCandy=R.drawable.transparent;
    Handler mHandler ;
    int interval=100;
    TextView scoreResult;
    int score=0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreResult=findViewById(R.id.score);
        DisplayMetrics displayMetrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthOfScreen =displayMetrics.widthPixels;
        int heightOfScreen =displayMetrics.heightPixels;
        score=0;
        widthOfBlock =widthOfScreen /noOfBLocks;
        createBoard();
        for(ImageView imageView :candy){
            imageView.setOnTouchListener(new OnSwipeListener(this){
                @Override
                void onSwipeLeft(){
                    super.onSwipeLeft();
                    candyToBeDragged=imageView.getId();
                    candyToBeReplaced=candyToBeDragged - 1;
                    candyInterchange();
                    mHandler =new Handler();
                    startRepeat();
                }
                @Override
                void onSwipeRight(){
                    super.onSwipeRight();
                    candyToBeDragged=imageView.getId();
                    candyToBeReplaced=candyToBeDragged + 1;
                    candyInterchange();
                    mHandler =new Handler();
                    startRepeat();
                }
                @Override
                void onSwipeBottom(){
                    super.onSwipeBottom();
                    candyToBeDragged=imageView.getId();
                    candyToBeReplaced=candyToBeDragged + noOfBLocks;
                    candyInterchange();
                    mHandler =new Handler();
                    startRepeat();

                }
                @Override
                void onSwipeTop(){
                    super.onSwipeTop();
                    candyToBeDragged=imageView.getId();
                    candyToBeReplaced=candyToBeDragged - noOfBLocks;
                    candyInterchange();
                    mHandler =new Handler();
                    startRepeat();

                }

            });
            mHandler =new Handler();
            startRepeat();

        }

    }
    private void checkRowForThree(){
        for(int i=0;i<62;i++){
            int chosedCandy=(int) candy.get(i).getTag();
            boolean isBlank=(int) candy.get(i).getTag()==notCandy;
            Integer[] notValid={6,7,14,15,22,23,30,31,38,39,46,47,54,55};
            List<Integer> list = Arrays.asList(notValid);
            if(!list.contains(i)){
                int x=i;
                if((int) candy.get(x++).getTag()==chosedCandy && !isBlank && (int) candy.get(x++).getTag()==chosedCandy &&(int) candy.get(x).getTag()==chosedCandy ){
                    score=score+3;
                    scoreResult.setText(String.valueOf(score));
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x--;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x--;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);

                }
            }

        }
        moveDownCandies();
    }
    private void checkColumnForThree() {
        for (int i = 0; i < noOfBLocks; i++) {
            for (int j = 0; j <= noOfBLocks - 3; j++) {
                int chosedCandy = (int) candy.get(j * noOfBLocks + i).getTag();
                boolean isBlank = (int) candy.get(j * noOfBLocks + i).getTag() == notCandy;
                if ((int) candy.get(j * noOfBLocks + i).getTag() == chosedCandy &&
                        !isBlank &&
                        (int) candy.get((j + 1) * noOfBLocks + i).getTag() == chosedCandy &&
                        (int) candy.get((j + 2) * noOfBLocks + i).getTag() == chosedCandy) {
                    score += 3;
                    scoreResult.setText(String.valueOf(score));
                    candy.get(j * noOfBLocks + i).setImageResource(notCandy);
                    candy.get(j * noOfBLocks + i).setTag(notCandy);

                    candy.get((j + 1) * noOfBLocks + i).setImageResource(notCandy);
                    candy.get((j + 1) * noOfBLocks + i).setTag(notCandy);

                    candy.get((j + 2) * noOfBLocks + i).setImageResource(notCandy);
                    candy.get((j + 2) * noOfBLocks + i).setTag(notCandy);
                }
            }
        }
        moveDownCandies();
    }


    private void moveDownCandies(){
        Integer[] firstRow={0,1,2,3,4,5,6,7};
        List<Integer> list =Arrays.asList(firstRow);
        for(int i=55;i>=0;i--){
            if((int) candy.get(i+noOfBLocks).getTag()==notCandy){
                candy.get(i+noOfBLocks).setImageResource((int) candy.get(i).getTag());
                candy.get(i+noOfBLocks).setTag(candy.get(i).getTag());
                candy.get(i).setImageResource(notCandy);
                candy.get(i).setTag(notCandy);
                if(list.contains(i)&&(int) candy.get(i) .getTag()==notCandy){
                    int randomColor=(int)Math.floor(Math.random()*candies.length);
                    candy.get(i).setImageResource(candies[randomColor]);
                    candy.get(i).setTag(candies[randomColor]);
                }
            }
        }
        for(int i=0;i<8;i++){
            if((int) candy.get(i).getTag()==notCandy){
                int randomColor=(int)Math.floor(Math.random()*candies.length);
                candy.get(i).setImageResource(candies[randomColor]);
                candy.get(i).setTag(candies[randomColor]);
            }
        }

    }

    Runnable repeatChecker =new Runnable() {
        @Override
        public void run() {
            try {
                checkRowForThree();
                checkColumnForThree();
                moveDownCandies();
            }finally {
                mHandler.postDelayed(repeatChecker,interval);


            }
        }
    };
    void startRepeat(){
        repeatChecker.run();
    }
    private void candyInterchange(){
        int background=(int) candy.get(candyToBeReplaced).getTag();
        int background1=(int) candy.get(candyToBeDragged).getTag();
        candy.get(candyToBeDragged).setImageResource(background);
        candy.get(candyToBeReplaced).setImageResource(background1);
        candy.get(candyToBeDragged).setTag(background);
        candy.get(candyToBeReplaced).setTag(background1);
    }

    private void createBoard() {
        GridLayout gridLayout=findViewById(R.id.board);
        gridLayout.setRowCount(noOfBLocks);
        gridLayout.setColumnCount(noOfBLocks);
        gridLayout.getLayoutParams().width= widthOfScreen;
        gridLayout.getLayoutParams().height= widthOfScreen;
        for(int i=0;i<noOfBLocks*noOfBLocks;i++){
            ImageView imageView =new ImageView(this);
            imageView.setId(i);
            imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(widthOfBlock,widthOfBlock));
            imageView.setMaxHeight(widthOfBlock);
            imageView.setMaxWidth(widthOfBlock);
            int randomCandy=(int) Math.floor(Math.random()* candies.length);
            imageView.setImageResource(candies[randomCandy]);
            imageView.setTag(candies[randomCandy]);
            candy.add(imageView);

            gridLayout.addView(imageView);


        }
    }

}