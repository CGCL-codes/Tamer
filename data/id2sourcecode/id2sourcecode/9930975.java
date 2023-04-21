    private static byte[] getData(HttpURLConnection conn) throws IOException {
        int contentLength = conn.getContentLength();
        ApplicationProperties ap = ApplicationProperties.getSingletonInstance();
        BufferedInputStream in = new BufferedInputStream(conn.getInputStream(), ap.getDefaultDataBuffer());
        if (in == null) {
            throw new IOException(ErrorMessages.IO_STREAM_ERROR + "(INPUT STREAM)");
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int total = 0;
        int read = -1;
        byte[] buffer = new byte[40960];
        while ((read = in.read(buffer)) != -1) {
            total += read;
            bout.write(buffer, 0, read);
            if (contentLength > 0) {
                EventDispatcher.getSingletonInstance().fireDownloadEvent(DownloadEvent.DOWNLOADING, total, contentLength);
            }
        }
        in.close();
        return bout.toByteArray();
    }
