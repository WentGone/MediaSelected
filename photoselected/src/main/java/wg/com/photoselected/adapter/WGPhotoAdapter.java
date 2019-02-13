package wg.com.photoselected.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import wg.com.photoselected.R;
import wg.com.photoselected.model.WGMedia;

/**
 * Create by Went_Gone on 2019/2/12
 **/
public class WGPhotoAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<WGMedia> mDatas;
    private boolean showCB;

    public WGPhotoAdapter(Context context) {
        this.context = context;
        mDatas = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WGPhotoPictureViewHolder(LayoutInflater.from(context).inflate(R.layout.wg_item_photo_picture_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof WGPhotoPictureViewHolder){
            ((WGPhotoPictureViewHolder) viewHolder).bindView(i);
        }
    }

    public void setMediaData(List<WGMedia> datas){
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas == null?0:mDatas.size();
    }

    private class WGPhotoPictureViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView image;
        private View root;
        private ImageView ivCB;

        public WGPhotoPictureViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.wg_image);
            ivCB = itemView.findViewById(R.id.wg_image_cb);
            root = itemView;
        }

        private void bindView(final int position){
            final WGMedia item = mDatas.get(position);
            if (item.path == null)
                return;
            Uri uri = Uri.parse("file://"+item.path);

            int width = 100, height = 100;
            ImageRequest request =ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder() .setOldController(image.getController()) .setImageRequest(request)
                    .build();

            image.setController(controller);
            image.setTag(item.path);

            ivCB.setVisibility(showCB?View.VISIBLE:View.GONE);
            ivCB.setImageResource(item.isSelected?R.mipmap.wg_check_selected:R.mipmap.wg_check_normal);

            if (item.isSelected){
//                scaleSmallAnimator(image);
            }

            root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.e("TAG", "onLongClick: "+firstAnimatorEnd );
                    if (!firstAnimatorEnd){
                        item.isSelected = true;
                        if (onItemPhotoClickListener != null){
                            onItemPhotoClickListener.OnItemPhotoClick(position,State.SELECT);
                        }
                        setShowCB(true);
                    }
                    return true;
                }
            });

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showCB){
                        item.isSelected = !item.isSelected;
                        if (!item.isSelected){
//                            scaleBigAnimator(image);
                        }
                        notifyItemChanged(position);
                    }

                    if (onItemPhotoClickListener != null){
                        onItemPhotoClickListener.OnItemPhotoClick(position,showCB?State.SELECT :State.PRVIEW);
                    }
                }
            });
        }
    }

    public List<WGMedia> getDatas(){
        return mDatas;
    }

    private boolean firstAnimatorEnd = false;
    private void scaleSmallAnimator(View view){
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.8f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.8f);
        scaleXAnim.start();
        scaleYAnim.start();
        scaleXAnim.setDuration(0);
        scaleYAnim.setDuration(0);
        scaleXAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                firstAnimatorEnd = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void scaleBigAnimator(View view){
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);
        scaleXAnim.start();
        scaleYAnim.start();
        scaleXAnim.setDuration(0);
        scaleYAnim.setDuration(0);
        scaleXAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void setShowCB(boolean showCB) {
        this.showCB = showCB;
        this.firstAnimatorEnd = showCB;
        notifyDataSetChanged();
    }

    public void setSelectes(List<WGMedia> medias){
        if (medias == null || medias.size() == 0)
            return;

        for (WGMedia media :
                mDatas) {
            for (WGMedia item :
                    medias) {
                if (media.path.equals(item.path)){
                    media.isSelected = true;
                    break;
                }
            }
        }
    }

    private WGMedia getItem(int position){
        if (mDatas.size()>position){
            return mDatas.get(position);
        }else {
            return null;
        }
    }

    private OnItemPhotoClickListener onItemPhotoClickListener;

    public void setOnItemPhotoClickListener(OnItemPhotoClickListener onItemPhotoClickListener) {
        this.onItemPhotoClickListener = onItemPhotoClickListener;
    }

    public interface OnItemPhotoClickListener{
        void OnItemPhotoClick(int position);
        void OnItemPhotoClick(int position,State state);
    }

    public enum State{
        PRVIEW, SELECT
    }
}
