    public static String readString(URL url, String encoding) throws IOException {
        InputStream is = url.openStream();
        String ret = new String(readBytes(is));
        is.close();
        return ret;
    }
