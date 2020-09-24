package com.example.ioclearn.bean.bind;

import android.app.Activity;
import android.view.View;

/**
 * Created by ddup on 2020/9/17.
 */
public final class ViewFinder {

    private View view;
    private Activity activity;

    public ViewFinder(View view) {
        this.view = view;
    }

    public ViewFinder(Activity activity) {
        this.activity = activity;
    }

    public View findViewById(int id) {
        if (view != null) return view.findViewById(id);
        if (activity != null) return activity.findViewById(id);
        return null;
    }
}
