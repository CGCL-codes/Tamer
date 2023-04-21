    public static void saveURL(URL url, OutputStream os) throws IOException {
        InputStream is = url.openStream();
        byte[] buf = new byte[1048576];
        int n = is.read(buf);
        while (n != -1) {
            os.write(buf, 0, n);
            n = is.read(buf);
        }
    }
