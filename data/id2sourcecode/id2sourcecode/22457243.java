    public static void fetch(String path, String url) throws Exception {
        InputStream is = fetch(url);
        FileOutputStream fos = new FileOutputStream(path);
        while (true) {
            byte[] buf = new byte[1024 * 16];
            int numread = is.read(buf, 0, buf.length);
            if (numread == -1) break;
            fos.write(buf, 0, numread);
        }
        fos.close();
    }
