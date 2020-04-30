package my.test.androidplugin;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by YiVjay
 * on 2020/4/29
 */
public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        HookHelper.init(base);
    }

    @Override
    public Resources getResources() {
        return ResUtil.getResources()==null?super.getResources():ResUtil.getResources();
    }
}
