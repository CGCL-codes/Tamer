    public static void copy(InputStream is, OutputStream os) throws IOException {
        BufferedOutputStream bos = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(os);
            byte[] buf = new byte[1024];
            int read = 0;
            while ((read = bis.read(buf)) != -1) bos.write(buf, 0, read);
        } finally {
            if (bos != null) bos.flush();
        }
    }
