package my.test.androidplugin;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by YiVjay
 * on 2020/4/29
 */
public class InstrumentationProxy extends Instrumentation {

    private static String TARTET="target";
    private Instrumentation instrumentation;
    private PackageManager packageManager;

    public InstrumentationProxy(Instrumentation instrumentation,PackageManager packageManager){
        this.instrumentation=instrumentation;
        this.packageManager=packageManager;
    }

    /**
     * 欺骗AMS
     * @param who
     * @param contextThread
     * @param token
     * @param target
     * @param intent
     * @param requestCode
     * @param options
     * @return
     */
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent,PackageManager.MATCH_ALL);
        //判断加载的是否是外部插件
        if(resolveInfos==null||resolveInfos.size()<=0){
            intent.putExtra(TARTET,intent.getComponent().getClassName());
            intent.setClassName(who,"my.test.androidplugin.PluginActivity");
        }
        try {
            Method execStartActivity = ApkUtil.getMethod(instrumentation, "execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class
                    , Intent.class, int.class, Bundle.class);
            return (ActivityResult) execStartActivity.invoke(instrumentation,who,contextThread,token,target,
                    intent,requestCode,options);
        } catch ( Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 加载插件的activity
     * @param cl
     * @param className
     * @param intent
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String stringExtra = intent.getStringExtra(TARTET);
        if(!TextUtils.isEmpty(stringExtra)){
            return super.newActivity(cl, stringExtra, intent);
        }
        return super.newActivity(cl, className, intent);
    }
}
