package wg.com.photoselected.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import wg.com.photoselected.util.PermissionUtil;

/**
 * Create by Went_Gone on 2019/2/12
 **/
public abstract class WGBaseActivity extends AppCompatActivity implements PermissionUtil.PermissionListener {
    private PermissionUtil permissionUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        permissionUtil = new PermissionUtil(this);
        super.onCreate(savedInstanceState);
        if (!Fresco.hasBeenInitialized()){
            ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                    .setDownsampleEnabled(true)
                    .build();

            Fresco.initialize(this,config);

        }
    }

    @Override
    public void permissionSuccess(int requestCode) {

    }

    @Override
    public void permissionFail(int requestCode) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtil.premissionCallBack(requestCode,grantResults);
    }

    /**
     * 校验权限
     * @param permissions
     * @param requestCode
     */
    protected void requestPermission(String[] permissions, int requestCode){
        permissionUtil.requestPermission(permissions,requestCode,this);
    }
}
