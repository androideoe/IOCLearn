package com.example.injectknife.viewfinder;

import android.content.Context;
import android.view.View;

/**
 * Created by ddup on 2020/9/21.
 */
public interface Provider {
    Context getContext(Object source);

    View findViewById(Object source, int id);
}
