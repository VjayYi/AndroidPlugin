package my.test.androidplugin;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YiVjay
 * on 2020/4/17
 */
public class ApkUtil {

    private static ApkUtil apkUtil=new ApkUtil();
    private String dexPath ;


    public static ApkUtil getInstance(){
        return apkUtil;
    }


    public static List<File> getListFile(File file){
        File[] files = file.listFiles();
        List<File> list=new ArrayList<>();
        for (File file1 : files) {
            if(file1.getName().endsWith(".dex")||file1.getName().endsWith(".apk")){
                list.add(file1);
            }
        }
        return list;
    }

    public void loadDex(Context context, String name) {
        File fileDir = context.getDir("dex", Context.MODE_PRIVATE);
        dexPath = fileDir.getAbsoluteFile() + File.separator + name;
        File dexFile = new File(dexPath);
        if (!dexFile.exists()) {
            try {
                dexFile.createNewFile();
                copyFiles(context, name, dexFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        initDex(context,fileDir);
        ResUtil.getRes(context,dexPath);
    }

    public String getDexPath(){
        return this.dexPath;
    }

    //获取dexElements
    public void initDex(Context context,File fileDir) {
        List<File> listDex = getListFile(fileDir);
        try {
            //获取pathList
            Field pathListField = getField(context.getClassLoader(), "pathList");
            Object pathList = pathListField.get(context.getClassLoader());
            //获取老的dexElements
            Field dexElementsField = getField(pathList, "dexElements");
            Object[] oldDexElements = (Object[]) dexElementsField.get(pathList);
            //获取 makeDexElements 方法
            Method makeDexElements = getMethod(pathList, "makeDexElements", List.class, File.class,
                    List.class, ClassLoader.class);
            //装载fix.dex
            ArrayList<IOException> suppressedExceptions = new ArrayList<IOException>();
            Object[] myDexElements = (Object[]) makeDexElements.invoke(pathList, listDex, fileDir, suppressedExceptions, context.getClassLoader());

            //合并
            Object[] newDexElements = (Object[]) Array.newInstance(oldDexElements.getClass().getComponentType(), oldDexElements.length + myDexElements.length);
            System.arraycopy(myDexElements,0,newDexElements,0,myDexElements.length);
            System.arraycopy(oldDexElements,0,newDexElements,myDexElements.length,oldDexElements.length);

            //设置
            dexElementsField.set(pathList,newDexElements);
            Toast.makeText(context,"装载完成",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public static Field getField(Object obj, String name) throws NoSuchFieldException {
        Class<?> aClass = obj.getClass();
        while (aClass != null) {
            try {
                Field declaredField = aClass.getDeclaredField(name);

                if(!declaredField.isAccessible()){
                    declaredField.setAccessible(true);
                }
                return declaredField;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                aClass=aClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException("找不到属性");
    }

    public static Method getMethod(Object obj, String name,Class... value) throws NoSuchMethodException {
        Class<?> aClass = obj.getClass();
        while (aClass != null) {
            try {
                Method declaredMethod = aClass.getDeclaredMethod(name,value);

                if(!declaredMethod.isAccessible()){
                    declaredMethod.setAccessible(true);
                }
                return declaredMethod;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                aClass=aClass.getSuperclass();
            }
        }
        throw new NoSuchMethodException("找不到属性");
    }

}
