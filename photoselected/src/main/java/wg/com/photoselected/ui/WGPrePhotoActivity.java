package wg.com.photoselected.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import wg.com.photoselected.PhotoManager;
import wg.com.photoselected.R;
import wg.com.photoselected.adapter.WGPrePhotoAdapter;
import wg.com.photoselected.model.WGMedia;
import wg.com.photoselected.util.WGStringConfig;

public class WGPrePhotoActivity extends WGBaseActivity {
    private RecyclerView mRV;
    private WGPrePhotoAdapter mAdapter;
    private ArrayList<WGMedia> mSelected;
    private ImageView mIVCB;
    private static final String TAG = "WGPrePhotoActivity";
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wg_activity_pre_photo);
        mRV = findViewById(R.id.wg_recyclerview);
        mIVCB = findViewById(R.id.wg_iv_cb);
        mSelected = getIntent().getParcelableArrayListExtra(WGStringConfig.SELECTED_MEDIAS);
        if (mSelected == null){
            mSelected = new ArrayList<>();
        }
        setListener();
    }

    private void setListener() {
        mRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mAdapter = new WGPrePhotoAdapter(this);
        mRV.setAdapter(mAdapter);
        mAdapter.setMedias(PhotoManager.getInstance().getWgMedias());
        mRV.scrollToPosition(PhotoManager.getInstance().getPrePhotoPosition());
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRV);
        mRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastCompletelyVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
                mIVCB.setImageResource(R.mipmap.wg_check_normal);
                if (lastCompletelyVisibleItemPosition>=0){
                    currentPosition = lastCompletelyVisibleItemPosition;
                    mIVCB.setImageResource(PhotoManager.getInstance().getWgMedias().get(currentPosition).isSelected?R.mipmap.wg_check_selected
                            :R.mipmap.wg_check_normal);
                }
            }
        });

        mIVCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WGMedia media = PhotoManager.getInstance().getWgMedias().get(currentPosition);
                media.isSelected = !media.isSelected;
                mIVCB.setImageResource(media.isSelected?R.mipmap.wg_check_selected
                        :R.mipmap.wg_check_normal);
                if (media.isSelected){
                    if (!mSelected.contains(media)){
                        mSelected.add(media);
                    }
                }else {
                    if (mSelected.contains(media)){
                        mSelected.remove(media);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent getIntent = getIntent();
        getIntent.putParcelableArrayListExtra(WGStringConfig.SELECTED_MEDIAS,mSelected);
        setResult(RESULT_OK,getIntent);
        super.onBackPressed();
    }
}
