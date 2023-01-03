package com.erif.quicktooltips;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.WindowCompat;

import com.google.android.material.card.MaterialCardView;

public class ActQuickTooltips extends AppCompatActivity {

    private QuickTooltipsPreferences helper;

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

    private int id;
    private int shape;


    private int curOrientation = 0;
    private static int ORIENTATION_PORTRAIT = 1;
    private static int ORIENTATION_LANDSCAPE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_quick_tooltips);
        helper = new QuickTooltipsPreferences(this);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        targetViewLayout = findViewById(R.id.act_quick_tooltips_targetViewLayout);
        card = findViewById(R.id.act_quick_tooltips_card);
        card.setAlpha(0f);
        ImageView imageView = findViewById(R.id.act_quick_tooltips_imgView);
        imageView.setAlpha(0f);
        imgTriangle = findViewById(R.id.act_quick_tooltips_img_triangle);
        imgTriangle.setAlpha(0f);

        RelativeLayout btnClose = findViewById(R.id.act_quick_tooltips_btnClose);

        TextView btnSecondary = findViewById(R.id.act_quick_tooltips_btnSecondary);
        fontFamily(helper.fontFamily(), btnSecondary);
        btnSecondary.setAlpha(0f);

        TextView btnPrimary = findViewById(R.id.act_quick_tooltips_btnPrimary);
        fontFamily(helper.fontFamily(), btnPrimary);
        btnPrimary.setAlpha(0f);

        TextView txtTitle = findViewById(R.id.act_quick_tooltips_txtTitle);
        fontFamily(helper.fontFamily(), txtTitle);
        txtTitle.setAlpha(0f);

        TextView txtDesc = findViewById(R.id.act_quick_tooltips_txtDesc);
        fontFamily(helper.fontFamily(), txtDesc);
        txtDesc.setAlpha(0f);

        btnSecondary.setOnClickListener(view -> onClickSecondary());
        btnPrimary.setOnClickListener(view -> onClickPrimary());

        shape = helper.shape();
        Drawable imgRes = getResDrawable(helper.image());
        if (imgRes != null) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(helper.image());
        } else {
            imageView.setVisibility(View.GONE);
        }

        targetViewLayout.setAnimate(helper.animate());

        targetViewLayout.post(() -> card.post(() -> {
            cardWidth = card.getWidth();
            cardHeight = card.getHeight();
            targetViewLayout.setCornersRadius(helper.shapeCornerRadius());

            ValueAnimator animCardScale = ValueAnimator.ofFloat(1.5f, 1f);
            animCardScale.addUpdateListener(valueAnimator -> {
                float value = (float) valueAnimator.getAnimatedValue();
                card.setScaleX(value);
                card.setScaleY(value);
            });
            animCardScale.setDuration(300L);
            animCardScale.start();

            targetViewLayout.setShape(shape);
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
                targetViewTop = helper.targetTop();
                targetViewLayout.setTargetTop(targetViewTop);
                // Right
                targetViewRight = targetViewLeft + targetViewWidth;
                targetViewLayout.setTargetRight(targetViewRight);
                // Bottom
                targetViewBottom = targetViewTop + targetViewHeight;
                targetViewLayout.setTargetBottom(targetViewBottom);
                targetViewLayout.invalidate();

                float triangleMargin = getDimen(R.dimen.quick_tooltips_icon_triangle_margin);
                boolean havePlace = true;

                if (availableLeft()) {
                    Toast.makeText(this, "Avail Left", Toast.LENGTH_SHORT).show();
                } else if (availableTop()) {
                    int newY = (targetViewTop - cardHeight) - (shape == QuickTooltips.SHAPE_CIRCLE ?
                            Math.max(targetViewWidth, targetViewHeight) : (int) (targetViewHeight / 1.5f));
                    card.setY(newY);

                    float triangleY = newY + cardHeight + triangleMargin;
                    imgTriangle.setRotation(0f);
                    imgTriangle.setY(triangleY);
                } else if (availableRight()) {
                    Toast.makeText(this, "Avail Right", Toast.LENGTH_SHORT).show();
                }
                else if (availableBottom()) {
                    int newY = targetViewBottom + (shape == QuickTooltips.SHAPE_CIRCLE ?
                            Math.max(targetViewWidth, targetViewHeight) : (int) (targetViewHeight / 1.5f));
                    card.setY(newY);

                    float triangleY = newY - triangleHeight - triangleMargin;
                    imgTriangle.setRotation(180f);
                    imgTriangle.setY(triangleY);
                } else {
                    havePlace = false;
                    /*if (availableTopValue() > availableBottomValue()) {
                        int newY = (targetViewTop - cardHeight) - (shape == QuickTooltips.SHAPE_CIRCLE ?
                                Math.max(targetViewWidth, targetViewHeight) : (int) (targetViewHeight / 1.5f));
                        card.setY(newY);

                        float triangleY = newY + cardHeight + triangleMargin;
                        imgTriangle.setRotation(0f);
                        imgTriangle.setY(triangleY);
                    } else {
                        int newY = targetViewBottom + (shape == QuickTooltips.SHAPE_CIRCLE ?
                                Math.max(targetViewWidth, targetViewHeight) : (int) (targetViewHeight / 1.5f));
                        card.setY(newY);

                        float triangleY = newY - triangleHeight - triangleMargin;
                        imgTriangle.setRotation(180f);
                        imgTriangle.setY(triangleY);

                        float cardBottom = newY + cardHeight;
                        float difference = cardBottom - parentBottom + getDimen(R.dimen.quick_tooltips_safe_area);

                        LinearLayout.LayoutParams paramImg = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                        paramImg.height = imageView.getHeight() - (int) difference;
                        imageView.setLayoutParams(paramImg);
                        imageView.setAdjustViewBounds(false);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    }*/
                }

                if (havePlace) {
                    animAlpha(card, 500L, 0);
                    animAlpha(imgTriangle, 200L, 300L);
                    animAlpha(imageView, 200L, 300L);
                    animAlpha(txtTitle, 350L, 400L);
                    animAlpha(txtDesc, 450L, 500L);
                    animAlpha(btnSecondary, 500L, 600L);
                    animAlpha(btnPrimary, 500L, 700L);
                }

                float centerX = targetViewRight - (targetViewWidth / 2f);
                float newX = centerX - (cardWidth / 2f);
                float currentRight = newX + cardWidth;
                float finalCardX;
                float safeArea = getDimen(R.dimen.quick_tooltips_safe_area);
                if (currentRight > (parentRight - safeArea))
                    finalCardX = (parentRight - safeArea) - cardWidth;
                else
                    finalCardX = Math.max(newX, (parentLeft + safeArea));
                card.setX(finalCardX);

                float triangleX = centerX - (triangleWidth / 2f);
                float cardCorner = getDimen(R.dimen.quick_tooltips_card_corner);

                float currentCardLeft = finalCardX + cardCorner;
                float currentCardRight = (finalCardX + cardWidth) - cardCorner;
                float currentTriangleRight = triangleX + triangleWidth;
                float finalTriangleX;
                if (triangleX < currentCardLeft) {
                    finalTriangleX = currentCardLeft;
                } else if (currentTriangleRight > currentCardRight) {
                    finalTriangleX = currentCardRight - triangleWidth;
                }else {
                    finalTriangleX = triangleX;
                }
                imgTriangle.setX(finalTriangleX);

            });
        }));

        id = helper.id();
        boolean cancelable = helper.cancelable();
        boolean closable = helper.closable();
        btnClose.setVisibility(closable ? View.VISIBLE : View.GONE);

        String title = helper.title();
        String desc = helper.description();
        String newTitle = title == null ? "This Is Default Title" : title;
        String newDesc = desc == null ? "This is default description for your tooltips." : desc;
        txtTitle.setText(newTitle);
        txtDesc.setText(newDesc);

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (cancelable) finish();
            }
        });

        targetViewLayout.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (cancelable)
                new Handler().postDelayed(this::finish, 100);
        });

        btnClose.setOnClickListener((view) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            new Handler().postDelayed(this::finish, 100);
        });

        SensorEventListener m_sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                int orientation;
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    orientation = ORIENTATION_PORTRAIT;
                }
                else {
                    orientation = ORIENTATION_LANDSCAPE;
                }
                if (curOrientation != orientation) {
                    curOrientation = orientation;
                    log(curOrientation == ORIENTATION_PORTRAIT ? "Portrait" : "Landscape");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        sm.registerListener(m_sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);

    }

    private boolean availableLeft() {
        int limit = cardWidth + (shape == QuickTooltips.SHAPE_CIRCLE ?
                Math.max(targetViewWidth, targetViewHeight) : (int) (targetViewWidth / 1.5f));
        return (parentLeft + targetViewLeft) > limit;
    }

    private int availableTopValue() {
        return parentTop + targetViewTop;
    }

    private boolean availableTop() {
        int limit = cardHeight + (shape == QuickTooltips.SHAPE_CIRCLE ?
                Math.max(targetViewWidth, targetViewHeight) : (int) (targetViewHeight / 1.5f));
        return (parentTop + targetViewTop) > limit;
    }

    private boolean availableRight() {
        int limit = cardWidth + (shape == QuickTooltips.SHAPE_CIRCLE ?
                Math.max(targetViewWidth, targetViewHeight) : (int) (targetViewWidth / 1.5f));
        return (parentRight - targetViewRight) > limit;
    }

    private int availableBottomValue() {
        return parentBottom - targetViewBottom;
    }

    private boolean availableBottom() {
        int limit = cardHeight + (shape == QuickTooltips.SHAPE_CIRCLE ?
                Math.max(targetViewWidth, targetViewHeight) : (int) (targetViewHeight / 1.5f));
        return availableBottomValue() > limit;
    }

    private void onClickSecondary() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        new Handler().postDelayed(() -> result("secondary", id), 250);
    }

    private void onClickPrimary() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        new Handler().postDelayed(() -> result("primary", id), 250);
    }

    public int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    private void result(String buttonType, int id) {
        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.putExtra("buttonType", buttonType);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.anim_quick_tooltips_show, R.anim.anim_quick_tooltips_hide);
    }

    private void log(String message) {
        Log.d("QuickTooltipsLog", message);
    }

    private Drawable getResDrawable(@DrawableRes int id) {
        Drawable drawable = null;
        if (id == 0 || id == -1)
            return null;
        try {
            drawable = getResDrawables(id);
        } catch (Resources.NotFoundException e) {
            log("Drawable resource not found.");
        }
        return drawable;
    }

    private Drawable getResDrawables(int id) {
        return ResourcesCompat.getDrawable(getResources(), id, null);
    }

    private void animAlpha(View view, long duration, long delay) {
        ValueAnimator animTriangle = ValueAnimator.ofFloat(0f, 1f);
        animTriangle.addUpdateListener(valueAnimator -> {
            float value = (float) valueAnimator.getAnimatedValue();
            view.setAlpha(value);
        });
        animTriangle.setDuration(duration);
        animTriangle.setStartDelay(delay);
        animTriangle.start();
    }

    private float getDimen(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void fontFamily(@FontRes int fontRes, TextView text) {
        try {
            Typeface typeface;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                typeface = getResources().getFont(fontRes);
            else
                typeface = ResourcesCompat.getFont(this, fontRes);
            text.setTypeface(typeface);
        } catch (Resources.NotFoundException e) {
            Log.d("QuickTooltipsLog", "Font resource not found");
        }
    }

}