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
 * on 2020/4/17
 */
public class InstrumentationProxy extends Instrumentation {

    private static final String SET_INTENT = "set_intent";
    private Instrumentation instrumentation;
    private PackageManager packageManager;

    public InstrumentationProxy(Instrumentation instrumentation, PackageManager packageManager) {
        this.packageManager = packageManager;
        this.instrumentation = instrumentation;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {

        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        if (resolveInfos == null || resolveInfos.size() == 0) {
            intent.putExtra(SET_INTENT, intent.getComponent().getClassName());
            intent.setClassName(who, "my.test.androidplugin.PlayActivity");
        }

        try {
            Method execStartActivity = ApkUtil.getMethod(instrumentation, "execStartActivity", Context.class,
                    IBinder.class, IBinder.class, Activity.class, Intent.class,
                    int.class, Bundle.class);
            return (ActivityResult) execStartActivity.invoke(instrumentation, who, contextThread,
                    token, target, intent, requestCode, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String stringExtra = intent.getStringExtra(SET_INTENT);
        if (!TextUtils.isEmpty(stringExtra)) {
            return super.newActivity(cl, stringExtra, intent);
        }
        return super.newActivity(cl, className, intent);
    }
}
