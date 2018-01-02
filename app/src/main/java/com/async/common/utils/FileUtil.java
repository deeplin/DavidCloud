package com.async.common.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * author: Ling Lin
 * created on: 2017/7/28 9:29
 * email: 10525677@qq.com
 * description:
 */

public class FileUtil {

    public static String readTextFileFromAssets(Context context, String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(fileName);
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputReader);

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line + "\t");
        }

        bufferedReader.close();
        inputReader.close();
        inputStream.close();

        return result.toString();
    }
}

