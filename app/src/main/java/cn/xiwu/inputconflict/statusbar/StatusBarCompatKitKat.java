package cn.xiwu.inputconflict.statusbar;

/**
 * Created by zuzu on 2017/12/21.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;

@TargetApi(19)
public class StatusBarCompatKitKat {
    private static final String TAG_FAKE_STATUS_BAR_VIEW = "statusBarView";
    private static final String TAG_MARGIN_ADDED = "marginAdded";

    StatusBarCompatKitKat() {
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }

        return result;
    }

    private static View addFakeStatusBarView(Activity activity, int statusBarColor, int statusBarHeight) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup)window.getDecorView();
        View mStatusBarView = new View(activity);
        LayoutParams layoutParams = new LayoutParams(-1, statusBarHeight);
        layoutParams.gravity = 48;
        mStatusBarView.setLayoutParams(layoutParams);
        mStatusBarView.setBackgroundColor(statusBarColor);
        mStatusBarView.setTag("statusBarView");
        mDecorView.addView(mStatusBarView);
        return mStatusBarView;
    }

    private static void removeFakeStatusBarViewIfExist(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup)window.getDecorView();
        View fakeView = mDecorView.findViewWithTag("statusBarView");
        if(fakeView != null) {
            mDecorView.removeView(fakeView);
        }

    }

    private static void addMarginTopToContentChild(View mContentChild, int statusBarHeight) {
        if(mContentChild != null) {
            if(!"marginAdded".equals(mContentChild.getTag())) {
                LayoutParams lp = (LayoutParams)mContentChild.getLayoutParams();
                lp.topMargin += statusBarHeight;
                mContentChild.setLayoutParams(lp);
                mContentChild.setTag("marginAdded");
            }

        }
    }

    private static void removeMarginTopOfContentChild(View mContentChild, int statusBarHeight) {
        if(mContentChild != null) {
            if("marginAdded".equals(mContentChild.getTag())) {
                LayoutParams lp = (LayoutParams)mContentChild.getLayoutParams();
                lp.topMargin -= statusBarHeight;
                mContentChild.setLayoutParams(lp);
                mContentChild.setTag((Object)null);
            }

        }
    }

    public static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        window.addFlags(67108864);
        ViewGroup mContentView = (ViewGroup)window.findViewById(16908290);
        View mContentChild = mContentView.getChildAt(0);
        int statusBarHeight = getStatusBarHeight(activity);
        removeFakeStatusBarViewIfExist(activity);
        addFakeStatusBarView(activity, statusColor, statusBarHeight);
        addMarginTopToContentChild(mContentChild, statusBarHeight);
        if(mContentChild != null) {
            ViewCompat.setFitsSystemWindows(mContentChild, false);
        }

    }

    public static void translucentStatusBar(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(67108864);
        ViewGroup mContentView = (ViewGroup)activity.findViewById(16908290);
        View mContentChild = mContentView.getChildAt(0);
        removeFakeStatusBarViewIfExist(activity);
        removeMarginTopOfContentChild(mContentChild, getStatusBarHeight(activity));
        if(mContentChild != null) {
            ViewCompat.setFitsSystemWindows(mContentChild, false);
        }

    }

    public static void setStatusBarColorForCollapsingToolbar(Activity activity, AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, int statusColor) {
        Window window = activity.getWindow();
        window.addFlags(67108864);
        ViewGroup mContentView = (ViewGroup)window.findViewById(16908290);
        View mContentChild = mContentView.getChildAt(0);
        mContentChild.setFitsSystemWindows(false);
        ((View)appBarLayout.getParent()).setFitsSystemWindows(false);
        appBarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);
        toolbar.setFitsSystemWindows(true);
        if(toolbar.getTag() == null) {
            android.support.design.widget.CollapsingToolbarLayout.LayoutParams statusBarHeight = (android.support.design.widget.CollapsingToolbarLayout.LayoutParams)toolbar.getLayoutParams();
            statusBarHeight.height += getStatusBarHeight(activity);
            toolbar.setLayoutParams(statusBarHeight);
            toolbar.setTag(Boolean.valueOf(true));
        }

        int statusBarHeight1 = getStatusBarHeight(activity);
        removeFakeStatusBarViewIfExist(activity);
        removeMarginTopOfContentChild(mContentChild, statusBarHeight1);
        final View statusView = addFakeStatusBarView(activity, statusColor, statusBarHeight1);
        Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams()).getBehavior();
        if(behavior != null && behavior instanceof android.support.design.widget.AppBarLayout.Behavior) {
            int verticalOffset = ((android.support.design.widget.AppBarLayout.Behavior)behavior).getTopAndBottomOffset();
            if(Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                statusView.setAlpha(1.0F);
            } else {
                statusView.setAlpha(0.0F);
            }
        } else {
            statusView.setAlpha(0.0F);
        }

        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListener() {
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                    if(statusView.getAlpha() == 0.0F) {
                        statusView.animate().cancel();
                        statusView.animate().alpha(1.0F).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                    }
                } else if(statusView.getAlpha() == 1.0F) {
                    statusView.animate().cancel();
                    statusView.animate().alpha(0.0F).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                }

            }
        });
    }
}

