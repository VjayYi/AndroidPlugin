package my.test.androidplugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by YiVjay
 * on 2020/4/17
 */
public class LoadedResource {
    private static Resources resources;

    public static void initResources(Context context, String filePath) {
        try {
            Class cls=Class.forName("android.content.res.AssetManager");
            Object assetManager = cls.newInstance();
            Method addAssetPath = ApkUtil.getMethod(assetManager, "addAssetPath", String.class);
            addAssetPath.invoke(assetManager,filePath);
            resources=new Resources((AssetManager) assetManager,context.getResources().getDisplayMetrics()
            ,context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Resources getResources() {
        return resources;
    }

}
