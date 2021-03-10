package com.example.ins_images_picker_example;

import android.content.Context;

import com.example.ins_images_picker.PictureSelectorEngineImp;
import com.luck.picture.lib.app.IApp;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.crash.PictureSelectorCrashUtils;
import com.luck.picture.lib.engine.PictureSelectorEngine;

import io.flutter.app.FlutterApplication;


public class App extends FlutterApplication implements IApp {
    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        PictureAppMaster.getInstance().setApp(this);
        PictureSelectorCrashUtils.init((t, e) -> {
        });
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public PictureSelectorEngine getPictureSelectorEngine() {
        return new PictureSelectorEngineImp();
    }

}
