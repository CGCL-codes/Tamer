    public void unpackImage(URL url) throws IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            unpackImage(is);
        } finally {
            FileUtil.closeAll(is);
        }
    }
