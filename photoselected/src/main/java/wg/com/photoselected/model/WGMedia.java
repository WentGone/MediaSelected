package wg.com.photoselected.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by Went_Gone on 2019/2/12
 **/
public class WGMedia implements Parcelable {
    public String path;
    public String date;
    public String groupName;
    public boolean isSelected;

    public WGMedia() {
    }

    protected WGMedia(Parcel in) {
        path = in.readString();
        date = in.readString();
        groupName = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<WGMedia> CREATOR = new Creator<WGMedia>() {
        @Override
        public WGMedia createFromParcel(Parcel in) {
            return new WGMedia(in);
        }

        @Override
        public WGMedia[] newArray(int size) {
            return new WGMedia[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(date);
        dest.writeString(groupName);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
