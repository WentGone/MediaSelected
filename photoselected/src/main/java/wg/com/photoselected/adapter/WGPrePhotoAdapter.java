package wg.com.photoselected.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;
import java.util.List;

import me.relex.photodraweeview.PhotoDraweeView;
import wg.com.photoselected.R;
import wg.com.photoselected.model.WGMedia;

/**
 * Create by Went_Gone on 2019/2/12
 **/
public class WGPrePhotoAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<WGMedia> datas;

    public WGPrePhotoAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WGPreViewHolder(LayoutInflater.from(context).inflate(R.layout.wg_item_pre_photo_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof WGPreViewHolder) {
            ((WGPreViewHolder) viewHolder).bindView(i);
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void setMedias(List<WGMedia> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    private class WGPreViewHolder extends RecyclerView.ViewHolder {
        private PhotoDraweeView photoDraweeView;

        public WGPreViewHolder(@NonNull View itemView) {
            super(itemView);
            photoDraweeView = itemView.findViewById(R.id.wg_photo_drawee_view);
        }

        private void bindView(int position) {
            WGMedia item = datas.get(position);
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setUri(Uri.parse("file://" + item.path));
            controller.setOldController(photoDraweeView.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null || photoDraweeView == null) {
                        return;
                    }
                    photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            photoDraweeView.setController(controller.build());
        }
    }
}
