package com.guarino.milectura.utils;

import android.graphics.Bitmap;
import android.text.Layout;

public class CommonUtils {
    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    public static boolean isEllipsized(Layout l) {
        if (l != null) {
            int lines = l.getLineCount();
            if (lines > 0)
                if (l.getEllipsisCount(lines-1) > 0)
                    return true;
        }
        return false;
    }
}
