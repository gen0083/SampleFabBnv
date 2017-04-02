/*
 * Copyright 2017 gen0083
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gcreate.sample.samplefabbnv;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

public class BottomNavigationBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {
    private static final String TAG = "CustomBehavior";

    private boolean isSnackbarShowing = false;
    private Snackbar.SnackbarLayout snackbar;

    public BottomNavigationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationView child, View dependency) {
        return dependency instanceof AppBarLayout || dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, BottomNavigationView child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, BottomNavigationView child, View target, int dx, int dy, int[] consumed) {
        if (isSnackbarShowing) {
            if (snackbar != null) {
                updateSnackbarPaddingByBottomNavigationView(child);
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, BottomNavigationView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            AppBarLayout appbar = (AppBarLayout) dependency;
            float bottom = appbar.getBottom();
            float height = appbar.getHeight();
            float hidingRate = (height - bottom) / height;
            child.setTranslationY(child.getHeight() * hidingRate);
            return true;
        }
        if (dependency instanceof Snackbar.SnackbarLayout) {
            if (isSnackbarShowing) return true;
            isSnackbarShowing = true;
            snackbar = (Snackbar.SnackbarLayout) dependency;
            updateSnackbarPaddingByBottomNavigationView(child);
            return true;
        }
        return false;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, BottomNavigationView child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            isSnackbarShowing = false;
            snackbar = null;
        }
        super.onDependentViewRemoved(parent, child, dependency);
    }

    private void updateSnackbarPaddingByBottomNavigationView(BottomNavigationView view) {
        if (snackbar != null) {
            int bottomTranslate = (int) (view.getHeight() - view.getTranslationY());
            snackbar.setPadding(snackbar.getPaddingLeft(), snackbar.getPaddingTop(), snackbar.getPaddingRight(), bottomTranslate);
            snackbar.requestLayout();
        }
    }
}
