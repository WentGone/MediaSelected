package wg.com.photoselected.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wg.com.photoselected.PhotoProviderIm;
import wg.com.photoselected.model.WGMedia;
import wg.com.photoselected.model.WGMediaGroup;
import wg.com.photoselected.model.WGPhoto;

/**
 * Create by Went_Gone on 2019/2/12
 **/
public class WGPhotoViewModel extends AndroidViewModel implements PhotoProviderIm {
    private static final String TAG = "WGPhotoViewModel";
    private boolean haveMedia = false;
    private MutableLiveData<List<WGMedia>> mediasLiveData = new MutableLiveData<>();
    private MutableLiveData<List<WGMediaGroup>> mediaGroupLiveData = new MutableLiveData<>();
    public WGPhotoViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<WGMedia>> getMediasLiveData() {
        return mediasLiveData;
    }

    public MutableLiveData<List<WGMediaGroup>> getMediaGroupLiveData() {
        return mediaGroupLiveData;
    }

    @Override
    public void getImage() {
        final List<WGMedia> medias = new ArrayList<>();
        final List<WGMediaGroup> groups = new ArrayList<>();

        mediaObservable()
               .subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<WGPhoto>() {
                   @Override
                   public void onSubscribe(Disposable d) {
                   }

                   @Override
                   public void onNext(WGPhoto wgMedia) {
                       if (!medias.contains(wgMedia)){
                           medias.add(wgMedia);
                       }

                       haveMedia = false;
                       for (WGMediaGroup itemGroup :
                               groups) {
                           if (itemGroup.groupName.equals(wgMedia.groupName)){
                               itemGroup.medias.add(wgMedia);
                               haveMedia = true;
                               break;
                           }
                       }

                       if (!haveMedia){
                           WGMediaGroup group = new WGMediaGroup();
                           group.groupName = wgMedia.groupName;
                           group.medias = new ArrayList<>();
                           group.medias.add(wgMedia);
                           groups.add(group);
                       }
                   }

                   @Override
                   public void onError(Throwable e) {

                   }

                   @Override
                   public void onComplete() {
                       WGMediaGroup group = new WGMediaGroup();
                       group.groupName = "全部";
                       group.medias = medias;
                       groups.add(0,group);

                       mediasLiveData.postValue(medias);
                       mediaGroupLiveData.postValue(groups);
                   }
               });
    }

    @Override
    public void getMediasByGroupName(String groupName) {
        List<WGMediaGroup> value = mediaGroupLiveData.getValue();
        if (value == null || value.size() == 0)
            return;
        if (TextUtils.isEmpty(groupName)){
            mediasLiveData.postValue(value.get(0).medias);
        }else {
            for (WGMediaGroup group :
                    value) {
                if (group.groupName.equals(groupName)){
                    mediasLiveData.postValue(group.medias);
                    break;
                }
            }
        }
    }

    private Observable<WGPhoto> mediaObservable(){
        return Observable.create(new ObservableOnSubscribe<WGPhoto>() {
            @Override
            public void subscribe(ObservableEmitter<WGPhoto> emitter) throws Exception {
                Cursor cursor = null;
                try {
                    cursor = getPhotoCursor();
                    WGPhoto wgPhoto = null;
                    while (cursor.moveToNext() && !emitter.isDisposed()){
                        wgPhoto = new WGPhoto();
                        String path = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        wgPhoto.path = path;
                        wgPhoto.groupName = new File(path).getParentFile().getName();
                        emitter.onNext(wgPhoto);
                    }
                    cursor.close();

                    emitter.onComplete();
                }catch (Exception e){
                    cursor.close();
                    emitter.onError(e);
                }
            }
        });
    }

    /**
     * 获取图片游标
     * @return
     */
    private Cursor getPhotoCursor() {
        String photoSelectedColumns = MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?";
        String[] selectionArgs = new String[]{"image/jpeg", "image/png", "image/gif"};

        String[] mediaColumns = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_ADDED,
        };

        ContentResolver mContentResolver = getApplication().getContentResolver();
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";

        return mContentResolver.query(mImageUri, mediaColumns,photoSelectedColumns,
                selectionArgs, sortOrder);
    }

    private Cursor getImageThumbCursor(Cursor cursorImage){
        String[] thumbColumns = new String[]{
                MediaStore.Images.Thumbnails._ID,
                MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA
        };
        int id = cursorImage.getInt(cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
        String selection = MediaStore.Images.Thumbnails.IMAGE_ID +"=?";
        String[] selectionArgs1 = new String[]{
                id+""
        };
        Cursor thumbCursor = getApplication().getContentResolver()
                .query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs1, null);
        return thumbCursor;
    }

}
