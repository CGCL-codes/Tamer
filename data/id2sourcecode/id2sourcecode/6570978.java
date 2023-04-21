    public static final Map.Entry<byte[], Integer> readAllData(final URL url) throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            return readAllData(in);
        } finally {
            FileUtil.closeAll(in);
        }
    }
