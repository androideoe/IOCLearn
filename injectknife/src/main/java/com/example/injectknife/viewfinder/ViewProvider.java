package com.example.injectknife.viewfinder;

import android.content.Context;
import android.view.View;

/**
 * Created by ddup on 2020/9/21.
 */
public class ViewProvider implements Provider {

    @Override
    public Context getContext(Object source) {
        return ((View) source).getContext();
    }

    @Override
    public View findViewById(Object source, int id) {
        return ((View) source).findViewById(id);
    }
}
