package wg.com.photoselecteddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

import wg.com.photoselected.PhotoSelectedListener;
import wg.com.photoselected.Photo;
import wg.com.photoselected.model.WGMedia;

public class MainActivity extends AppCompatActivity implements PhotoSelectedListener{
    private static final String TAG = "MainActivity";
    private RecyclerView mRV;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Fresco.hasBeenInitialized()){
            Fresco.initialize(this);
        }
        setContentView(R.layout.activity_main);

        mRV = findViewById(R.id.recyclerview);
        mRV.setLayoutManager(new GridLayoutManager(this,4));
        adapter = new Adapter(this);
        mRV.setAdapter(adapter);

        findViewById(R.id.main_tv)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Photo.Builder(MainActivity.this)
                                .setListener(MainActivity.this)
                                .build();
                    }
                });
    }

    @Override
    public void onPhotos(List<String> paths) {
        adapter.setPaths(paths);
    }
}
