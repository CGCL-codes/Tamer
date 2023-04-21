    private static InputStream getInputStream(@NotNull String url, @NotNull ProgressIndicator progressIndicator) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
        InputStream is = UrlConnectionUtil.getConnectionInputStreamWithException(urlConnection, progressIndicator);
        int j = urlConnection.getResponseCode();
        switch(j) {
            default:
                throw new IOException(IdeBundle.message("error.connection.failed.with.http.code.N", j));
            case 200:
                progressIndicator.setIndeterminate(urlConnection.getContentLength() == -1);
                break;
        }
        return is;
    }
