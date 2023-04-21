    public void downloadToCache(URL url) throws IOException {
        final URLConnection connection = url.openConnection();
        if (connection instanceof HttpURLConnection) {
            this.setCacheETag(url, connection.getHeaderField("ETag"));
            this.setCacheDate(url, new Date(connection.getDate()));
        } else {
            this.setCacheDate(url, new Date(connection.getLastModified()));
        }
        final InputStream input = connection.getInputStream();
        final ReadableByteChannel readChannel = Channels.newChannel(input);
        final FileChannel writeChannel = (new FileOutputStream(this.getCacheFile(url))).getChannel();
        final long BIG_NUM = 99999999999999l;
        writeChannel.transferFrom(readChannel, 0, BIG_NUM);
        writeChannel.close();
    }
