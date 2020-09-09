package com.moon.androiddemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by star on 2017/2/24.
 */

public class DensityUtil {

    private static int isPad = -1;

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取手机宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert manager != null;
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        if (DensityUtil.isPad(context)) {
            return Math.max(size.x, size.y);
        } else {
            return Math.min(size.x, size.y);
        }
    }

    /**
     * 获取手机高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager == null) return 0;
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (DensityUtil.isPad(context)) {
            return Math.min(size.x, size.y);
        } else {
            return Math.max(size.x, size.y);
        }
    }

    private static boolean isNavigationBarShow(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert manager != null;
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        Point realSize = new Point();
        display.getSize(size);
        display.getRealSize(realSize);
        return realSize.y != size.y;
    }

    public static int getNavigationBarHeight(Context context) {
        if (!isNavigationBarShow(context)) {
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * convert px to its equivalent sp
     * <p>
     * 将px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * convert sp to its equivalent px
     * <p>
     * 将sp转换为px
     */
    public static int sp2px(Context context, float sp) {
//        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
//        return (int) (spValue * fontScale + 0.5f);

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕原始宽高
     */
    public static int getDpi(Activity activity) {
        int dpi = 0;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 屏幕尺寸 inch
     *
     * @param context
     * @return
     */
    public static double getScreenInch(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return 0;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        return Math.sqrt(x + y);
    }

    /**
     * check whether the device is pad or not
     *
     * @param context
     * @return
     */
    public static boolean isPad(Context context) {
        if (isPad > -1) return isPad == 1;
        isPad = (isGreatScreen(context) && officialPadCheck(context)) ? 1 : 0;
        return isPad == 1;
    }

    private static boolean isGreatScreen(Context context) {
        double screenInches = getScreenInch(context);
        if (screenInches >= 6.0) {
            return true;
        }
        return false;
    }

    // 华为 tag-al00 不准确
    @Deprecated
    private static boolean hasTelFeature(Activity activity) {
        TelephonyManager telephony = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony == null) return false;
        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return false;
        }
        return true;
    }

    private static boolean officialPadCheck(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isLandscapeScreen(Context context) {
        return getCurrentOrientation(context) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    private static int getCurrentOrientation(Context context) {
        return ((Activity) context).getRequestedOrientation();
    }

    public static void forceLandscape(Context context) {
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void forcePortrait(Context context) {
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void supportScreenOrientation(Context context) {
        if (isPad(context)) {
            forceLandscape(context);
        } else {
            forcePortrait(context);
        }
    }
}
