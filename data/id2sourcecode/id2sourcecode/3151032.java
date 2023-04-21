    public static void copyURL(java.net.URL url, java.io.File file) throws java.io.IOException {
        if (DEBUG.IO) out("VueUtil: copying " + url + " to " + file);
        copyStream(url.openStream(), new java.io.FileOutputStream(file));
    }
