    public static long transfer(InputStream is, OutputStream os, int buffSize) throws IOException {
        long ret = 0;
        byte[] buff = new byte[buffSize];
        BufferedInputStream bis = new BufferedInputStream(is, 2048);
        try {
            int readIn;
            while ((readIn = bis.read(buff)) > 0) {
                ret += readIn;
                os.write(buff, 0, readIn);
            }
        } finally {
            bis.close();
        }
        return ret;
    }
