package my.test.androidplugin;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YiVjay
 * on 2020/4/17
 */
public class ApkUtil {

    /**
     * 获取文件夹下所有.dex文件
     *
     * @param context
     * @param dexDir
     * @return
     */
    public static List<File> getDexList(Context context, File dexDir) {
        File[] files = dexDir.listFiles();
        List<File> fileList = new ArrayList<>();
        for (File file : files) {
            if (file.getName().endsWith(".apk")) {
                fileList.add(file);
            }
        }

        return fileList;
    }

    /**
     * 将文件拷贝到私有目录下
     *
     * @param context
     * @param fileName
     * @param desFile
     */
    public static void copyFiles(Context context, String fileName, File desFile) {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = context.getAssets().open(fileName);
            out = new FileOutputStream(desFile.getAbsolutePath());
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = in.read(bytes)) != -1)
                out.write(bytes, 0, len);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Field getField(Object object, String name) throws NoSuchFieldException {
        Class<?> aClass = object.getClass();
        while (aClass != null) {
            try {
                Field declaredField = aClass.getDeclaredField(name);
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                return declaredField;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                aClass = aClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException("找不到属性");
    }

    public static Method getMethod(Object object, String name,Class... value) throws NoSuchMethodException {
        Class<?> aClass = object.getClass();
        while (aClass != null) {
            try {
                Method declaredMethod = aClass.getDeclaredMethod(name, value);
                if (!declaredMethod.isAccessible()) {
                    declaredMethod.setAccessible(true);
                }
                return declaredMethod;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                aClass = aClass.getSuperclass();
            }
        }
        throw new NoSuchMethodException("找不到方法");
    }

}
