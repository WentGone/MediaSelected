package wg.com.photoselecteddemo;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Went_Gone on 2019/2/12
 **/
public class Adapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> path;

    public Adapter(Context context) {
        this.context = context;
        path = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_layout_root,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder){
            ((ViewHolder) viewHolder).bindView(i);
        }
    }

    @Override
    public int getItemCount() {
        return path==null?0:path.size();
    }

    public void setPaths(List<String> paths){
        path.clear();
        path.addAll(paths);
        notifyDataSetChanged();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }

        private void bindView(int position){
            Uri uri = Uri.parse("file://"+path.get(position));
            image.setImageURI(uri);
        }
    }
}
