    public static void copyStreamContents(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[1024];
        int readSize = -1;
        while ((readSize = is.read(buf)) > 0) {
            os.write(buf, 0, readSize);
        }
        is.close();
        os.close();
    }
