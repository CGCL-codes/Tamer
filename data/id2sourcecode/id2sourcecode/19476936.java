    public static void copyResource(InputStream fin, File dest) throws IOException {
        if (fin == null) {
            LogFactory.getLog(ResourceLoader.class).error("Can't read from NULL InputStream brew!");
            return;
        }
        BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(dest));
        byte[] buffer = new byte[COPY_BUFFER_SIZE];
        int readBytes = fin.read(buffer);
        while (readBytes > -1) {
            fout.write(buffer, 0, readBytes);
            readBytes = fin.read(buffer);
        }
        fout.close();
        fin.close();
    }
