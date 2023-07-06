package org.android.andserverswagger;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.yanzhenjie.andserver.util.IOUtils;

import org.android.andserverswagger.swagger.SwaggerInit;

import java.io.File;

import io.swagger.models.Swagger;

public class App extends Application {
    private static Context context;
    private static Swagger swagger;
    private static App mInstance;
    private File mRootDir;
    @Override
    public void onCreate() {
        super.onCreate();
        if (mInstance == null) {
            mInstance = this;
            initRootPath();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.context = base;
        SwaggerInit swaggerInit = new SwaggerInit("org.android.andserverswagger.controller","true","127.0.0.1:8080","1.0");
        swaggerInit.init();
        this.swagger = swaggerInit.getSwagger();
    }

    public static Context getContext() {
        return context;
    }

    public static Swagger getSwagger() {
        return swagger;
    }
    @NonNull
    public static App getInstance() {
        return mInstance;
    }

    @NonNull
    public File getRootDir() {
        return mRootDir;
    }

    private void initRootPath() {
        if (mRootDir != null) {
            return;
        }

        mRootDir = new File(getFilesDir(), "AndServer");
        IOUtils.createFolder(mRootDir);
    }
}
