    public static void copyBytes(InputStream is, OutputStream os) throws Exception {
        byte[] buffer = new byte[4098];
        for (int i = 0; (i = is.read(buffer)) > 0; ) os.write(buffer, 0, i);
    }
