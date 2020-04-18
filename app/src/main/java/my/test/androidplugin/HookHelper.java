package my.test.androidplugin;

import android.app.Instrumentation;
import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by YiVjay
 * on 2020/4/17
 */
public class HookHelper {


    public static void initHook(Context context) throws Exception{
        Field mMainThreadField = ApkUtil.getField(context, "mMainThread");
        Object mMainThread = mMainThreadField.get(context);
        Field mInstrumentationField = ApkUtil.getField(mMainThread, "mInstrumentation");
        Object mInstrumentation = mInstrumentationField.get(mMainThread);
        mInstrumentationField.set(mMainThread,new InstrumentationProxy((Instrumentation) mInstrumentation,context.getPackageManager()));
    }
}
