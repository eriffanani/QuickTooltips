package com.erif.quicktooltips;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.View;

public class QuickTooltipsBuilder {

    private Context context;
    private View targetView;
    private QuickTooltipsHelper helper;

    public QuickTooltipsBuilder() {}

    public QuickTooltipsBuilder(Context context) {
        this.context = context;
        helper = new QuickTooltipsHelper(context);
    }

    public void show() {
        if (targetView != null) {

            targetView.post(() -> {
                Rect rectF = new Rect();
                //For coordinates location relative to the parent
                targetView.getLocalVisibleRect(rectF);
                //For coordinates location relative to the screen/display
                targetView.getGlobalVisibleRect(rectF);

                // Width
                int width = rectF.width();
                helper.targetWidth(width);

                // Height
                int height = rectF.height();
                helper.targetHeight(height);

                // Left
                helper.targetLeft(rectF.left);
                // Top
                helper.targetTop(rectF.top);
                // Right
                helper.targetRight(rectF.right);
                // Bottom
                helper.targetBottom(rectF.bottom);

            });

        }
        if (context != null)
            context.startActivity(new Intent(context, ActQuickTooltips.class));
    }

    public void targetView(View targetView) {
        this.targetView = targetView;
    }

}
