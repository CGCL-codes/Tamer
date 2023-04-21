    public void writePNG(OutputStream out, int partNum) throws UnsupportedOperationException {
        byte[] buff = new byte[128];
        int r = 0;
        try {
            InputStream in = constructionURL.openStream();
            while ((r = in.read(buff, 0, 128)) > 0) out.write(buff, 0, r);
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Failed delivering image.", e);
        }
    }
