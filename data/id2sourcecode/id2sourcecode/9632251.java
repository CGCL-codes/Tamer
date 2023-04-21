    public static final void copyStreamToStream(InputStream src, OutputStream dst, boolean close_streams) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(src);
            bos = new BufferedOutputStream(dst);
            int count;
            byte[] buffer = new byte[4096];
            while ((count = bis.read(buffer, 0, 4096)) > 0) dst.write(buffer, 0, count);
            bos.flush();
        } finally {
            if (close_streams) {
                try {
                    if (bis != null) bis.close();
                } catch (IOException e) {
                    Debug.debug(e);
                }
                try {
                    if (bos != null) bos.close();
                } catch (IOException e) {
                    Debug.debug(e);
                }
            }
        }
    }
