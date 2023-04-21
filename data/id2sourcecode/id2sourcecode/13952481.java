    private static void copy(InputStream is, OutputStream os) throws IOException {
        InputStream bis = null;
        OutputStream bos = null;
        try {
            if (is instanceof BufferedInputStream) {
                bis = is;
            } else {
                bis = new BufferedInputStream(is);
            }
            if (os instanceof BufferedOutputStream) {
                bos = os;
            } else {
                bos = new BufferedOutputStream(os);
            }
            byte[] buf = new byte[1024];
            int nread = -1;
            while ((nread = bis.read(buf)) != -1) {
                bos.write(buf, 0, nread);
            }
        } finally {
            bos.close();
            bis.close();
        }
    }
