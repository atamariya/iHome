package com.at.iHome.util;

import android.util.Base64;

/**
* Created by Anand.Tamariya on 02-Feb-15.
*/
public class BaseUtil {
    private BaseUtil() {

    }
    public static String encode(String str) {
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
    }
}
