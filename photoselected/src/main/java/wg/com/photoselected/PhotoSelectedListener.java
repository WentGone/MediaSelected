package wg.com.photoselected;

import java.util.List;

import wg.com.photoselected.model.WGMedia;

/**
 * Create by Went_Gone on 2019/2/12
 **/
public interface PhotoSelectedListener {
//    void onPhotoSelected(List<WGMedia> medias);
    void onPhotos(List<String> paths);
}
