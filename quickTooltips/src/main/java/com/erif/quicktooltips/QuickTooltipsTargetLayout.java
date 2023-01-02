package com.erif.quicktooltips;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class QuickTooltipsTargetLayout extends RelativeLayout {

    private Paint paint;
    private Paint paintCircle;
    private Paint paintRounded;
    private RectF rectF;
    private float[] cornerRadius;

    private int targetWidth = 0;
    private int targetHeight = 0;

    private int targetLeft = 0;
    private int targetTop = 0;
    private int targetRight = 0;
    private int targetBottom = 0;

    private int additional = 0;
    private ValueAnimator anim;
    private boolean isAnim = false;

    private float circleRadius = 0f;

    private static final long ANIM_DURATION = 700L;

    public QuickTooltipsTargetLayout(Context context) {
        super(context);
        init(context);
    }

    public QuickTooltipsTargetLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QuickTooltipsTargetLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        setWillNotDraw(false);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ContextCompat.getColor(context, R.color.color_quick_tooltips_overlay));

        // Paint Circle
        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setColor(-0x10);
        paintCircle.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        // Paint Rounded
        paintRounded = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintRounded.setStyle(Paint.Style.FILL);
        paintRounded.setColor(-0x1);
        paintRounded.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        additional = getDimen(context, R.dimen.quick_tooltips_additional);
        float corner = getDimen(context, R.dimen.quick_tooltips_corner);

        rectF = new RectF();
        cornerRadius = new float[]{
                corner, corner, // Top Left
                corner, corner, // Top Right
                corner, corner, // Bottom Right
                corner, corner, // Bottom Left
        };

    }

    public void setTargetWidth(int width) {
        this.targetWidth = width;
    }

    public void setTargetHeight(int height) {
        this.targetHeight = height;
    }

    public void setTargetLeft(int left) {
        this.targetLeft = left;
    }

    public void setTargetTop(int top) {
        this.targetTop = top;
    }

    public void setTargetRight(int right) {
        this.targetRight = right;
    }

    public void setTargetBottom(int bottom) {
        this.targetBottom = bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(paint);
        drawHoleCircle(canvas);
        //drawHoleRounded(canvas);
        super.onDraw(canvas);
    }

    private void drawHoleCircle(Canvas canvas) {
        float centerX = targetLeft + (targetWidth / 2f);
        float centerY = targetTop + (targetHeight / 2f);
        float size = Math.max(targetWidth, targetHeight);
        if (circleRadius == 0)
            circleRadius = size / 1.5f;
        if (canvas != null)
            canvas.drawCircle(centerX, centerY, circleRadius, paintCircle);
        if (circleRadius > 0)
            if (!isAnim)
                playAnimCircle();
    }

    private void playAnimCircle() {
        if (isAnim) anim.cancel();
        float size = Math.max(targetWidth, targetHeight);
        anim = ValueAnimator.ofFloat((size / 1.9f), (size / 1.5f));
        anim.addUpdateListener(valueAnimator -> {
            circleRadius = (float) valueAnimator.getAnimatedValue();
            invalidate();
        });
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setDuration(ANIM_DURATION);
        anim.start();
        isAnim = true;
    }

    private void drawHoleRounded(Canvas canvas) {
        if (rectF.left == 0 && rectF.top == 0 && rectF.right == 0 && rectF.bottom == 0) {
            rectF.set(
                    targetLeft - additional,
                    targetTop,
                    targetRight + additional,
                    targetBottom
            );
        }
        Path path = new Path();
        path.addRoundRect(rectF, cornerRadius, Path.Direction.CCW);
        if (canvas != null)
            canvas.drawPath(path, paintRounded);
        if (rectF.left == 0 && rectF.top == 0 && rectF.right == 0 && rectF.bottom == 0) {

        } else {
            if (!isAnim)
                playAnimRounded();
        }
    }

    private void playAnimRounded() {
        if (isAnim) anim.cancel();
        anim = ValueAnimator.ofFloat(0f, getDimen(getContext(), R.dimen.quick_tooltips_card_elevation));
        anim.addUpdateListener(valueAnimator -> {
            float mValue = (float) valueAnimator.getAnimatedValue();
            rectF.set(
                    (targetLeft - additional) - mValue,
                    targetTop - mValue,
                    (targetRight + additional) + mValue,
                    targetBottom + mValue
            );
            invalidate();
        });
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setDuration(ANIM_DURATION);
        anim.start();
        isAnim = true;
    }

    private int getDimen(Context context, int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

    private void log(String message) {
        Log.d("QuickTooltipsLog", message);
    }

}
