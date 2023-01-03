package com.erif.quicktooltips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;

import java.util.ArrayList;
import java.util.List;

public class QuickTooltips {

    private Context context;

    private int tooltipsId = 0;
    private View targetView;
    private boolean animate;
    private int imgRes;
    private String title;
    private String description;
    private String buttonSecondaryText;
    private String buttonPrimaryText;
    private boolean cancelable = true;
    private boolean closable = false;
    private ActivityResultLauncher<Intent> launcher;
    private int fontFamily = 0;

    private final List<Item> items = new ArrayList<>();
    private int currentPosition = 0;

    private QuickTooltipsPreferences helper;
    private int shape = SHAPE_CIRCLE;
    private float shape_corner_radius = 0f;

    public QuickTooltips() {}

    public QuickTooltips(Context context) {
        this.context = context;
        helper = new QuickTooltipsPreferences(context);
    }

    public void show() {
        if (items.size() > 0) {
            Item mItem = items.get(currentPosition);
            setTooltipsId(mItem.tooltipsId);
            setShape(mItem.shape, mItem.shape_corner_radius);
            targetView(mItem.targetView);
            setImage(mItem.image);
            setTitle(mItem.title);
            setDescription(mItem.description);
            setButtonSecondaryText(mItem.buttonSecondaryText);
            setButtonPrimaryText(mItem.buttonPrimaryText);
            setCancelable(mItem.cancelable);
            setClosable(mItem.closeable);
            setAnimate(mItem.animate);
        }
        setup();
        helper.id(tooltipsId);
        helper.shape(shape);
        helper.shapeCornerRadius(shape_corner_radius);
        helper.image(imgRes);
        helper.title(title);
        helper.description(description);
        helper.btnPriTxt(buttonPrimaryText);
        helper.btnSecTxt(buttonSecondaryText);
        helper.cancelable(cancelable);
        helper.closable(closable);
        helper.animate(animate);
        helper.fontFamily(fontFamily);
    }

    private void setup() {
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
        if (context != null) {
            Intent intent = new Intent(context, ActQuickTooltips.class);
            if (launcher != null) launcher.launch(intent);
            else context.startActivity(intent);
        }
    }

    public void setTooltipsId(int tooltipsId) {
        this.tooltipsId = tooltipsId;
    }

    public void targetView(View targetView) {
        this.targetView = targetView;
    }

    public void targetView(View targetView, boolean animate) {
        this.targetView = targetView;
        this.animate = animate;
    }

    public void setImage(@DrawableRes int imgRes) {
        this.imgRes = imgRes;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setButtonSecondaryText(String buttonSecondaryText) {
        this.buttonSecondaryText = buttonSecondaryText;
    }

    public void setButtonPrimaryText(String buttonPrimaryText) {
        this.buttonPrimaryText = buttonPrimaryText;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void setClosable(boolean closable) {
        this.closable = closable;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public void setShape(int shape, float corner_radius) {
        this.shape = shape;
        this.shape_corner_radius = corner_radius;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    public void setFontFamily(@FontRes int fontFamily) {
        this.fontFamily = fontFamily;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void onClickListener(ActivityResultLauncher<Intent> launcher) {
        this.launcher = launcher;
    }

    public void previous() {
        if (items.size() > 1) {
            if (currentPosition > 0) {
                currentPosition-=1;
            }
        }
    }

    public void next() {
        if (items.size() > 1) {
            if (currentPosition < (items.size() - 1)) {
                currentPosition+=1;
            }
        }
    }

    public static class Item {

        private int tooltipsId;
        private View targetView;
        private int shape;
        private float shape_corner_radius;
        private int image;
        private String title;
        private String description;
        private String buttonSecondaryText;
        private String buttonPrimaryText;
        private boolean cancelable = false;
        private boolean closeable = false;
        private boolean animate = false;

        public Item() {}

        public Item(
                View targetView, int shape, @DrawableRes int image, String title, String description,
                String buttonSecondaryText, String buttonPrimaryText
        ) {
            this.targetView = targetView;
            this.shape = shape;
            this.image = image;
            this.title = title;
            this.description = description;
            this.buttonSecondaryText = buttonSecondaryText;
            this.buttonPrimaryText = buttonPrimaryText;
        }

        public Item(
                int tooltipsId, View targetView, int shape, @DrawableRes int image, String title, String description,
                String buttonSecondaryText, String buttonPrimaryText
        ) {
            this.tooltipsId = tooltipsId;
            this.targetView = targetView;
            this.shape = shape;
            this.image = image;
            this.title = title;
            this.description = description;
            this.buttonSecondaryText = buttonSecondaryText;
            this.buttonPrimaryText = buttonPrimaryText;
        }

        public int getTooltipsId() {
            return tooltipsId;
        }

        public void setTooltipsId(int tooltipsId) {
            this.tooltipsId = tooltipsId;
        }

        public View getTargetView() {
            return targetView;
        }

        public void setTargetView(View targetView) {
            this.targetView = targetView;
        }

        public int getShape() {
            return shape;
        }

        public void setShape(int shape) {
            this.shape = shape;
        }

        public float getShape_corner_radius() {
            return shape_corner_radius;
        }

        public void setShape_corner_radius(float shape_corner_radius) {
            this.shape_corner_radius = shape_corner_radius;
        }

        public int getImage() {
            return image;
        }

        public void setImage(@DrawableRes int image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getButtonSecondaryText() {
            return buttonSecondaryText;
        }

        public void setButtonSecondaryText(String buttonSecondaryText) {
            this.buttonSecondaryText = buttonSecondaryText;
        }

        public String getButtonPrimaryText() {
            return buttonPrimaryText;
        }

        public void setButtonPrimaryText(String buttonPrimaryText) {
            this.buttonPrimaryText = buttonPrimaryText;
        }

        public boolean isCancelable() {
            return cancelable;
        }

        public void setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
        }

        public boolean isCloseable() {
            return closeable;
        }

        public void setCloseable(boolean closeable) {
            this.closeable = closeable;
        }

        public boolean isAnimate() {
            return animate;
        }

        public void setAnimate(boolean animate) {
            this.animate = animate;
        }
    }

    private static final String BUTTON_PRIMARY = "primary";
    private static final String BUTTON_SECONDARY = "secondary";

    public static final int SHAPE_CIRCLE = 0;
    public static final int SHAPE_SQUARE = 1;

    public static int getId(ActivityResult result) {
        int id = 0;
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                id = data.getIntExtra("id", 0);
            }
        }
        return id;
    }

    private static String getButtonType(ActivityResult result) {
        String buttonType = null;
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                buttonType = data.getStringExtra("buttonType");
            }
        }
        return buttonType;
    }

    public static boolean isButtonPrimary(ActivityResult result) {
        String buttonType = getButtonType(result);
        if (buttonType == null)
            return false;
        return buttonType.equals(BUTTON_PRIMARY);
    }

    public static boolean isButtonSecondary(ActivityResult result) {
        String buttonType = getButtonType(result);
        if (buttonType == null)
            return false;
        return buttonType.equals(BUTTON_SECONDARY);
    }

}
