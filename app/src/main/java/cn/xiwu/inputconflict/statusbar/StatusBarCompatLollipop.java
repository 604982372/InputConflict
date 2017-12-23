package cn.xiwu.inputconflict.statusbar;

/**
 * Created by zuzu on 2017/12/21.
 */


import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CollapsingToolbarLayout.LayoutParams;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

@TargetApi(21)
public class StatusBarCompatLollipop {
    StatusBarCompatLollipop() {
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }

        return result;
    }

    public static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        window.clearFlags(67108864);
        window.addFlags(-2147483648);
        window.setStatusBarColor(statusColor);
        window.getDecorView().setSystemUiVisibility(0);
        ViewGroup mContentView = (ViewGroup)window.findViewById(16908290);
        View mChildView = mContentView.getChildAt(0);
        if(mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }

    }

    public  static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        window.addFlags(-2147483648);
        if(hideStatusBarBackground) {
            window.clearFlags(67108864);
            window.setStatusBarColor(0);
            window.getDecorView().setSystemUiVisibility(1280);
        } else {
            window.addFlags(67108864);
            window.getDecorView().setSystemUiVisibility(0);
        }

        ViewGroup mContentView = (ViewGroup)window.findViewById(16908290);
        View mChildView = mContentView.getChildAt(0);
        if(mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }

    }

    public static void setStatusBarColorForCollapsingToolbar(Activity activity, AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, final int statusColor) {
        final Window window = activity.getWindow();
        window.clearFlags(67108864);
        window.addFlags(-2147483648);
        window.setStatusBarColor(0);
        window.getDecorView().setSystemUiVisibility(0);
        ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout, new OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                return insets;
            }
        });
        ViewGroup mContentView = (ViewGroup)window.findViewById(16908290);
        View mChildView = mContentView.getChildAt(0);
        if(mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }

        ((View)appBarLayout.getParent()).setFitsSystemWindows(false);
        appBarLayout.setFitsSystemWindows(false);
        toolbar.setFitsSystemWindows(true);
        if(toolbar.getTag() == null) {
            LayoutParams behavior = (LayoutParams)toolbar.getLayoutParams();
            behavior.height += getStatusBarHeight(activity);
            toolbar.setLayoutParams(behavior);
            toolbar.setTag(Boolean.valueOf(true));
        }

        Behavior behavior1 = ((android.support.design.widget.CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams()).getBehavior();
        if(behavior1 != null && behavior1 instanceof android.support.design.widget.AppBarLayout.Behavior) {
            int verticalOffset = ((android.support.design.widget.AppBarLayout.Behavior)behavior1).getTopAndBottomOffset();
            if(Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                window.setStatusBarColor(statusColor);
            } else {
                window.setStatusBarColor(0);
            }
        } else {
            window.setStatusBarColor(0);
        }

        collapsingToolbarLayout.setFitsSystemWindows(false);
        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListener() {
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ValueAnimator animator;
                if(Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                    if(window.getStatusBarColor() == 0) {
                        animator = ValueAnimator.ofArgb(new int[]{0, statusColor}).setDuration(collapsingToolbarLayout.getScrimAnimationDuration());
                        animator.addUpdateListener(new AnimatorUpdateListener() {
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                window.setStatusBarColor(((Integer)valueAnimator.getAnimatedValue()).intValue());
                            }
                        });
                        animator.start();
                    }
                } else if(window.getStatusBarColor() == statusColor) {
                    animator = ValueAnimator.ofArgb(new int[]{statusColor, 0}).setDuration(collapsingToolbarLayout.getScrimAnimationDuration());
                    animator.addUpdateListener(new AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            window.setStatusBarColor(((Integer)valueAnimator.getAnimatedValue()).intValue());
                        }
                    });
                    animator.start();
                }

            }
        });
        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);
        collapsingToolbarLayout.setStatusBarScrimColor(statusColor);
    }
}

