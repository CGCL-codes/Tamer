    public static byte[] read(URL url) throws IOException {
        if (url == null) {
            return null;
        }
        InputStream is = url.openStream();
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Streams.copy(is, os);
            return os.toByteArray();
        } finally {
            StreamUtils.close(is);
        }
    }
