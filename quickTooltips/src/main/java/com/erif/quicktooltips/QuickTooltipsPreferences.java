package com.erif.quicktooltips;

import android.content.Context;
import android.content.SharedPreferences;

public class QuickTooltipsPreferences {

    private final SharedPreferences sp;
    private final SharedPreferences.Editor edit;

    private static final String SP_NAME = "com.erif.quicktooltips.shared.preference";

    private static final String SP_WIDTH = SP_NAME+".target.view.width";
    private static final String SP_HEIGHT = SP_NAME+".target.view.height";

    private static final String SP_LEFT = SP_NAME+".target.view.left";
    private static final String SP_TOP = SP_NAME+".target.view.top";
    private static final String SP_RIGHT = SP_NAME+".target.view.right";
    private static final String SP_BOTTOM = SP_NAME+".target.view.bottom";

    private static final String SP_ID = SP_NAME+".target.id";
    private static final String SP_IMAGE = SP_NAME+".target.image";
    private static final String SP_TITLE = SP_NAME+".target.title";
    private static final String SP_DESCRIPTION = SP_NAME+".target.desc";
    private static final String SP_BTN_PRI_TEXT = SP_NAME+".target.btn.pri.text";
    private static final String SP_BTN_SEC_TEXT = SP_NAME+".target.btn.sec.text";
    private static final String SP_CANCELABLE = SP_NAME+".target.cancelable";
    private static final String SP_CLOSABLE = SP_NAME+".target.closable";
    private static final String SP_SHAPE = SP_NAME+".target.shape";
    private static final String SP_SHAPE_CORNER_RADIUS = SP_NAME+".target.shape.corner.radius";
    private static final String SP_SHAPE_ANIMATE = SP_NAME+".target.shape.animate";
    private static final String SP_FONT_FAMILY = SP_NAME+".target.font.family";

    public QuickTooltipsPreferences(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        edit = sp.edit();
    }

    public void targetWidth(int width) {
        spInt(SP_WIDTH, width);
    }
    public int targetWidth() {
        return spInt(SP_WIDTH);
    }

    public void targetHeight(int top) {
        spInt(SP_HEIGHT, top);
    }
    public int targetHeight() {
        return spInt(SP_HEIGHT);
    }

    public void targetLeft(int left) {
        spInt(SP_LEFT, left);
    }
    public int targetLeft() {
        return spInt(SP_LEFT);
    }

    public void targetTop(int top) {
        spInt(SP_TOP, top);
    }
    public int targetTop() {
        return spInt(SP_TOP);
    }

    public void targetRight(int right) {
        spInt(SP_RIGHT, right);
    }
    public int targetRight() {
        return spInt(SP_RIGHT);
    }

    public void targetBottom(int bottom) {
        spInt(SP_BOTTOM, bottom);
    }
    public int targetBottom() {
        return spInt(SP_BOTTOM);
    }

    public void id(int id) {
        spInt(SP_ID, id);
    }
    public int id() {
        return spInt(SP_ID);
    }

    public void image(int image) {
        spInt(SP_IMAGE, image);
    }
    public int image() {
        return spInt(SP_IMAGE);
    }

    public void title(String title) {
        spString(SP_TITLE, title);
    }
    public String title() {
        return spString(SP_TITLE);
    }

    public void description(String description) {
        spString(SP_DESCRIPTION, description);
    }
    public String description() {
        return spString(SP_DESCRIPTION);
    }

    public void btnPriTxt(String btnPriTxt) {
        spString(SP_BTN_PRI_TEXT, btnPriTxt);
    }
    public String btnPriTxt() {
        return spString(SP_BTN_PRI_TEXT);
    }

    public void btnSecTxt(String btnSecTxt) {
        spString(SP_BTN_SEC_TEXT, btnSecTxt);
    }
    public String btnSecTxt() {
        return spString(SP_BTN_SEC_TEXT);
    }

    public void cancelable(boolean cancelable) {
        spBool(SP_CANCELABLE, cancelable);
    }
    public boolean cancelable() {
        return spBool(SP_CANCELABLE);
    }

    public void closable(boolean closable) {
        spBool(SP_CLOSABLE, closable);
    }
    public boolean closable() {
        return spBool(SP_CLOSABLE);
    }

    public void shape(int shape) {
        spInt(SP_SHAPE, shape);
    }
    public int shape() {
        return spInt(SP_SHAPE);
    }

    public void shapeCornerRadius(float shapeCornerRadius) {
        spFloat(SP_SHAPE_CORNER_RADIUS, shapeCornerRadius);
    }
    public float shapeCornerRadius() {
        return spFloat(SP_SHAPE_CORNER_RADIUS);
    }

    public void animate(boolean animate) {
        spBool(SP_SHAPE_ANIMATE, animate);
    }
    public boolean animate() {
        return spBool(SP_SHAPE_ANIMATE);
    }

    public void fontFamily(int fontFamily) {
        spInt(SP_FONT_FAMILY, fontFamily);
    }
    public int fontFamily() {
        return spInt(SP_FONT_FAMILY);
    }

    private void spInt(String key, int value) {
        if (edit != null) {
            edit.putInt(key, value);
            edit.apply();
            edit.commit();
        }
    }
    private int spInt(String key) {
        if (sp == null)
            return 0;
        return sp.getInt(key, 0);
    }

    private void spString(String key, String value) {
        if (edit != null) {
            edit.putString(key, value);
            edit.apply();
            edit.commit();
        }
    }
    private String spString(String key) {
        if (sp == null)
            return null;
        return sp.getString(key, null);
    }

    private void spBool(String key, boolean value) {
        if (edit != null) {
            edit.putBoolean(key, value);
            edit.apply();
            edit.commit();
        }
    }
    private boolean spBool(String key) {
        if (sp == null)
            return false;
        return sp.getBoolean(key, false);
    }

    private void spFloat(String key, float value) {
        if (edit != null) {
            edit.putFloat(key, value);
            edit.apply();
            edit.commit();
        }
    }
    private float spFloat(String key) {
        if (sp == null)
            return 0f;
        return sp.getFloat(key, 0f);
    }

}
