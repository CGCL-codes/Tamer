    public List<Image> readImages(URL url) throws IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            return readImages(is);
        } finally {
            FileUtil.closeAll(is);
        }
    }
