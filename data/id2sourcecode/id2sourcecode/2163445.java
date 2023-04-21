    private static void copy(InputStream is, OutputStream os) throws IOException {
        InputStream bis = null;
        OutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(os);
            byte[] buf = new byte[1024];
            int nread = -1;
            while ((nread = bis.read(buf)) != -1) {
                bos.write(buf, 0, nread);
            }
        } finally {
            bos.close();
            bis.close();
        }
        is.close();
        os.close();
    }
