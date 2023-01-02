package com.erif.quicktooltips;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class ActQuickTooltips extends AppCompatActivity {

    private QuickTooltipsHelper helper;

    private int targetViewWidth = 0;
    private int targetViewHeight = 0;
    private int targetViewLeft = 0;
    private int targetViewTop = 0;
    private int targetViewRight = 0;
    private int targetViewBottom = 0;

    private MaterialCardView card;
    private int cardWidth = 0;
    private int cardHeight = 0;

    private long mLastClickTime = 0;

    private QuickTooltipsTargetLayout targetViewLayout;
    private int parentLeft = 0;
    private int parentTop = 0;
    private int parentRight = 0;
    private int parentBottom = 0;

    private ImageView imgTriangle;
    private int triangleHeight = 0;
    private int triangleWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_quick_tooltips);
        helper = new QuickTooltipsHelper(this);

        targetViewLayout = findViewById(R.id.act_quick_tooltips_targetViewLayout);
        card = findViewById(R.id.act_quick_tooltips_card);
        imgTriangle = findViewById(R.id.act_quick_tooltips_img_triangle);
        TextView btnLearn = findViewById(R.id.act_quick_tooltips_btnLearnMore);
        TextView btnGotIt = findViewById(R.id.act_quick_tooltips_btnGotIt);

        btnLearn.setOnClickListener(view -> {
            onClickLearnMore();
        });

        btnGotIt.setOnClickListener(view -> {
            onClickGotIt();
        });

        targetViewLayout.post(() -> {
            card.post(() -> {
                cardWidth = card.getWidth();
                cardHeight = card.getHeight();
                imgTriangle.post(() -> {
                    triangleHeight = imgTriangle.getHeight();
                    triangleWidth = imgTriangle.getWidth();
                    int statusBarHeight = getStatusBarHeight();
                    parentLeft = targetViewLayout.getLeft();
                    parentTop = targetViewLayout.getTop();
                    parentRight = parentLeft + targetViewLayout.getWidth();
                    parentBottom = parentTop + targetViewLayout.getHeight();

                    // Width
                    targetViewWidth = helper.targetWidth();
                    targetViewLayout.setTargetWidth(targetViewWidth);
                    // Height
                    targetViewHeight = helper.targetHeight();
                    targetViewLayout.setTargetHeight(targetViewHeight);

                    // Left
                    targetViewLeft = helper.targetLeft();
                    targetViewLayout.setTargetLeft(targetViewLeft);
                    // Top
                    targetViewTop = helper.targetTop() - statusBarHeight;
                    targetViewLayout.setTargetTop(targetViewTop);
                    // Right
                    targetViewRight = targetViewLeft + targetViewWidth;
                    targetViewLayout.setTargetRight(targetViewRight);
                    // Bottom
                    targetViewBottom = targetViewTop + targetViewHeight;
                    targetViewLayout.setTargetBottom(targetViewBottom);
                    targetViewLayout.invalidate();

                    if (availableTop()) {

                    } else if (availableBottom()) {
                        card.setAlpha(1f);
                        imgTriangle.setAlpha(1f);
                        int newY = targetViewBottom + Math.max(targetViewWidth, targetViewHeight);
                        card.setY(newY);

                        float centerX = targetViewRight - (targetViewWidth / 2f);
                        float newX = centerX - (cardWidth / 2f);
                        card.setX(newX);

                        float triangleX = centerX - (triangleWidth / 2f);
                        imgTriangle.setX(triangleX);

                        float triangleY = card.getY() - triangleHeight;
                        imgTriangle.setRotation(180f);
                        imgTriangle.setY(triangleY);

                    }

                });
            });
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });

    }

    private boolean availableTop() {
        return (parentTop + targetViewTop) > cardHeight + Math.max(targetViewWidth, targetViewHeight);
    }

    private boolean availableBottom() {
        return (parentBottom - targetViewBottom) > cardHeight + Math.max(targetViewWidth, targetViewHeight);
    }

    private void onClickLearnMore() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        new Handler().postDelayed((Runnable) () -> {
            finish();
        }, 250);
    }

    private void onClickGotIt() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        new Handler().postDelayed((Runnable) () -> {
            finish();
        }, 250);
    }

    public int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.anim_quick_tooltips_show, R.anim.anim_quick_tooltips_hide);
    }

    private void log(String message) {
        Log.d("QuickTooltipsLog", message);
    }

}