    static byte[] readUrlContent(final URL url) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        InputStream urlStrm = url.openStream();
        try {
            IoUtil.copyStream(urlStrm, result, 256);
        } finally {
            urlStrm.close();
        }
        return result.toByteArray();
    }
