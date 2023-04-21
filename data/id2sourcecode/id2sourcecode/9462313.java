    public static String write(InputStream inputStream, String path) throws Exception {
        OutputStream outputStream = new FileOutputStream(path);
        int read = 0;
        byte[] buffer = new byte[8192];
        while ((read = inputStream.read(buffer, 0, 8192)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        return path;
    }
