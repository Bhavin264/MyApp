package com.fittrack.Utility;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;


public class KeyboardUtil {

    private KeyboardUtil() {
    }

    /**
     * This function is used to hide the virtual keyboard.
     *
     * @param activity
     */
    public static void hideKeyBoard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), 0);
    }

    /**
     * Shows the soft keyboard
     */
    public static void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    /**
     * This method works only when Activity is instance of ActionbarActivity and
     * it has overlayed actionbar.
     *
     * @param activity
     * @param onKeyBoardAppearListener
     */
    public static void setKeyBoardAppearListener(
            final FragmentActivity activity,
            final OnKeyBoardAppearListener onKeyBoardAppearListener) {
        final View activityRootView = activity
                .findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    {
                        activityRootView.setTag(this);
                    }

                    @Override
                    public void onGlobalLayout() {
                        int heightDiff = activityRootView.getRootView()
                                .getHeight()
                                - activityRootView.getHeight()
                                - 100; // header height
                        // - activity.v.getHeight();
                        if (heightDiff > 150) { // if more than 100 pixels, its
                            // probably a keyboard...
                            if (onKeyBoardAppearListener != null)
                                onKeyBoardAppearListener.onKeyBoardAppear();
                        } else {
                            if (onKeyBoardAppearListener != null)
                                onKeyBoardAppearListener.onKeyBoardDisappear();
                        }
                    }
                });
    }

    /**
     * This method works only when Activity is instance of ActionbarActivity and
     * it has overlayed actionbar. This removes the KeyboardAppearListener from
     * the activity.
     *
     * @param activity
     */
    public static void removeKeyBoardAppearListener(
            final FragmentActivity activity) {
        final View activityRootView = activity
                .findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().removeGlobalOnLayoutListener(
                (ViewTreeObserver.OnGlobalLayoutListener) activityRootView
                        .getTag());
    }

    public interface OnKeyBoardAppearListener {
        public void onKeyBoardAppear();

        public void onKeyBoardDisappear();
    }

}