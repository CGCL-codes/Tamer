    public static String loadURL(URL url) throws Exception {
        InputStream in = url.openStream();
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        com.netbreeze.util.Utility.copyStream(in, bytesOut);
        in.close();
        bytesOut.close();
        return new String(bytesOut.toByteArray());
    }
