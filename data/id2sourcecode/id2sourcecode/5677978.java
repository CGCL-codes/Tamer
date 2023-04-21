    public static final synchronized String getLocalResourcePath(ClassLoader cl, String resourcePath) throws IOException {
        File f = File.createTempFile("libfec", ".tmp");
        URL url = cl.getResource(resourcePath);
        if (url == null) {
            return null;
        }
        InputStream is = url.openStream();
        f.delete();
        OutputStream os = new FileOutputStream(f);
        byte[] b = new byte[1024];
        int c;
        while ((c = is.read(b)) != -1) {
            os.write(b, 0, c);
        }
        is.close();
        os.flush();
        os.close();
        return f.toString();
    }
