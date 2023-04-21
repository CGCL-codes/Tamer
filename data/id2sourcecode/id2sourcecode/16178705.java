    public static void downloadWebpage(URL url, String dir, String fileName, int maxFileDownloadSize) {
        Integer totalBytesRead = null;
        File tempFile = null;
        try {
            File localFile = new File(dir, fileName);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
            urlConnection.setConnectTimeout(6000);
            urlConnection.setReadTimeout(6000);
            InputStream inputStream = urlConnection.getInputStream();
            tempFile = File.createTempFile("euklas-", null, new File(dir));
            tempFile.deleteOnExit();
            OutputStream outputStream = new FileOutputStream(tempFile);
            totalBytesRead = pump(inputStream, outputStream, maxFileDownloadSize);
            if (totalBytesRead != null) {
                localFile.getParentFile().mkdirs();
                if (!tempFile.renameTo(localFile)) {
                    System.err.println("[HTTPDownloader]: Error while trying to rename file '" + tempFile.getAbsolutePath() + "'!");
                }
            }
        } catch (IOException e) {
            System.err.println("[HTTPDownloader]: Error while trying to read from URL: " + url.toExternalForm());
        } finally {
            if ((tempFile != null) && (tempFile.exists())) {
                if (!tempFile.delete()) {
                    System.err.println("[HTTPDownloader]: Error while trying to delete file '" + tempFile.getAbsolutePath() + "'!");
                }
            }
        }
    }
