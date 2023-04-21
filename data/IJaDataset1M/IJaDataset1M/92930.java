package com.siema.games.freeland.lib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.res.AssetManager;
import com.siema.games.freeland.main.Stock;

public class CommonHelper {

    public static String getFileContents(String fileName) throws IOException {
        AssetManager assets = Stock.getInstance().context.getAssets();
        InputStream input = null;
        input = assets.open(fileName);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int buffer;
        try {
            buffer = input.read();
            while (buffer != -1) {
                output.write(buffer);
                buffer = input.read();
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
