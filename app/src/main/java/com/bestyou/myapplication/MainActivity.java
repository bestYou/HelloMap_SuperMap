package com.bestyou.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.supermap.data.Environment;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();

        //初始化环境
        Environment.setLicensePath("/sdcard/SuperMap/license/");
        Environment.setTemporaryPath("/sdcard/SuperMap/temp/");
        Environment.setWebCacheDirectory("/sdcard/SuperMap/WebCatch");
        Environment.initialization(this);

        setContentView(R.layout.activity_main);

        //打开工作空间
        Workspace workspace = new Workspace();
        WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
        info.setServer("/sdcard/SuperMap/GeometryInfo/World.smwu");
        info.setType(WorkspaceType.SMWU);
        workspace.open(info);

        //将地图显示控件和工作空间关联
        MapView mapView = findViewById(R.id.mapview);
        MapControl mapcontrol = mapView.getMapControl();
        mapcontrol.getMap().setWorkspace(workspace);

        //打开工作空间中的第1幅地图
        String mapName = workspace.getMaps().get(0);
        mapcontrol.getMap().open(mapName);
        mapcontrol.getMap().refresh();
    }

    //动态权限适配
    public void checkPermission() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            Log.i("cbs","isGranted == "+isGranted);
            if (!isGranted) {
                this.requestPermissions(
                        new String[]{
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102
                );
            }
        }
    }

}
