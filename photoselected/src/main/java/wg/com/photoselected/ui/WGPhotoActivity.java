package wg.com.photoselected.ui;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wg.com.photoselected.PhotoManager;
import wg.com.photoselected.R;
import wg.com.photoselected.adapter.WGLeftAdapter;
import wg.com.photoselected.model.WGMediaGroup;
import wg.com.photoselected.util.WGStringConfig;
import wg.com.photoselected.view_model.WGPhotoViewModel;
import wg.com.photoselected.adapter.WGPhotoAdapter;
import wg.com.photoselected.model.WGMedia;
import wg.com.photoselected.widget.GridSpacingItemDecoration;


public class WGPhotoActivity extends WGBaseActivity implements WGLeftAdapter.OnItemGroupClickListener,
        WGPhotoAdapter.OnItemPhotoClickListener {
    private final int TO_PRE_PHOTO_VIEW_REQUEST_CODE = 0X02;
    private RecyclerView mRV;
    private RecyclerView mRVLeft;
    private int PERMISSION = 0x01;
    private WGPhotoViewModel photoViewModel;
    private WGPhotoAdapter mAdapter;
    private WGLeftAdapter mLeftAdapter;
    private DrawerLayout mDrawerLayout;
    private TextView mTVGroupName;
    private View mToolLayout;
    private TextView mTVNum,mTVSure;
    private ArrayList<WGMedia> mSelecteds = new ArrayList<>();
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wg_activity_photo);
        mRV = findViewById(R.id.wg_recyclerview);
        mRVLeft = findViewById(R.id.wg_left_recyclerview);
        mDrawerLayout = findViewById(R.id.wg_drawerlayout);
        mTVGroupName = findViewById(R.id.wg_group_name);
        mToolLayout = findViewById(R.id.wg_tool_layout);
        mTVNum = findViewById(R.id.wg_tool_num);
        mTVSure = findViewById(R.id.wg_tool_sure);
        mToolBar = findViewById(R.id.wg_tool_bar);
        setListener();

        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},PERMISSION);
    }

    private void setListener() {
        mDrawerToggle = new OnDragScrollListener(this, mDrawerLayout, mToolBar, R.string.wg_open, R.string.wg_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mRV.setLayoutManager(new GridLayoutManager(this,4));
        mRV.addItemDecoration(new GridSpacingItemDecoration(4,10,false));
        mAdapter = new WGPhotoAdapter(this);
        mAdapter.setOnItemPhotoClickListener(this);
        mRV.setAdapter(mAdapter);

        mRVLeft.setLayoutManager(new LinearLayoutManager(this));
        mLeftAdapter = new WGLeftAdapter(this);
        mRVLeft.setAdapter(mLeftAdapter);
        mLeftAdapter.setOnItemGroupClickListener(this);

        mTVSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> paths = new ArrayList<>();
                for (WGMedia item :
                        mSelecteds) {
                    paths.add(item.path);
                }

                PhotoManager.getInstance()
                        .getPhotoSelectedListener()
                        .onPhotos(paths);

                WGPhotoActivity.this.finish();
            }
        });
    }

    private class OnDragScrollListener extends ActionBarDrawerToggle{

        public OnDragScrollListener(Activity activity, DrawerLayout drawerLayout, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        public OnDragScrollListener(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
           /* if (groupImages.size()==0 && dirImageStrings.size()==0){
                groupImage = PickPreferences.getInstance(PickPhotoActivity.this).getListImage();
                dirImage = PickPreferences.getInstance(PickPhotoActivity.this).getDirImage();
                groupImages.putAll(groupImage.getGroupMedias());
                dirImageStrings.addAll(dirImage.dirName);
                mListAdapter.notifyDataSetChanged();
            }*/
            super.onDrawerOpened(drawerView);
        }
    }


    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        initViewModel();
    }

    private void initViewModel() {
        photoViewModel = ViewModelProviders.of(this).get(WGPhotoViewModel.class);
        photoViewModel.getImage();
        photoViewModel.getMediasLiveData()
                .observe(this, new Observer<List<WGMedia>>() {
                    @Override
                    public void onChanged(@Nullable List<WGMedia> wgMedias) {
                        mAdapter.setMediaData(wgMedias);
                    }
                });

        photoViewModel.getMediaGroupLiveData()
                .observe(this, new Observer<List<WGMediaGroup>>() {
                    @Override
                    public void onChanged(@Nullable List<WGMediaGroup> wgMediaGroups) {
                        mLeftAdapter.setGroups(wgMediaGroups);
                    }
                });
    }

    @Override
    public void permissionFail(int requestCode) {
        super.permissionFail(requestCode);
    }

    @Override
    public void onItemGroupClick(int position) {
        WGMediaGroup item = mLeftAdapter.getItem(position);
        mDrawerLayout.closeDrawers();
        if (item == null) {
            return;
        }

        mTVGroupName.setText(item.groupName);
        mAdapter.setMediaData(item.medias);
    }

    @Override
    public void OnItemPhotoClick(int position) {

    }

    @Override
    public void OnItemPhotoClick(int position, WGPhotoAdapter.State state) {
        switch (state){
            case PRVIEW:
                PhotoManager.getInstance().setWgMedias(mAdapter.getDatas());
                PhotoManager.getInstance().setPrePhotoPosition(position);
                Intent intent = new Intent(this, WGPrePhotoActivity.class);
                intent.putParcelableArrayListExtra(WGStringConfig.SELECTED_MEDIAS,mSelecteds);
                startActivityForResult(intent,TO_PRE_PHOTO_VIEW_REQUEST_CODE);
                break;
            case SELECT:
                List<WGMedia> datas = mAdapter.getDatas();
                WGMedia media = datas.get(position);
                if (media.isSelected){
                    if (!mSelecteds.contains(media)){
                        mSelecteds.add(media);
                    }
                }else {
                    if (mSelecteds.contains(media)){
                        mSelecteds.remove(media);
                    }
                }

                boolean isSelected = false;
                for (WGMedia item:
                     datas) {
                    if (item.isSelected){
                        isSelected = true;
                        break;
                    }
                }

                if (!isSelected){
                    mAdapter.setShowCB(false);
                    mSelecteds.clear();
                    mToolLayout.setVisibility(View.GONE);
                }else {
                    if (mToolLayout.getVisibility() == View.GONE){
                        mToolLayout.setVisibility(View.VISIBLE);
                    }
                    mTVNum.setText("已选择："+mSelecteds.size());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TO_PRE_PHOTO_VIEW_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    mSelecteds = data.getParcelableArrayListExtra(WGStringConfig.SELECTED_MEDIAS);
                    if (mSelecteds == null || mSelecteds.size() == 0){
                        mToolLayout.setVisibility(View.GONE);
                    }else {
                        mToolLayout.setVisibility(View.VISIBLE);
                        mTVNum.setText("已选择："+mSelecteds.size());
                        mAdapter.setSelectes(mSelecteds);
                        mAdapter.setShowCB(true);
                    }
                }
                break;
        }
    }
}
