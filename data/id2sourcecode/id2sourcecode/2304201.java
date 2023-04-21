    private long gzip(File file) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            is = new BufferedInputStream(new FileInputStream(file));
            os = new GZIPOutputStream(bos);
            byte[] buf = new byte[4096];
            int nread = -1;
            while ((nread = is.read(buf)) != -1) {
                System.out.println(nread);
                os.write(buf, 0, nread);
            }
            os.flush();
        } finally {
            os.close();
            is.close();
        }
        return bos.size();
    }
