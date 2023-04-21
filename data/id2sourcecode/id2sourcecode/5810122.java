    public static String downloadUrlToFile(String url, String outputFileName) throws IOException {
        InputStream in = new URL(url).openStream();
        try {
            return writeStreamToFile(in, outputFileName);
        } finally {
            in.close();
        }
    }
