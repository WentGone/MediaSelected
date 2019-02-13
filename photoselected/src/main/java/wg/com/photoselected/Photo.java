package wg.com.photoselected;


import android.content.Context;
import android.content.Intent;

import wg.com.photoselected.ui.WGPhotoActivity;

/**
 * Create by Went_Gone on 2019/2/12
 **/
public class Photo {
    private PhotoSelectedListener photoSelectedListener;

    public static class Builder{
        private Photo photo;
        private Context context;
        public Builder(Context context) {
            photo = new Photo();
            this.context = context;
        }

        public Builder setListener(PhotoSelectedListener photoSelectedListener){
            photo.photoSelectedListener = photoSelectedListener;
            PhotoManager.getInstance(photoSelectedListener);
            return this;
        }

        public void build(){
            if (photo == null || context == null)
                return;

            photo.start(context);
        }
    }

    public void start(Context context){
        context.startActivity(new Intent(context,WGPhotoActivity.class));
    }
}
