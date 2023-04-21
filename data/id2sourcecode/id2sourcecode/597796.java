    void copyResource(String name) throws IOException {
        URL url = getClass().getResource(name);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(new File(outDir, name));
        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) os.write(buf, 0, len);
        is.close();
        os.close();
    }
