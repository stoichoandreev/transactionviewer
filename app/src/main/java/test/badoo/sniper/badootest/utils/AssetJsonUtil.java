package test.badoo.sniper.badootest.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sniper on 1/9/16.
 */
public class AssetJsonUtil {
    public static String AssetJSONFile (String filename, AssetManager manager) throws IOException {
        String json = null;
        InputStream is = manager.open(filename);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");
        return json;
    }
}
