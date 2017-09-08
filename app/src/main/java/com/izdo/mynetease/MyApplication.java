package com.izdo.mynetease;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * Created by iZdo on 2017/9/4.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 配置ImageLoader
        //        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        //        ImageLoader.getInstance().init(configuration);
        File sd = Environment.getExternalStorageDirectory();
        File image_loader_cache = new File(sd, "izdo");
        if (!image_loader_cache.exists()) {
            image_loader_cache.mkdirs();
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).discCache(new UnlimitedDiskCache(image_loader_cache)).diskCacheFileNameGenerator(new Md5FileNameGenerator()).build();
        ImageLoader.getInstance().init(config);

        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
