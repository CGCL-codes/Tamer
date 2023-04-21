    public static File copyToTemp(InputStream is) throws IOException {
        File f = File.createTempFile("jtfile", null);
        FileOutputStream os = new FileOutputStream(f);
        byte[] buf = new byte[16 * 1024];
        int i;
        while ((i = is.read(buf)) != -1) os.write(buf, 0, i);
        is.close();
        os.close();
        return f;
    }
