    public static void downloadUrlToFile(URL url, File targetFile) throws IOException {
        FileOutputStream targetFileWriting = null;
        InputStream urlDownloading = null;
        try {
            URLConnection conn = url.openConnection();
            urlDownloading = conn.getInputStream();
            targetFileWriting = new FileOutputStream(targetFile);
            int length = conn.getContentLength();
            targetFileWriting.getChannel().transferFrom(Channels.newChannel(urlDownloading), 0, length != -1 ? length : 1 << 24);
        } finally {
            if (urlDownloading != null) {
                try {
                    urlDownloading.close();
                } catch (IOException ioe) {
                    Helper.logger.log(Level.SEVERE, "Не удалось закрыть поток", ioe);
                }
            }
            if (targetFileWriting != null) {
                try {
                    targetFileWriting.close();
                } catch (IOException ioe) {
                    Helper.logger.log(Level.SEVERE, "Не удалось закрыть поток", ioe);
                }
            }
        }
    }
