package wg.com.photoselected.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import wg.com.photoselected.R;
import wg.com.photoselected.model.WGMediaGroup;

/**
 * Create by Went_Gone on 2019/2/12
 **/
public class WGLeftAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<WGMediaGroup> groups;

    public WGLeftAdapter(Context context) {
        this.context = context;
        groups = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WGLeftViewHolder(LayoutInflater.from(context).inflate(R.layout.wg_item_left_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof WGLeftViewHolder){
            ((WGLeftViewHolder) viewHolder).bindView(i);
        }
    }

    @Override
    public int getItemCount() {
        return groups == null?0:groups.size();
    }

    public void setGroups(List<WGMediaGroup> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
        notifyDataSetChanged();
    }

    private class WGLeftViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView image;
        private TextView tvName;
        private View root;

        public WGLeftViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.wg_image);
            tvName = itemView.findViewById(R.id.wg_name);
            root = itemView;
        }

        private void bindView(final int position){
            WGMediaGroup item = groups.get(position);
            if (item.medias == null || item.medias.size() == 0)
                return;

            Uri uri = Uri.parse("file://" + item.medias.get(0).path);
            int width = 100, height = 100;
            ImageRequest request =ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder() .setOldController(image.getController()) .setImageRequest(request)
                    .build();
            image.setController(controller);
            tvName.setText(item.groupName+"("+item.medias.size()+")");

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemGroupClickListener != null){
                        onItemGroupClickListener.onItemGroupClick(position);
                    }
                }
            });
        }
    }

    public WGMediaGroup getItem(int position){
        if (groups.size()>position){
            return groups.get(position);
        }else {
            return null;
        }
    }

    private OnItemGroupClickListener onItemGroupClickListener;

    public void setOnItemGroupClickListener(OnItemGroupClickListener onItemGroupClickListener) {
        this.onItemGroupClickListener = onItemGroupClickListener;
    }

    public interface OnItemGroupClickListener{
        void onItemGroupClick(int position);
    }
}
