    public static String httpPost(final URL url, final String encoding, final KeyValue... dataToPost) throws IOException {
        assert url != null;
        final String strToPost = createHttpPostString(encoding, dataToPost);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        return httpPost(strToPost, conn);
    }
