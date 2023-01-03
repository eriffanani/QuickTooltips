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
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class QuickTooltipsTargetLayout extends RelativeLayout {

    private Paint paint;
    private Paint paintCircle;
    private Paint paintRounded;
    private RectF rectF;
    private float[] corners;
    private float cornersRadius;

    private int targetWidth = 0;
    private int targetHeight = 0;

    private int targetLeft = 0;
    private int targetTop = 0;
    private int targetRight = 0;
    private int targetBottom = 0;

    private boolean animate = false;
    private int additional = 0;
    private ValueAnimator anim;
    private boolean isAnim = false;
    private float circleRadius = 0f;
    private int shape = QuickTooltips.SHAPE_CIRCLE;

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

        rectF = new RectF();
        corners = new float[]{
                cornersRadius, cornersRadius, // Top Left
                cornersRadius, cornersRadius, // Top Right
                cornersRadius, cornersRadius, // Bottom Right
                cornersRadius, cornersRadius, // Bottom Left
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

    public void setShape(int shape) {
        this.shape = shape;
    }

    public void setCornersRadius(float cornersRadius) {
        this.cornersRadius = cornersRadius;
        corners = new float[]{
                cornersRadius, cornersRadius, // Top Left
                cornersRadius, cornersRadius, // Top Right
                cornersRadius, cornersRadius, // Bottom Right
                cornersRadius, cornersRadius, // Bottom Left
        };
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(paint);
        if (shape == QuickTooltips.SHAPE_SQUARE) {
            drawHoleSquare(canvas);
        } else {
            drawHoleCircle(canvas);
        }
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
        if (circleRadius > 0 && animate && !isAnim)
            playAnimCircle();
    }

    private void playAnimCircle() {
        if (isAnim) anim.cancel();
        float size = Math.max(targetWidth, targetHeight);
        anim = ValueAnimator.ofFloat((size / 1.7f), (size / 1.5f));
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

    private void drawHoleSquare(Canvas canvas) {
        if (rectF.left == 0 && rectF.top == 0 && rectF.right == 0 && rectF.bottom == 0) {
            rectF.set(
                    targetLeft - additional,
                    targetTop,
                    targetRight + additional,
                    targetBottom
            );
        }
        Path path = new Path();
        path.addRoundRect(rectF, corners, Path.Direction.CCW);
        if (canvas != null)
            canvas.drawPath(path, paintRounded);
        boolean notFound = rectF.left == 0 && rectF.top == 0 && rectF.right == 0 && rectF.bottom == 0;
        if (!notFound && animate && !isAnim)
            playAnimSquare();
    }

    private void playAnimSquare() {
        if (isAnim) anim.cancel();
        float flexValue = getDimen(getContext(), R.dimen.quick_tooltips_additional);
        anim = ValueAnimator.ofFloat(0f, (flexValue / 2f));
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

    /*
    private void log(String message) {Log.d("QuickTooltipsLog", message);}
    */

}
