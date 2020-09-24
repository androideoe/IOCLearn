package com.example.injectknife.viewfinder;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by ddup on 2020/9/21.
 */
public class ActivityProvider implements Provider {

    @Override
    public Context getContext(Object source) {
        return ((Activity) source);
    }

    @Override
    public View findViewById(Object source, int id) {
        return ((Activity)source).findViewById(id);
    }
}
