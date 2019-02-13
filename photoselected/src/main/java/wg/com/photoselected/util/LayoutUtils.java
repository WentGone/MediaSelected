package wg.com.photoselected.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.Window;

public class LayoutUtils {

    public static int toolbarHeight(Context context) {
        return dpToPx(context, 56);
    }

    public static int tabBarHeight(Context context) {
        return dpToPx(context, 48);
    }

    public static int statusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int statusBarHeight(Activity activity) {
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    public static int screenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int spToPx(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }

    public static int dpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * @param pxValue 传入的px的值
     *  px转换为sp
     */
    public static int pxTosp(Context context, float pxValue){
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * @param pxValue 传入的px的值
     *  px转换为dp
     */
    public static int pxTodp(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue/scale+0.5f);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

}
