package com.erif.quicktooltips;

import android.content.Context;
import android.content.SharedPreferences;

public class QuickTooltipsHelper {

    private SharedPreferences sp;
    private SharedPreferences.Editor edit;

    private static final String SP_NAME = "com.erif.quicktooltips.shared.preference";
    private static final String SP_WIDTH = SP_NAME+".target.view.width";
    private static final String SP_HEIGHT = SP_NAME+".target.view.height";

    private static final String SP_LEFT = SP_NAME+".target.view.left";
    private static final String SP_TOP = SP_NAME+".target.view.top";
    private static final String SP_RIGHT = SP_NAME+".target.view.right";
    private static final String SP_BOTTOM = SP_NAME+".target.view.bottom";

    public QuickTooltipsHelper(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        edit = sp.edit();
    }

    public void targetWidth(int top) {
        spInt(SP_WIDTH, top);
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

}
