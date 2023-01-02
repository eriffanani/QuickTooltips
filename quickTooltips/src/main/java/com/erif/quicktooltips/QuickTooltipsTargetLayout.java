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
    private int targetWidth = 0;
    private int targetHeight = 0;

    private int targetLeft = 0;
    private int targetTop = 0;
    private int targetRight = 0;
    private int targetBottom = 0;

    private float corner = 0f;
    private int additional = 0;
    private ValueAnimator anim;
    private boolean isAnim = false;

    private float circleRadius = 0f;

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
        corner = getDimen(context, R.dimen.quick_tooltips_corner);

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
                playAnim();
    }

    private void playAnim() {
        float size = Math.max(targetWidth, targetHeight);
        anim = ValueAnimator.ofFloat((size / 1.9f), (size / 1.5f));
        anim.addUpdateListener(valueAnimator -> {
            circleRadius = (float) valueAnimator.getAnimatedValue();
            invalidate();
        });
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setDuration(700L);
        anim.start();
        isAnim = true;
    }

    private void drawHoleRounded(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.set(
                targetLeft - additional,
                targetTop - additional,
                targetRight + additional,
                targetBottom + additional
        );
        Path path = new Path();

        float[] cornerRadius = new float[]{
                corner, corner, // Top Left
                corner, corner, // Top Right
                corner, corner, // Bottom Right
                corner, corner, // Bottom Left
        };

        path.addRoundRect(rectF, cornerRadius, Path.Direction.CCW);
        if (canvas != null)
            canvas.drawPath(path, paintRounded);
    }

    private int getDimen(Context context, int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

}
