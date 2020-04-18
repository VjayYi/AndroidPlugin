package my.test.androidplugin;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YiVjay
 * on 2020/4/17
 */
public class LoadedApk {

    public static void copyApk(Context context, String fileName) {
        File apkDir = context.getDir("apk", Context.MODE_PRIVATE);
        String apkPath = apkDir.getAbsolutePath() + File.separator + fileName;
        File apkFile = new File(apkPath);
        if (!apkFile.exists()) {
            try {
                apkFile.createNewFile();
                ApkUtil.copyFiles(context, fileName, apkFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            loadApk(context,apkDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoadedResource.initResources(context,apkPath);
    }

    public static void loadApk(Context context, File fileDir) throws Exception {
        List<File> dexList = ApkUtil.getDexList(context, fileDir);
        Field pathListFiled = ApkUtil.getField(context.getClassLoader(), "pathList");
        Object pathList = pathListFiled.get(context.getClassLoader());
        Field dexElementsField = ApkUtil.getField(pathList, "dexElements");
        Object[] oldDexElements = (Object[]) dexElementsField.get(pathList);

        ArrayList<IOException> suppressedExceptions = new ArrayList<IOException>();
        Method makeDexElements = ApkUtil.getMethod(pathList, "makeDexElements", List.class,
                File.class, List.class, ClassLoader.class);
        Object[] newDexElements = (Object[]) makeDexElements.invoke(pathList, dexList, fileDir, suppressedExceptions, context.getClassLoader());

        Object[] allElements = (Object[]) Array.newInstance(oldDexElements.getClass().getComponentType(),
                oldDexElements.length+ newDexElements.length);

        System.arraycopy(newDexElements,0,allElements,0,newDexElements.length);
        System.arraycopy(oldDexElements,0,allElements,newDexElements.length,oldDexElements.length);

        dexElementsField.set(pathList,allElements);
        Toast.makeText(context,"加载成功",Toast.LENGTH_SHORT).show();
    }

}
