    public static InputStream downloadStream(URL url, UpdateAsyncTask task) throws IOException {
        URLConnection connection = url.openConnection();
        int totalDownloadSize = connection.getContentLength();
        InputStream inputStream = connection.getInputStream();
        return new StreamCounter(inputStream, task, totalDownloadSize);
    }
