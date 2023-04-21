    public static void copy(File src, OutputStream dest) throws IOException {
        if (!src.exists()) {
            throw new IOException(StaticUtils.format(OStrings.getString("LFC_ERROR_FILE_DOESNT_EXIST"), new Object[] { src.getAbsolutePath() }));
        }
        FileInputStream fis = new FileInputStream(src);
        byte[] b = new byte[BUFSIZE];
        int readBytes;
        while ((readBytes = fis.read(b)) > 0) dest.write(b, 0, readBytes);
        fis.close();
    }
