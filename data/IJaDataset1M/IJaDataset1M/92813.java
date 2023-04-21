package com.antlersoft.android.bc;

import java.io.File;
import android.content.Context;

/**
 * @author Michael A. MacDonald
 *
 */
class BCStorageContext8 implements IBCStorageContext {

    @Override
    public File getExternalStorageDir(Context context, String type) {
        return context.getExternalFilesDir(type);
    }
}
