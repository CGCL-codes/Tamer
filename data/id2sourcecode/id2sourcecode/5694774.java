    private static void get(String u) throws IOException {
        URL url = new URL(u);
        URLConnection cn = url.openConnection();
        cn.connect();
        InputStream stream = cn.getInputStream();
        if (stream == null) {
            throw new RuntimeException("stream is null");
        }
        byte[] data = new byte[1024];
        stream.read(data);
        assertTrue(new String(data).indexOf("<html>") >= 0);
    }
