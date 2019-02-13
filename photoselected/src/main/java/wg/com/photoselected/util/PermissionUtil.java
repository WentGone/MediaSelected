package wg.com.photoselected.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Went_Gone on 2018/11/6
 **/
public class PermissionUtil {
    public PermissionUtil(PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }

    public PermissionUtil() {
    }

    private int REQUEST_CODE_PERMISSION;

    /**
     * 请求权限
     * @param permissions  请求的权限
     * @param requestCode  请求权限的请求码
     */
    public void requestPermission(String[] permissions, int requestCode, AppCompatActivity activity) {
        this.REQUEST_CODE_PERMISSION = requestCode;
        if (checkPermissions(permissions,activity)) {
            if (permissionListener != null){
                permissionListener.permissionSuccess(REQUEST_CODE_PERMISSION);
            }
        } else {
//            List<String> needPermissions = getDeniedPermissions(permissions,activity);
            ActivityCompat.requestPermissions(activity,
                    permissions,
                    REQUEST_CODE_PERMISSION);

            /*ActivityCompat.requestPermissions(activity
                    , needPermissions.toArray(new String[needPermissions.size()]),
                    REQUEST_CODE_PERMISSION);*/
        }
    }

    /**
     *检测所有的权限是否都已授权
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限集中需要申请权限的列表
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions, AppCompatActivity context) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }


    /**
     *确认所有的权限是否都已授权
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void premissionCallBack(int requestCode,int[] grantResults){
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                if (permissionListener != null){
                    permissionListener.permissionSuccess(REQUEST_CODE_PERMISSION);
                }
            } else {
                if (permissionListener != null){
                    permissionListener.permissionFail(REQUEST_CODE_PERMISSION);
                }
            }
        }
    }

    private PermissionListener permissionListener;

    public void setPermissionListener(PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }

    public interface PermissionListener{
        /**
         * 获取权限成功
         * @param requestCode
         */
        void permissionSuccess(int requestCode);

        /**
         * 获取权限失败
         * @param requestCode
         */
        void permissionFail(int requestCode);
    }
}
