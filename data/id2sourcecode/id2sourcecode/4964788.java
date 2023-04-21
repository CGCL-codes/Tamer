    public static byte[] loadURL(URL url) throws IOException {
        byte[] buf = new byte[1024];
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        int n;
        while ((n = in.read(buf)) > 0) {
            bout.write(buf, 0, n);
        }
        try {
            in.close();
        } catch (Exception ignored) {
        }
        return bout.toByteArray();
    }
