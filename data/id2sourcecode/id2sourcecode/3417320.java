    public static String loadAsString(URL url, String charset) throws IOException {
        InputStream is = url.openStream();
        try {
            return loadAsString(is, charset);
        } finally {
            is.close();
        }
    }
