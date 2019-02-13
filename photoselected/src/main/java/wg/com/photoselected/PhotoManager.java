package wg.com.photoselected;

import java.util.ArrayList;
import java.util.List;

import wg.com.photoselected.model.WGMedia;

/**
 * Create by Went_Gone on 2019/2/12
 **/
public class PhotoManager {
    private PhotoSelectedListener photoSelectedListener;
    private static PhotoManager manager;
    public static PhotoManager getInstance(){
        if (manager == null){
            synchronized (PhotoManager.class){
                if (manager == null){
                    manager = new PhotoManager();
                }
            }
        }
        return manager;
    }
    public static PhotoManager getInstance(PhotoSelectedListener photoSelectedListener){
        if (manager == null){
            synchronized (PhotoManager.class){
                if (manager == null){
                    manager = new PhotoManager(photoSelectedListener);
                }
            }
        }
        return manager;
    }

    public PhotoManager() {
    }

    public PhotoManager(PhotoSelectedListener photoSelectedListener) {
        this.photoSelectedListener = photoSelectedListener;
    }

    public PhotoSelectedListener getPhotoSelectedListener() {
        return photoSelectedListener;
    }

    private List<WGMedia> wgMedias = new ArrayList<>();

    public List<WGMedia> getWgMedias() {
        return wgMedias;
    }

    public void setWgMedias(List<WGMedia> wgMedias) {
        this.wgMedias = wgMedias;
    }

    private int prePhotoPosition;

    public int getPrePhotoPosition() {
        return prePhotoPosition;
    }

    public void setPrePhotoPosition(int prePhotoPosition) {
        this.prePhotoPosition = prePhotoPosition;
    }
}
