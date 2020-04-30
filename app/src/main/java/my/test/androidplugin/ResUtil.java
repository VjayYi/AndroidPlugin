package my.test.androidplugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;

/**
 * Created by YiVjay
 * on 2020/4/29
 */
public class ResUtil {
    private static Resources resources;

    /**
     * 加载外部资源文件
     * @param context
     * @param filePath
     */
    public static void getRes(Context context, String filePath){
        Class cls= AssetManager.class;
        try {
            Object assetManager = cls.newInstance();
            Method addAssetPath = cls.getDeclaredMethod("addAssetPath",String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(assetManager,filePath);
            resources=new Resources((AssetManager) assetManager,context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration());
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    public static Resources getResources(){
        return resources;
    }

}
